-- Common Table Expressions : CTE : 공통 테이블 표현식
-- WITH 문을 사용하여 가독성을 높이고 복잡한 쿼리를 계층적으로 구성
-- ✅ 1. 가장 자주 발생한 이벤트 유형
WITH event_counts AS (
    SELECT event_type, COUNT(*) AS cnt
    FROM security_logs
    GROUP BY event_type
)
SELECT *
FROM event_counts
ORDER BY cnt DESC
LIMIT 1;


-- ✅ 2. 비활성화된 사용자 계정의 로그인 시도
WITH inactive_users AS (
    SELECT username
    FROM users
    WHERE is_active = false
)
SELECT *
FROM security_logs
WHERE parsed_log::json->>'user' IN (
    SELECT username FROM inactive_users
);
--
-- ✅ 3. IP별 평균 위험 레벨 계산
WITH risk_levels AS (
    SELECT
        ip,
        CASE
            WHEN risk_level = 'Low' THEN 1
            WHEN risk_level = 'Medium' THEN 2
            WHEN risk_level = 'High' THEN 3
            END AS risk_score
    FROM ip_addresses
)
SELECT ip, AVG(risk_score) AS avg_risk
FROM risk_levels
GROUP BY ip;
--
-- ✅ 4. 사용자별 최근 로그인 시도
WITH user_logins AS (
    SELECT
        parsed_log::json->>'user' AS username,
        detected_at,
        ROW_NUMBER() OVER (PARTITION BY parsed_log::json->>'user' ORDER BY detected_at DESC) AS rn
    FROM security_logs
    WHERE event_type ILIKE '%Login%'
)
SELECT username, detected_at
FROM user_logins
WHERE rn = 1;

--
-- ✅ 10. 국가별 위험 IP 비율
WITH total_ips AS (
    SELECT country, COUNT(*) AS total
    FROM ip_addresses
    GROUP BY country
),
     high_risk_ips AS (
         SELECT country, COUNT(*) AS high_risk
         FROM ip_addresses
         WHERE risk_level = 'High'
         GROUP BY country
     )
SELECT t.country,
       t.total,
       COALESCE(h.high_risk, 0) AS high_risk,
       ROUND(COALESCE(h.high_risk, 0)::decimal / t.total * 100, 2) AS high_risk_ratio
FROM total_ips t
         LEFT JOIN high_risk_ips h ON t.country = h.country;

--
-- ✅ 8. 중복된 로그인 IP 탐지
WITH ip_usage AS (
    SELECT
        parsed_log::json->>'ip' AS ip,
        COUNT(DISTINCT parsed_log::json->>'user') AS user_count
    FROM security_logs
    GROUP BY ip
)
SELECT *
FROM ip_usage
WHERE user_count > 1;