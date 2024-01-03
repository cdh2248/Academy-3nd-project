  CREATE TABLE Client(
    sequenceID NUMBER NOT NULL ENABLE, 
    clientID VARCHAR2(128 BYTE) NOT NULL unique, 
	password VARCHAR2(128 BYTE) not null, 
    clientEmail VARCHAR2(512 BYTE) not null unique, 
    keyword VARCHAR2(2056 byte),
    primary key(sequenceid)
    );
    
CREATE SEQUENCE client_seq
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE;              -- 사이클 없음

drop table client;
drop sequence client_seq;

insert into client(sequenceid, clientid, password, clientemail, keyword)
values(client_seq.nextval, 'tester', 'test', 'test@test.com', '');

select * from client;
commit;