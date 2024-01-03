  CREATE TABLE PLACEinfo(
    ID NUMBER NOT NULL ENABLE, 
    카테고리 VARCHAR2(128 BYTE), 
	소분류 VARCHAR2(128 BYTE), 
	제목 VARCHAR2(256 BYTE) NOT NULL ENABLE, 
	주소 VARCHAR2(512 BYTE), 
	전화번호 VARCHAR2(128 BYTE), 
	위도 NUMBER, 
	경도 NUMBER, 
	애완동물동반가능정보 VARCHAR2(256 BYTE), 
	주차시설 VARCHAR2(256 BYTE), 
	쉬는날 VARCHAR2(256 BYTE), 
	영업시간 VARCHAR2(256 BYTE), 
	이용요금 VARCHAR2(512 BYTE), 
	화장실설명 VARCHAR2(256 BYTE), 
	외국어안내서비스 VARCHAR2(256 BYTE), 
	사고보험가입여부 VARCHAR2(128 BYTE), 
	판매품목 VARCHAR2(256 BYTE), 
	편의시설 VARCHAR2(512 BYTE), 
	대표메뉴 VARCHAR2(256 BYTE), 
	포장가능여부 VARCHAR2(128 BYTE), 
	입실시간 VARCHAR2(128 BYTE), 
	퇴실시간 VARCHAR2(128 BYTE), 
	부대시설 VARCHAR2(512 BYTE), 
	키워드리스트 VARCHAR2(2048 BYTE), 
	이미지주소1 VARCHAR2(1024 BYTE), 
	이미지주소2 VARCHAR2(1024 BYTE), 
    primary key (id)
    );
    
CREATE SEQUENCE place_seq
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE;              -- 사이클 없음    
  
COMMIT;

select * from placeinfo;

CREATE TABLE placeinfo_backup AS
SELECT *
FROM placeinfo;

drop table placeinfo_test;
commit;



drop table placeinfo_test;
drop sequence place_seq;

select unique(카테고리) from placeinfo;

