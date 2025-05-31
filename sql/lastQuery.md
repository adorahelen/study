
## 가장 복잡한 SQL 쿼리

- 가상의 전자상거래 데이터를 기반으로 **월별 판매 실적, 고객 및 제품 순위, 영업 담당자 성과, 특정 조건에 맞는 고객 재활성화 대상**까지 복합적으로 분석하는 보고서를 생성합니다. 다양한 고급 SQL 기능을 한데 모아 활용하고 있습니다.

### 가정된 테이블 구조

* `customers`: `customer_id` (PK), `customer_name`, `email`, `region`, `signup_date`
* `products`: `product_id` (PK), `product_name`, `category`, `price`, `stock_quantity`, `last_stock_update`
* `orders`: `order_id` (PK), `customer_id` (FK), `order_date`, `sales_rep_id` (FK), `total_amount`
* `order_items`: `order_item_id` (PK), `order_id` (FK), `product_id` (FK), `quantity`, `item_price` (단가)
* `sales_reps`: `sales_rep_id` (PK), `rep_name`, `team_id` (FK), `commission_rate`
* `teams`: `team_id` (PK), `team_name`

---

### PostgreSQL 쿼리

```sql
WITH
    -- 1. 월별 주문 요약 CTE: 각 월의 총 판매액, 주문 수, 고유 고객 수, 평균 주문액 계산
    MonthlyOrderSummary AS (
        SELECT
            TO_CHAR(o.order_date, 'YYYY-MM') AS sales_month,
            COUNT(DISTINCT o.order_id) AS total_orders,
            COUNT(DISTINCT o.customer_id) AS distinct_customers,
            SUM(o.total_amount) AS monthly_total_sales,
            AVG(o.total_amount) AS monthly_avg_order_value
        FROM
            orders o
        WHERE
            o.order_date >= '2024-01-01' -- 특정 기간 제한
          AND o.order_date < '2025-01-01'
        GROUP BY
            sales_month
        ORDER BY
            sales_month
    ),
    -- 2. 제품별 총 판매액 CTE: 각 제품의 총 판매액과 판매 수량 계산
    ProductSales AS (
        SELECT
            oi.product_id,
            SUM(oi.quantity) AS total_quantity_sold,
            SUM(oi.quantity * oi.item_price) AS product_total_sales
        FROM
            order_items oi
        GROUP BY
            oi.product_id
    ),
    -- 3. 고가치 제품 및 재고 상태 CTE: 특정 카테고리에서 판매액이 높은 제품과 재고 상태 결합
    HighValueProductStock AS (
        SELECT
            ps.product_id,
            p.product_name,
            p.category,
            ps.product_total_sales,
            p.stock_quantity,
            ROW_NUMBER() OVER (PARTITION BY p.category ORDER BY ps.product_total_sales DESC) AS rank_in_category
        FROM
            ProductSales ps
                JOIN
            products p ON ps.product_id = p.product_id
        WHERE
            p.category IN ('Electronics', 'Home Appliances', 'Books') -- 특정 카테고리만 포함
          AND ps.product_total_sales >= 500000 -- 특정 판매액 이상
    ),
    -- 4. 영업 담당자별 성과 CTE: 각 담당자의 총 판매액, 주문 수, 평균 주문액 계산
    SalesRepPerformance AS (
        SELECT
            sr.sales_rep_id,
            sr.rep_name,
            t.team_name,
            SUM(o.total_amount) AS rep_total_sales,
            COUNT(DISTINCT o.order_id) AS rep_total_orders,
            AVG(o.total_amount) AS rep_avg_order_value,
            (SUM(o.total_amount) * sr.commission_rate) AS estimated_commission
        FROM
            orders o
                JOIN
            sales_reps sr ON o.sales_rep_id = sr.sales_rep_id
                JOIN
            teams t ON sr.team_id = t.team_id
        GROUP BY
            sr.sales_rep_id, sr.rep_name, t.team_name, sr.commission_rate
    ),
    -- 5. 팀별/개인별 영업 담당자 순위 및 전월 대비 실적 CTE
    RankedSalesRepPerformance AS (
        SELECT
            srp.sales_rep_id,
            srp.rep_name,
            srp.team_name,
            srp.rep_total_sales,
            srp.estimated_commission,
            RANK() OVER (PARTITION BY srp.team_name ORDER BY srp.rep_total_sales DESC) AS rank_in_team,
            NTILE(4) OVER (ORDER BY srp.rep_total_sales DESC) AS sales_quartile, -- 전체 영업사원 4분위
            LAG(srp.rep_total_sales, 1, 0) OVER (PARTITION BY srp.sales_rep_id ORDER BY srp.rep_total_sales DESC) AS previous_period_sales -- 가상의 이전 기간 실적 (ORDER BY는 여기서는 누적 판매액 기준)
        FROM
            SalesRepPerformance srp
    ),
    -- 6. 잠재적 이탈 고객 식별 CTE: 최근 6개월간 주문이 없는 고객
    PotentialChurnCustomers AS (
        SELECT
            c.customer_id,
            c.customer_name,
            c.email,
            c.region,
            MAX(o.order_date) AS last_order_date
        FROM
            customers c
                LEFT JOIN
            orders o ON c.customer_id = o.customer_id
        GROUP BY
            c.customer_id, c.customer_name, c.email, c.region
        HAVING
            MAX(o.order_date) IS NULL OR MAX(o.order_date) < CURRENT_DATE - INTERVAL '6 months'
    ),
    -- 7. 고객별 월별 주문 횟수 및 총액 CTE
    CustomerMonthlyOrders AS (
        SELECT
            customer_id,
            TO_CHAR(order_date, 'YYYY-MM') AS order_month,
            COUNT(order_id) AS monthly_order_count,
            SUM(total_amount) AS monthly_order_amount
        FROM
            orders
        GROUP BY
            customer_id, TO_CHAR(order_date, 'YYYY-MM')
    ),
    -- 8. 전월 대비 고객 주문 변화 분석 CTE (윈도우 함수 활용)
    CustomerOrderChange AS (
        SELECT
            cmo.customer_id,
            cmo.order_month,
            cmo.monthly_order_amount,
            LAG(cmo.monthly_order_amount, 1, 0) OVER (PARTITION BY cmo.customer_id ORDER BY cmo.order_month) AS prev_month_amount,
            (cmo.monthly_order_amount - LAG(cmo.monthly_order_amount, 1, 0) OVER (PARTITION BY cmo.customer_id ORDER BY cmo.order_month)) AS amount_change
        FROM
            CustomerMonthlyOrders cmo
    )

-- 최종 결과 쿼리: 위의 모든 CTE들을 조합하여 복합적인 보고서 생성
SELECT
    'Global Monthly Summary' AS report_type,
    mos.sales_month AS metric_key,
    mos.monthly_total_sales AS primary_metric,
    mos.total_orders AS secondary_metric_1,
    mos.distinct_customers AS secondary_metric_2,
    mos.monthly_avg_order_value AS secondary_metric_3,
    NULL AS product_info,
    NULL AS rep_info,
    NULL AS customer_info,
    NULL AS additional_data_1,
    NULL AS additional_data_2
FROM
    MonthlyOrderSummary mos

UNION ALL

SELECT
    'Top Products by Category' AS report_type,
    hp.category AS metric_key,
    hp.product_total_sales AS primary_metric,
    hp.stock_quantity AS secondary_metric_1,
    hp.rank_in_category AS secondary_metric_2,
    NULL AS secondary_metric_3,
    hp.product_name AS product_info,
    NULL AS rep_info,
    NULL AS customer_info,
    -- 재고 상태에 따른 텍스트 분류 (CASE 문)
    CASE
        WHEN hp.stock_quantity > 100 THEN 'High Stock'
        WHEN hp.stock_quantity BETWEEN 50 AND 100 THEN 'Medium Stock'
        ELSE 'Low Stock'
        END AS additional_data_1,
    -- 제품명에 특정 단어가 포함된 경우 플래그
    CASE
        WHEN hp.product_name ILIKE '%pro%' OR hp.product_name ILIKE '%plus%' THEN 'Premium Model'
        ELSE 'Standard Model'
        END AS additional_data_2
FROM
    HighValueProductStock hp
WHERE
    hp.rank_in_category <= 5 -- 각 카테고리별 상위 5개 제품만
ORDER BY
    report_type, metric_key, primary_metric DESC

    UNION ALL

SELECT
    'Sales Rep Performance' AS report_type,
    srp.rep_name AS metric_key,
    srp.rep_total_sales AS primary_metric,
    srp.rep_total_orders AS secondary_metric_1,
    srp.estimated_commission AS secondary_metric_2,
    srp.sales_quartile AS secondary_metric_3,
    NULL AS product_info,
    srp.team_name AS rep_info,
    NULL AS customer_info,
    srp.rank_in_team AS additional_data_1,
    -- 전월 대비 실적 변화 (수익 증가/감소)
    CASE
        WHEN srp.rep_total_sales > srp.previous_period_sales THEN 'Sales Growth'
        WHEN srp.rep_total_sales < srp.previous_period_sales THEN 'Sales Decline'
        ELSE 'No Change'
        END AS additional_data_2
FROM
    RankedSalesRepPerformance srp
WHERE
    srp.rep_total_orders >= 10 -- 10건 이상 주문을 처리한 영업사원만 포함
    AND EXISTS ( -- 해당 영업사원이 특정 지역 고객의 주문을 처리한 경우에만
        SELECT 1
        FROM orders o2
                JOIN customers c2 ON o2.customer_id = c2.customer_id
        WHERE o2.sales_rep_id = srp.sales_rep_id AND c2.region = 'Gyeonggi-do'
        LIMIT 1
    )

UNION ALL

SELECT
    'Potential Churn Customers' AS report_type,
    pcc.customer_name AS metric_key,
    NULL AS primary_metric,
    NULL AS secondary_metric_1,
    NULL AS secondary_metric_2,
    NULL AS secondary_metric_3,
    NULL AS product_info,
    NULL AS rep_info,
    pcc.customer_id AS customer_info,
    pcc.email AS additional_data_1,
    pcc.last_order_date::text AS additional_data_2 -- 날짜를 텍스트로 변환
FROM
    PotentialChurnCustomers pcc
WHERE
    pcc.region = 'Seoul' -- 서울 지역 이탈 가능 고객만
    AND pcc.signup_date < CURRENT_DATE - INTERVAL '1 year' -- 가입한 지 1년 이상된 고객 중

UNION ALL

SELECT
    'Customer Order Change Analysis' AS report_type,
    c.customer_name AS metric_key,
    coc.monthly_order_amount AS primary_metric,
    coc.prev_month_amount AS secondary_metric_1,
    coc.amount_change AS secondary_metric_2,
    NULL AS secondary_metric_3,
    NULL AS product_info,
    NULL AS rep_info,
    c.customer_id AS customer_info,
    coc.order_month AS additional_data_1,
    -- 월별 주문액 변화에 따른 고객 분류
    CASE
        WHEN coc.amount_change > 0 THEN 'Increased Spending'
        WHEN coc.amount_change < 0 THEN 'Decreased Spending'
        ELSE 'Stable Spending'
        END AS additional_data_2
FROM
    CustomerOrderChange coc
        JOIN
    customers c ON coc.customer_id = c.customer_id
WHERE
    ABS(coc.amount_change) > 100000 -- 10만원 이상 변화가 있는 고객만
    AND coc.order_month = TO_CHAR(CURRENT_DATE - INTERVAL '1 month', 'YYYY-MM') -- 지난달 기준

ORDER BY
    report_type, metric_key, primary_metric DESC;
```

---

---

## 복잡한 PostgreSQL 쿼리 요약

이 복잡한 PostgreSQL 쿼리는 **다수의 CTE(Common Table Expression)**를 활용하여 여러 단계의 분석을 수행한 후, 최종적으로 5개의 `SELECT` 문을 **`UNION ALL`**로 결합하여 하나의 통합 보고서를 생성합니다.

### 쿼리 복잡성의 핵심 요소

* **CTE (WITH 절)**: 복잡한 쿼리를 논리적 단계로 분리하고 재사용성을 높여, 가독성 좋은 구조로 복잡성을 관리합니다.
* **다양한 조인(JOIN)**: 여러 테이블의 데이터를 효율적으로 연결하며, `LEFT JOIN` 등을 통해 조건에 맞는 데이터뿐 아니라 매칭되지 않는 경우도 처리합니다.
* **그룹화 및 집계 (GROUP BY, SUM, COUNT, AVG)**: 데이터를 다양한 기준으로 요약하고 통계를 계산합니다.
* **HAVING 절**: `GROUP BY`로 집계된 결과에 추가적인 조건을 적용하여 필터링합니다.
* **윈도우 함수 (Window Functions)**: `ROW_NUMBER()`, `RANK()`, `NTILE()`, `LAG()` 등을 통해 순위, 구간별 분류, 이전 기간과의 비교 등 고급 분석을 가능하게 합니다.
* **다중 UNION ALL**: 각기 다른 분석 결과(월별 요약, 제품 순위, 영업 담당자 성과, 이탈 고객, 고객 주문 변화 등)를 단일한 형식의 보고서로 통합합니다.
* **복잡한 WHERE 절 및 서브쿼리/EXISTS**: `EXISTS` 구문, 날짜 함수, 문자열 비교(`ILIKE`), `CASE` 문 등을 활용하여 정교한 조건과 필터링을 구현합니다.
* **CASE 문**: 다양한 조건에 따라 데이터를 분류하거나 새로운 값을 생성합니다.
* **데이터 타입 변환**: 필요에 따라 데이터 타입을 명시적으로 변경합니다.

---

**결론적으로, 이 쿼리는 단순한 결합이 아니라 각 CTE 내에서 수행되는 복잡한 계산과 다양한 SQL 기능들의 유기적인 조합을 통해 강력하고 다층적인 분석을 수행하는 예시입니다.** 실제 운영에서는 성능 최적화를 위해 뷰나 ETL 과정을 거치는 것이 일반적입니다.

