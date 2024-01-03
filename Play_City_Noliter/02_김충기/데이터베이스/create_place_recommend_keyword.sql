create table placerecommend (
    sequenceid number primary key,
    카테고리 varchar2(128),
    제목 varchar2(512),
    가까운장소 varchar2(512),
    거리 varchar2(128),
    id number,
    FOREIGN KEY (id) REFERENCES placeinfo(id)
    );
    
CREATE SEQUENCE placerecommend_seq
  START WITH 1          -- 시작을 1부터
  INCREMENT BY 1        -- 1씩 증가
  NOMAXVALUE            -- 최대값 제한 없음
  NOCYCLE;
    
    
    