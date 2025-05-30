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
