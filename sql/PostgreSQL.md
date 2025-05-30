
## PostgreSQL `WHERE` 절 완벽 가이드

---

이 문서는 PostgreSQL에서 자주 사용되는 `WHERE` 절의 다양한 활용법과 함께 `WHERE 1=1` 구문의 사용 이유 및 예시를 정리합니다.

### 1. 다양한 `WHERE` 절 활용법 (PostgreSQL 기준)

#### 1.1. 기본 비교 연산자

레코드의 특정 컬럼 값을 다른 값과 비교할 때 사용합니다.

```sql
-- 연봉이 55,000 이상인 교사 조회
SELECT * FROM teachers
WHERE salary >= 55000;

-- 2015년 이후 입사한 교사
SELECT * FROM teachers
WHERE hire_date >= '2015-01-01';
```

#### 1.2. `BETWEEN`으로 범위 조건

컬럼 값이 특정 범위 내에 있는지 확인할 때 사용합니다.

```sql
-- 연봉이 50,000에서 55,000 사이인 교사
SELECT * FROM teachers
WHERE salary BETWEEN 50000 AND 55000;

-- 2015년~2018년 사이 입사한 교사
SELECT * FROM teachers
WHERE hire_date BETWEEN '2015-01-01' AND '2018-12-31';
```

#### 1.3. `LIKE`로 부분 일치 검색

문자열 컬럼에서 특정 패턴을 포함하는 레코드를 찾을 때 사용합니다. `%`는 0개 이상의 문자를, `_`는 단일 문자를 나타냅니다.

```sql
-- 학교명이 'High'를 포함하는 교사
SELECT * FROM teachers
WHERE school LIKE '%High%';

-- 성(last_name)이 S로 시작하는 교사
SELECT * FROM teachers
WHERE last_name LIKE 'S%';
```

#### 1.4. `IN`으로 여러 값 중 포함 여부

컬럼 값이 지정된 여러 값 중 하나와 일치하는지 확인할 때 사용합니다.

```sql
-- 특정 학교의 교사들 조회
SELECT * FROM teachers
WHERE school IN ('Lincoln High School', 'Kennedy High School', 'Riverdale High');

-- 특정 이름(first_name)의 교사들
SELECT * FROM teachers
WHERE first_name IN ('Alice', 'David', 'Olivia');
```

#### 1.5. `IS NULL` / `IS NOT NULL`

해당 컬럼의 값이 `NULL`인지 아닌지 확인할 때 사용합니다.

```sql
-- salary 값이 없는 교사
SELECT * FROM teachers
WHERE salary IS NULL;

-- hire_date가 존재하는 교사
SELECT * FROM teachers
WHERE hire_date IS NOT NULL;
```

#### 1.6. `AND`, `OR` 및 괄호로 복합 조건

여러 조건을 조합하여 사용할 때 `AND` (모든 조건 충족) 또는 `OR` (하나의 조건이라도 충족)를 사용합니다. 괄호를 사용하여 조건의 우선순위를 명확히 할 수 있습니다.

```sql
-- 연봉이 55,000 이상이고 입사일이 2016년 이후인 교사
SELECT * FROM teachers
WHERE salary >= 55000 AND hire_date >= '2016-01-01';

-- 성이 Lee이거나 연봉이 60,000 초과인 교사
SELECT * FROM teachers
WHERE last_name = 'Lee' OR salary > 60000;

-- AND/OR 혼합: 괄호로 우선순위 지정
SELECT * FROM teachers
WHERE (salary < 50000 OR salary > 60000) AND school LIKE '%High%';
```

#### 1.7. `NOT` 부정 조건

특정 조건의 반대(부정)를 지정할 때 사용합니다.

```sql
-- 특정 학교가 아닌 교사들
SELECT * FROM teachers
WHERE school NOT IN ('Kennedy High School', 'Lincoln High School');

-- 'High'가 학교명에 없는 교사
SELECT * FROM teachers
WHERE school NOT LIKE '%High%';
```

---

### 2. `WHERE 1=1`을 사용하는 이유와 예시

#### 2.1. 의미

`WHERE 1=1`은 항상 **참(TRUE)**인 조건입니다. 이 구문을 사용하는 주된 이유는 동적 쿼리를 생성할 때 조건을 쉽게 추가하기 위함입니다.

#### 2.2. 예시

**❌ 일반 쿼리 (동적 조건 추가 시 번거로움)**

```sql
SELECT * FROM teachers
WHERE first_name = 'Alice'
AND school LIKE '%High%';
```
* **문제점:** 여기에서 조건이 없을 수도 있고, 많을 수도 있다면 `WHERE` 절 제어가 까다로워집니다. 예를 들어, `WHERE` 절이 필요한 첫 번째 조건 앞에 `AND`를 붙일 수 없습니다.

**✅ `WHERE 1=1` 사용 예시 (추가 조건이 있어도 구문 에러 X)**

```sql
SELECT * FROM teachers
WHERE 1=1
  AND first_name = 'Alice'
  AND school LIKE '%High%';
```
* **장점:** `WHERE 1=1`을 사용하면 조건을 추가할 때 항상 `AND`를 붙여도 문법 오류가 발생하지 않습니다. 이는 프로그램에서 동적으로 쿼리 문자열을 조립할 때 매우 유용합니다.

**✅ 동적 쿼리 생성 코드 예시 (Python, Java 등에서 문자열로 조립할 때)**

```python
query = "SELECT * FROM teachers WHERE 1=1"
if name:
    query += f" AND first_name = '{name}'"
if salary_min:
    query += f" AND salary >= {salary_min}"
# print(query) # 최종 쿼리 문자열 확인 가능
```

#### 2.3. 요약

| 목적                  | `WHERE 1=1` 효과                   |
| :-------------------- | :--------------------------------- |
| 쿼리 조건을 유연하게 추가 | 항상 참이기 때문에 `AND`로 조건을 계속 붙이기 쉬움 |
| 쿼리 자동 생성 시 오류 방지 | 조건이 하나도 없어도 구문이 항상 유효함     |

---

### 3. 보너스: `WHERE FALSE` 또는 `1=0`

`WHERE 1=0` (또는 `WHERE FALSE`)은 항상 **거짓(FALSE)**인 조건입니다. 이 구문을 사용하면 쿼리 결과로 어떠한 레코드도 반환되지 않습니다. 주로 테스트 용도나 특정 조건에서 강제로 빈 결과를 반환하고 싶을 때 사용됩니다.

```sql
SELECT * FROM teachers WHERE 1=0;  -- 아무 결과도 반환하지 않음
```

---

### 4. 마무리 요약

| `WHERE` 유형   | 설명                                   | 예시                                             |
| :------------- | :------------------------------------- | :----------------------------------------------- |
| **기본 비교** | `=`, `>`, `<`, `!=` 등                  | `salary >= 55000`                                |
| **범위** | `BETWEEN ... AND ...`                  | `hire_date BETWEEN '2015-01-01' AND '2018-12-31'` |
| **부분 문자열** | `LIKE`, `ILIKE` (대소문자 무시)        | `school LIKE '%High%'`                           |
| **집합 포함 여부** | `IN`, `NOT IN`                         | `first_name IN ('Alice', 'David')`               |
| **Null 검사** | `IS NULL`, `IS NOT NULL`               | `salary IS NOT NULL`                             |
| **복합 조건** | `AND`, `OR`, `NOT` + 괄호 사용         | `(salary > 50000 AND hire_date > '2016-01-01')`  |
| **항상 참 조건** | `WHERE 1=1`                            | 동적 쿼리 빌드 시 유용                           |
| **항상 거짓 조건** | `WHERE 1=0`                            | 테스트용 또는 강제 0건 반환 시 사용                  |