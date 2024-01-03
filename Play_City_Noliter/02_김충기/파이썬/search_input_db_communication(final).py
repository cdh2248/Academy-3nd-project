from algorithm5 import recommend_distance, data_recommend
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import cx_Oracle
import pandas as pd
import math
import re
import time

# Oracle DB 접속 정보 설정
connection = cx_Oracle.connect("party/party@192.168.30.240:1521/xe")


# DB에서 계속 데이터를 호출하는 함수
def check_cdc_changes():

    with connection.cursor() as cursor:
        cursor.execute("SELECT SEQUENCEID, CATEGORY, KEYWORD, ADDRESS  FROM searchinput")
        result = cursor.fetchall()
        return result


while True:
    # DB 데이터 쿼리를 날려 LIST로 가져온다
    new_changes = check_cdc_changes()
    old = new_changes
    # 일정 시간 동안 대기 (예: 1초)
    time.sleep(1)
    new_changes = check_cdc_changes()
    if old!=new_changes:
        print("새로운 값이 들어왔습니다")
        new_db_data = new_changes[len(new_changes)-1]
        # 새로운 값 처리 로직 추가
        # 특정 카테고리와 키워드, 주소를 기반으로 제목(장소) 추천 및 거리 계산 실행
        추천카테고리 = new_db_data[1]  # None으로 두면 카테고리를 사용하지 않음
        추천키워드 = new_db_data[2]  # None으로 두면 키워드를 사용하지 않음
        추천주소 = new_db_data[3]  # None으로 두면 주소를 사용하지 않음
        top_n = 3
        recommended_distance = recommend_distance(data_recommend, 추천카테고리, 추천키워드, 추천주소, top_n)
        recommend_list = recommended_distance
        # print(recommend_list)
        searchResult = ""
        count = 0;
        for x in range(len(recommend_list)):
            count += 1
            if(count==7):
                break
            searchResult += recommend_list[x][1] + ","
            for i in range(len(recommend_list[0][2])):
                try:
                    if (i==2) :
                        break
                    searchResult += recommend_list[x][2][i][1] + ","
                except:
                    pass
            searchResult = searchResult.rstrip(',')
            searchResult += '/'
        searchResult = searchResult.rstrip('/')

        category_list = []
        for item in recommended_distance[:3]:

            카테고리, 제목, 거리정보 = item

            all_keywords = []
            for category, place, address, distance, keywords in 거리정보:
                all_keywords.extend(keywords)

            # 키워드 빈도수 계산 및 상위 5개 추출
            keyword_counts = pd.Series(all_keywords).value_counts()
            top_keywords = keyword_counts.head(10).index.tolist()
            top_keywords.sort(reverse=True)

            keywords_slash_delete = [re.match(r'([^/]+)', keyword).group(1) for keyword in top_keywords]
            set_keywords_slash_delete = list(set(keywords_slash_delete))
            category_list.append(set_keywords_slash_delete)

        baseString = ''
        for cate in category_list:
            for ca in cate:
                baseString = baseString + ca.strip() + ','
            baseString = baseString.rstrip(',') + '/'
        finalString = baseString.rstrip('/').rstrip(',')

        insert_sql = "INSERT INTO  searchresult(sequenceid, result) VALUES (searchresult_seq.nextval, :val2)"
        with connection.cursor() as cursor:
            cursor.execute(insert_sql, val2=searchResult)
            connection.commit()  # 변경사항을 커밋하여 반영
    else:
        print("새로운 값이 없습니다.")
