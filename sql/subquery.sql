-- ✅ 1. 위험도 ‘High’인 IP에서 발생한 로그인 시도
SELECT *
FROM security_logs
WHERE parsed_log::json->>'ip' IN (
    SELECT ip
    FROM ip_addresses
    WHERE risk_level = 'High'
);

-- ✅ 2. 비활성화된 사용자 계정의 로그인 시도 로그
SELECT *
FROM security_logs
WHERE parsed_log::json->>'user' IN (
    SELECT username
    FROM users
    WHERE is_active = false
);

--
-- ✅ 3. RDP Brute Force 공격이 발생한 국가
SELECT DISTINCT country
FROM ip_addresses
WHERE ip IN (
    SELECT parsed_log::json->>'ip'
    FROM security_logs
    WHERE event_type = 'RDP Brute Force'
);
--
-- ✅ 4. 가장 최근 로그인 시도 로그 5개
SELECT *
FROM (
         SELECT *
         FROM security_logs
         WHERE event_type ILIKE '%Login%'
         ORDER BY detected_at DESC
         LIMIT 5
     ) AS recent_login_attempts;
--
-- ✅ 6. 로그 수가 가장 많은 사용자
SELECT parsed_log::json->>'user' AS username,
       COUNT(*) AS log_count
FROM security_logs
GROUP BY username
HAVING COUNT(*) = (
    SELECT MAX(user_log_count)
    FROM (
             SELECT COUNT(*) AS user_log_count
             FROM security_logs
             GROUP BY parsed_log::json->>'user'
         ) AS sub
);

--
-- ✅ 10. 가장 많이 시도된 이벤트 유형
SELECT event_type
FROM (
         SELECT event_type,
                COUNT(*) AS count
         FROM security_logs
         GROUP BY event_type
         ORDER BY count DESC
         LIMIT 1
     ) AS most_common_event;