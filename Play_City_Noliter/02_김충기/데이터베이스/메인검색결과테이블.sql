select * from searchresult;
select * from main_result;
commit;

-- 관광 카테고리 검색결과
insert into main_result (sequenceid, category ,result)
values(main_result_seq.nextval,'관광' ,'강화 석수문,강화산성 북장대,강화 홍릉/강화 전등사/애관극장,영화공간주안,2층 다복집/강화 화문석마을,화문석문화관/강화 광성보,강화 초지진,강화 덕진진/쇠뿔고개길,배다리 헌책방 골목');

-- 전시 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'전시' ,'국제성서박물관,송암미술관,인천상륙작전기념관/집현전,인천국악회관,부평공원/배미꾸미조각공원,섬사랑굴사랑/인천관동갤러리,금강산추어탕,청관 (淸館)/인천상륙작전기념관,인천도시역사관,인천광역시립박물관/송도 초콜릿문화박물관,호텔빈');

-- 자연 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'자연', '석모도 칠면초 군락지,석모도,어류정항/계양꽃마루,계양산,부평공원/모도 배미꾸미 해변,시도 수기해변,신도·시도·모도 해안누리길(인천 삼형제섬길)/드림파크 야생화공원,청라생태공원,국립생물자원관/영종도 백운산,인천대교/백령도 사곶해변,백령도 용트림바위,백령도 콩돌해변');

-- 레저 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'레저', '함허동천(야영장),노을빛바다애글램핑,바다로글램핑/강화나들길/무의바다누리길,무의도 하나개해수욕장/어류정항,석모도 칠면초 군락지,석모도 민머루해변/삼산저수지,어류정항/선재도 트리캠핑장,선재오토캠핑장');

-- 쇼핑 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'쇼핑', '구월시장,호텔더디자이너스인천,모래내시장/부평깡시장,신기시장,부평종합시장/부평문화의거리,부평종합시장/송현시장,현대시장,신포패션문화의거리/신흥시장,부평문화의거리,부평종합시장/열우물전통시장(구 십정종합시장),중앙시장 전통혼수거리,용남시장');

-- 음식 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'음식', '돈비어천가,모심갈비,촌장골/명품삼계탕,향계,풍전식당/바다해물탕,우리해물탕,소래포구종합어시장/정정당당 화로구이(만수 화로구이),새마을식당 주안역점,음식(꽃게)특화거리/회마트서산갯마을/2층 다복집,예전');

-- 숙박 카테고리 검색결과
insert into main_result (sequenceid, category, result)
values(main_result_seq.nextval,'숙박', 'G관광호텔 월미도,싸이판모텔,베니키아바다의별호텔/st.179 (에스티),리버관광호텔,조박사한우마을/골든호텔 인천/그랜드 하얏트 인천,호텔오라/3S Boutique Hotel/딘관광호텔,호텔아띠,위드모텔');