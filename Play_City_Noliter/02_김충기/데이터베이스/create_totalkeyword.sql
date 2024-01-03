create table totalkeyword (
    index_id number primary key,
    category varchar2(512),
    keywords varchar2(2054)    
);

create sequence keyword_seq 
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE; 

select * from totalkeyword;
drop table totalkeyword;
drop sequence keyword_seq;