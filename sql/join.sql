-- 01. 조인 연습을 위한 테이블 생성 및 데이터 삽입

CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100),
                       role VARCHAR(20),
                       is_active BOOLEAN DEFAULT TRUE
);

-- 더미 데이터
INSERT INTO users (username, email, role, is_active) VALUES
                                                     ('alice', 'alice@example.com', 'admin', TRUE),
                                                     ('bob', 'bob@example.com', 'user', TRUE),
                                                     ('john', 'john@example.com', 'user', FALSE),
                                                     ('guest', 'guest@example.com', 'guest', TRUE),
                                                     ('lee', 'lee@example.com', 'vpn_user', TRUE);

CREATE TABLE ip_addresses (
                              ip VARCHAR(50) PRIMARY KEY,
                              country VARCHAR(50),
                              risk_level VARCHAR(10)
);

-- 더미 데이터
INSERT INTO ip_addresses (ip, country, risk_level) VALUES
                                                       ('203.0.113.1', 'USA', 'high'),
                                                       ('192.168.1.10', 'Korea', 'low'),
                                                       ('10.0.0.15', 'Korea', 'low'),
                                                       ('203.0.113.200', 'Russia', 'high'),
                                                       ('120.10.12.12', 'Japan', 'medium'),
                                                       ('10.0.0.5', 'Korea', 'critical');

CREATE TABLE threat_levels (
                               event_type VARCHAR(100) PRIMARY KEY,
                               severity VARCHAR(10),
                               recommended_action VARCHAR(255)
);

-- 더미 데이터
INSERT INTO threat_levels (event_type, severity, recommended_action) VALUES
                                                                         ('Brute Force Login', 'high', 'Block IP, force password reset'),
                                                                         ('Failed Login', 'low', 'Monitor'),
                                                                         ('Normal Login', 'none', 'No action'),
                                                                         ('SQL Injection Attempt', 'critical', 'Alert and block request'),
                                                                         ('Port Scan', 'medium', 'Rate limit or block IP'),
                                                                         ('DDoS Detected', 'critical', 'Activate mitigation system');


-- GROUP BY 는 원하는 데이터들 끼리 묶어서 처리하고 싶을 때 사용할 수 있다.
-- HAVING은 그룹에 대해 조건에 맞는 데이터만 뽑아 사용할 때 쓰인다


-- 01. INNER JOIN — 공통된 데이터만 추출
SELECT
    s.event_type,
    s.detected_at,
    u.username,
    u.email
FROM
    security_logs s
        INNER JOIN
    users u
    ON s.parsed_log::json->>'user' = u.username;

-- 02. LEFT JOIN — 왼쪽 테이블 전체 + 오른쪽 일치하는 것
--     * 모든 로그를 기준으로 사용자 정보를 붙이고, 사용자 정보가 없으면 NULL.
SELECT
    s.event_type,
    s.detected_at,
    s.parsed_log::json->>'user' AS username,
    u.email
FROM
    security_logs s
        LEFT JOIN
    users u
    ON s.parsed_log::json->>'user' = u.username;

-- 3. RIGHT JOIN — 오른쪽 테이블 전체 + 왼쪽 일치하는 것
--     * 모든 로그를 기준으로 사용자 정보를 붙이고, 사용자 정보가 없으면 NULL.
SELECT
    u.username,
    u.email,
    s.event_type,
    s.detected_at
FROM
    security_logs s
        RIGHT JOIN
    users u
    ON s.parsed_log::json->>'user' = u.username;

-- 4. FULL OUTER JOIN — 양쪽 모두 전부 + 매칭된 것
--     * 보안 로그 또는 사용자 중 하나라도 존재하면 출력 (양쪽 다 NULL 허용).
SELECT
    COALESCE(s.parsed_log::json->>'user', u.username) AS username,
    s.event_type,
    s.detected_at,
    u.email
FROM
    security_logs s
        FULL OUTER JOIN
    users u
    ON s.parsed_log::json->>'user' = u.username;

-- 5. CROSS JOIN — 가능한 모든 조합
--     * 모든 이벤트와 모든 위험도를 조합하여 “위협 시나리오” 만들기.
SELECT
    s.event_type AS log_event,
    t.event_type AS threat_event,
    t.severity
FROM
    security_logs s
        CROSS JOIN
    threat_levels t
WHERE
    s.is_threat = TRUE;

-- 06. 다중조인
SELECT
    u.username,
    u.email,
    s.event_type,
    i.country,
    t.severity,
    t.recommended_action
FROM
    security_logs s
        LEFT JOIN users u ON s.parsed_log::json->>'user' = u.username
        LEFT JOIN ip_addresses i ON s.parsed_log::json->>'ip' = i.ip
        LEFT JOIN threat_levels t ON s.event_type = t.event_type
WHERE
    s.is_threat = TRUE;