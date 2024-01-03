CREATE table test(
    id number,
    test_string varchar2(50),
    test_number number,
    primary key (id)
);

CREATE SEQUENCE test_seq
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE;              -- 사이클 없음
  
insert into test (id, test_string, test_number)
values (test_seq.nextval, 'tester', 1234);

select * from test;
commit;