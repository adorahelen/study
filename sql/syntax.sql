create schema test;
drop schema test;
-- 데이터베이스는 데이터를 저장하는 컨테이너이고,
-- 스키마는 컨테이너 안에 데이터들을 정리하고 관리하는 방식

create database study;
-- 01. 데이터베이스 생성

create table teachers (
                          id bigserial,
                          first_name varchar(25),
                          last_name varchar(50),
                          school varchar(50),
                          hire_date date,
                          salary numeric
);
-- 02. 테이블 생성, 이때 study 가 아닌, postgres 내부 public schema 에 생성된건 default 경로가 여기여서


INSERT INTO teachers (first_name, last_name, school, hire_date, salary) VALUES
    ('Alice', 'Johnson', 'Lincoln High School', '2015-08-25', 54000),
    ('Bob', 'Smith', 'Roosevelt Middle School', '2012-03-15', 58500),
    ('Catherine', 'Lee', 'Washington Elementary', '2018-09-10', 52000),
    ('David', 'Brown', 'Kennedy High School', '2010-05-18', 61000),
    ('Emma', 'Clark', 'Franklin Academy', '2017-01-03', 49900),
    ('Frank', 'Miller', 'Westside Prep', '2016-11-22', 53000),
    ('Grace', 'Taylor', 'Central Elementary', '2014-04-09', 55500),
    ('Henry', 'Lopez', 'North Ridge High', '2020-08-01', 47000),
    ('Irene', 'Garcia', 'Sunnyvale School', '2013-02-27', 57500),
    ('Jason', 'Martinez', 'Evergreen High School', '2011-06-30', 60000),
    ('Karen', 'Wilson', 'Lakeside Middle', '2016-09-19', 51000),
    ('Liam', 'Anderson', 'Riverdale High', '2019-05-24', 49500),
    ('Mia', 'Thomas', 'Hillcrest Elementary', '2018-07-13', 50500),
    ('Nathan', 'Walker', 'Greenfield High', '2013-12-04', 58500),
    ('Olivia', 'White', 'Brighton Academy', '2015-10-11', 54000),
    ('Peter', 'Hall', 'Sunset High School', '2014-06-15', 55000),
    ('Quinn', 'Allen', 'Springfield High', '2010-02-20', 63000),
    ('Rachel', 'Young', 'Liberty School', '2017-03-06', 49000),
    ('Samuel', 'King', 'Meadowbrook School', '2019-10-09', 51000),
    ('Tina', 'Scott', 'Oakwood Academy', '2021-08-20', 46500),
    ('Umar', 'Green', 'Brookfield High', '2012-01-25', 59500),
    ('Vanessa', 'Adams', 'Pine Valley Middle', '2014-11-12', 52000),
    ('William', 'Nelson', 'Rockridge High', '2013-03-17', 56000),
    ('Xena', 'Reed', 'Golden Gate Academy', '2016-05-28', 53500),
    ('Yusuf', 'Hughes', 'Starlight Prep', '2019-02-01', 50000),
    ('Zoe', 'Bailey', 'Parkside School', '2018-12-05', 51000),
    ('Aaron', 'Powell', 'Eastpoint High', '2015-04-21', 54500),
    ('Bella', 'Foster', 'Ridgewood School', '2017-07-17', 49500),
    ('Carlos', 'Bennett', 'Maple Leaf High', '2012-09-29', 58000),
    ('Diana', 'Coleman', 'Summit High School', '2011-01-10', 60000);
-- 03. 더미 데이터 30건 삽입

SELECT * FROM teachers ORDER BY id LIMIT 10;
-- 04. 삽입된 데이터 확인

--     ======================================

-- 01. 새로운 테이블 생성

CREATE TABLE security_logs (
                               id SERIAL PRIMARY KEY,
                               event_type TEXT,                    -- 이벤트 유형 (예: 로그인 실패, 포트 스캔 등)
                               detected_date DATE,                 -- 날짜
                               detected_time TIME,                 -- 시간
                               detected_at TIMESTAMP,             -- 전체 타임스탬프
                               response_delay INTERVAL,           -- 대응까지 걸린 시간
                               raw_log JSON,                      -- 원시 로그 (인덱싱 X)
                               parsed_log JSONB,                  -- 파싱된 로그 (인덱싱 O)
                               is_threat BOOLEAN                  -- 실제 위협 여부
);

-- 02. 더미 데이터 30개 삽입

INSERT INTO security_logs (
    event_type, detected_date, detected_time, detected_at,
    response_delay, raw_log, parsed_log, is_threat
) VALUES
-- 1~5: 로그인 관련
('Brute Force Login', '2025-05-30', '02:13:45', '2025-05-30 02:13:45', '10 minutes',
 '{"source_ip":"203.0.113.1","attempts":48,"username":"admin"}',
 '{"source_ip":"203.0.113.1","attempts":48,"username":"admin"}', TRUE),

('Failed Login', '2025-05-30', '03:45:22', '2025-05-30 03:45:22', '2 minutes',
 '{"user":"root","ip":"192.168.1.10"}',
 '{"user":"root","ip":"192.168.1.10"}', FALSE),

('Normal Login', '2025-05-30', '04:00:01', '2025-05-30 04:00:01', '30 seconds',
 '{"user":"alice","ip":"10.0.0.15","method":"2FA"}',
 '{"user":"alice","ip":"10.0.0.15","method":"2FA"}', FALSE),

('Multiple Failed Login', '2025-05-29', '18:12:45', '2025-05-29 18:12:45', '15 minutes',
 '{"user":"test","failures":6,"ip":"172.16.0.5"}',
 '{"user":"test","failures":6,"ip":"172.16.0.5"}', TRUE),

('Session Hijack Detected', '2025-05-28', '16:03:14', '2025-05-28 16:03:14', '45 minutes',
 '{"session_id":"abc123","geo_mismatch":true,"ip":"203.0.113.90"}',
 '{"session_id":"abc123","geo_mismatch":true,"ip":"203.0.113.90"}', TRUE),

-- 6~10: 포트 스캔
('Port Scan', '2025-05-29', '12:00:00', '2025-05-29 12:00:00', '5 minutes',
 '{"scanner_ip":"198.51.100.1","ports":[21,22,23,80,443]}',
 '{"scanner_ip":"198.51.100.1","ports":[21,22,23,80,443]}', TRUE),

('Port Scan', '2025-05-28', '21:43:32', '2025-05-28 21:43:32', '8 minutes',
 '{"scanner_ip":"203.0.113.200","ports":[135,137,139,445]}',
 '{"scanner_ip":"203.0.113.200","ports":[135,137,139,445]}', TRUE),

('Normal Login', '2025-05-27', '09:30:00', '2025-05-27 09:30:00', '2 minutes',
 '{"user":"john","ip":"10.0.0.20"}',
 '{"user":"john","ip":"10.0.0.20"}', FALSE),

('Port Scan', '2025-05-27', '17:15:45', '2025-05-27 17:15:45', '11 minutes',
 '{"scanner_ip":"192.0.2.22","ports":[22,3389]}',
 '{"scanner_ip":"192.0.2.22","ports":[22,3389]}', TRUE),

('Idle Session', '2025-05-26', '13:22:11', '2025-05-26 13:22:11', '5 minutes',
 '{"user":"guest","duration":"2h","last_active":"12:00"}',
 '{"user":"guest","duration":"2h","last_active":"12:00"}', FALSE),

-- 11~15: SQL Injection
('SQL Injection Attempt', '2025-05-30', '01:03:03', '2025-05-30 01:03:03', '30 minutes',
 '{"endpoint":"/login","payload":"'' OR 1=1 --"}',
 '{"endpoint":"/login","payload":"'' OR 1=1 --"}', TRUE),

('SQL Injection Attempt', '2025-05-29', '05:40:40', '2025-05-29 05:40:40', '12 minutes',
 '{"endpoint":"/search","payload":"admin'' --"}',
 '{"endpoint":"/search","payload":"admin'' --"}', TRUE),

('Form Tampering', '2025-05-28', '08:22:33', '2025-05-28 08:22:33', '3 minutes',
 '{"form":"signup","altered_fields":["role","isAdmin"]}',
 '{"form":"signup","altered_fields":["role","isAdmin"]}', TRUE),

('XSS Attempt', '2025-05-27', '19:10:55', '2025-05-27 19:10:55', '1 minute',
 '{"field":"comments","script":"<script>alert(1)</script>"}',
 '{"field":"comments","script":"<script>alert(1)</script>"}', TRUE),

('CSRF Detected', '2025-05-26', '10:00:00', '2025-05-26 10:00:00', '20 minutes',
 '{"user":"bob","csrf_token_mismatch":true}',
 '{"user":"bob","csrf_token_mismatch":true}', TRUE),

-- 16~20: 인증 우회, 악성 코드
('Unauthorized Access', '2025-05-30', '07:21:18', '2025-05-30 07:21:18', '14 minutes',
 '{"user":"unknown","endpoint":"/admin"}',
 '{"user":"unknown","endpoint":"/admin"}', TRUE),

('Malware Upload', '2025-05-29', '23:00:00', '2025-05-29 23:00:00', '2 hours',
 '{"file_name":"ransom.exe","md5":"abcd1234"}',
 '{"file_name":"ransom.exe","md5":"abcd1234"}', TRUE),

('Phishing Attempt', '2025-05-28', '04:44:44', '2025-05-28 04:44:44', '25 minutes',
 '{"email":"spoofed@support.com","subject":"Verify Your Account"}',
 '{"email":"spoofed@support.com","subject":"Verify Your Account"}', TRUE),

('DDoS Detected', '2025-05-27', '14:30:00', '2025-05-27 14:30:00', '3 hours',
 '{"requests_per_sec":5000,"target_ip":"10.0.0.5"}',
 '{"requests_per_sec":5000,"target_ip":"10.0.0.5"}', TRUE),

('RDP Brute Force', '2025-05-26', '02:12:00', '2025-05-26 02:12:00', '18 minutes',
 '{"target":"RDP","ip":"203.0.113.150","failures":45}',
 '{"target":"RDP","ip":"203.0.113.150","failures":45}', TRUE),

-- 21~30: 기타 이벤트
('VPN Login', '2025-05-30', '06:18:00', '2025-05-30 06:18:00', '1 minute',
 '{"user":"lee","location":"Japan","ip":"120.10.12.12"}',
 '{"user":"lee","location":"Japan","ip":"120.10.12.12"}', FALSE),

('VPN Anomaly', '2025-05-29', '09:09:00', '2025-05-29 09:09:00', '10 minutes',
 '{"user":"lee","prev_location":"Korea","current_location":"Brazil"}',
 '{"user":"lee","prev_location":"Korea","current_location":"Brazil"}', TRUE),

('Token Expired', '2025-05-28', '15:35:00', '2025-05-28 15:35:00', '1 minute',
 '{"user":"guest","token":"xyz"}',
 '{"user":"guest","token":"xyz"}', FALSE),

('DNS Tunneling', '2025-05-27', '10:45:00', '2025-05-27 10:45:00', '30 minutes',
 '{"domain":"malicious.domain.com","client_ip":"192.0.2.5"}',
 '{"domain":"malicious.domain.com","client_ip":"192.0.2.5"}', TRUE),

('WAF Blocked', '2025-05-26', '17:20:00', '2025-05-26 17:20:00', '2 minutes',
 '{"url":"/admin","rule_id":1012}',
 '{"url":"/admin","rule_id":1012}', TRUE),

('Email Spoofing', '2025-05-25', '12:12:00', '2025-05-25 12:12:00', '4 minutes',
 '{"from":"admin@bank.com","dkim_valid":false}',
 '{"from":"admin@bank.com","dkim_valid":false}', TRUE),

('File Integrity Alert', '2025-05-24', '07:15:00', '2025-05-24 07:15:00', '12 minutes',
 '{"file":"/etc/passwd","modified_by":"unknown"}',
 '{"file":"/etc/passwd","modified_by":"unknown"}', TRUE),

('Kernel Exploit Detected', '2025-05-23', '05:55:00', '2025-05-23 05:55:00', '1 hour',
 '{"exploit":"Dirty COW","user":"nobody"}',
 '{"exploit":"Dirty COW","user":"nobody"}', TRUE),

('New Admin Created', '2025-05-22', '11:30:00', '2025-05-22 11:30:00', '5 minutes',
 '{"created_by":"admin","new_user":"hacker"}',
 '{"created_by":"admin","new_user":"hacker"}', TRUE),

('Config Changed', '2025-05-21', '08:22:00', '2025-05-21 08:22:00', '3 minutes',
 '{"parameter":"firewall_rules","changed_by":"sysadmin"}',
 '{"parameter":"firewall_rules","changed_by":"sysadmin"}', FALSE);

-- 03. 테이블(security_logs) 데이터 조회
SELECT *
FROM security_logs
WHERE CAST(detected_at AS timestamp) >= '2025-05-29 00:00:00';

SELECT event_type, detected_at
FROM security_logs
WHERE is_threat = TRUE;

select * from security_logs;

SELECT event_type, is_threat, parsed_log ->> 'ip' AS ip_address
FROM security_logs
WHERE parsed_log ->> 'ip' IS NOT NULL;

SELECT event_type, detected_time
FROM security_logs
WHERE CAST(detected_time AS time) >= TIME '22:00';

SELECT event_type, raw_log ->> 'payload' AS payload
FROM security_logs
WHERE raw_log ->> 'payload' LIKE '%1=1%';

SELECT event_type, is_threat, COUNT(*) AS count
FROM security_logs
GROUP BY event_type, is_threat
ORDER BY count DESC;



