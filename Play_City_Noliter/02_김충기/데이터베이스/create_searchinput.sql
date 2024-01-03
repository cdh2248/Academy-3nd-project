create table searchinput (
    sequenceid number,
    category varchar2(256),
    keyword varchar2(256),
    address varchar2(256)
);

drop table searchinput;
drop sequence searchinput_seq;

CREATE SEQUENCE searchinput_seq
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE;    
  select * from placeinfo;
select * from searchinput;
select * from searchresult;
select * from placeinfo where 제목 like '%3S%';
insert into searchinput(sequenceid, category, keyword, address)
values(searchinput_seq.nextval, '음식', '맛집', '부평구');
insert into searchinput(sequenceid, category, keyword, address)
values(searchinput_seq.nextval, '관광', '', '중구');
commit;