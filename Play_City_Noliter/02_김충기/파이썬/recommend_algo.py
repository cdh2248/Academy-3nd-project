from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import cx_Oracle
import pandas as pd
import math
import time
## 라이브러리 임포트

## DB연결해 DATA 추출하기
connection = cx_Oracle.connect("party/party@192.168.30.240:1521/xe")
query = """select * from PlACEINFO"""
place_df = pd.read_sql(query, con=connection)
data=place_df[['카테고리','제목','주소','위도','경도','키워드리스트']]


# 데이터프레임에서 가져오기
data_recommend = data

# 데이터프레임을 카테고리와 키워드 컬럼만 남기고 복사
data_for_recommend = data_recommend[['카테고리', '키워드리스트', '위도', '경도', '주소']].copy()

# 각 컬럼의 NaN 값을 빈 문자열로 대체(전처리)
data_for_recommend.fillna('', inplace=True)

# 텍스트 데이터 합치기(분석할 텍스트 카테고리+키워드리스트)
data_for_recommend['combined'] = data_for_recommend['카테고리'] + ' ' + data_for_recommend['키워드리스트']

# TF-IDF 벡터화
tfidf_vectorizer = TfidfVectorizer(ngram_range=(1, 3))  # 텍스트데이터를 TF_IDF 행렬로 변환 ngram_range 파라미터를 통해서 구성 범위 설정
# "beautiful view"라는 문장이 있다면 ngram_range=(1, 3)의 경우 "beautiful", "view", "beautiful view" 등의 토큰이 생성
tfidf_matrix = tfidf_vectorizer.fit_transform(data_for_recommend['combined'])
# fit_transform 메서드를 사용하여 텍스트 데이터를 TF-IDF 행렬로 변환

# 코사인 유사도 계산
cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)


# TF_IDF 행렬을 사용하여 코사인 유사도를 계산한다. 각 제목(장소) 간의 유사도(카테고리+키워드리스트 분석)를 나타내는 행렬

# 위도 경도를 기반으로 두 지점 간의 거리를 계산하는 함수
def haversine_distance(lat1, lon1, lat2, lon2):
    radius = 6371.0  # 지구의 반지름 (단위: km)

    lat1 = math.radians(lat1)
    lon1 = math.radians(lon1)
    lat2 = math.radians(lat2)
    lon2 = math.radians(lon2)

    dlat = lat2 - lat1
    dlon = lon2 - lon1

    a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    distance = radius * c
    return distance if not math.isnan(distance) else None


# 피타고라스의 정리와 유사함 a^2+c^2=b^2

# 특정 카테고리와 키워드, 주소를 기반으로 제목(장소) 추천 및 거리 계산 실행
def recommend_distance(df, 추천카테고리=None, 추천키워드=None, 추천주소=None, top_n=3):
    # DB의 데이터프레임을 받아서 특정 조건에 맞는 제목(장소)를 추천하고 그 제목(장소)들의 거리 정보를 함께 반환한다.
    # df=Db의 데이터프레임, 추천카테고리 : Df의 '카테고리', 추천키워드 : DF의 '키워드리스트', 추천주소 : Df의 제목(장소)의 주소 top_n: 개수
    filtered_data = df.copy()
    # df를 복사하여 filtererd_data 변수에 할당시킴 추천 조건을 적용하여 필터링한 데이터를 생성하면서, 원본데이터프레임은 그대로 유지
    if 추천카테고리:
        filtered_data = filtered_data[filtered_data['카테고리'] == 추천카테고리]
        # 추천카테고리가 입력한 경우 (None이 아닌 경우), filtered_data를 해당 카테고리에 맞는 제목(장소)들로 필터링.
    if 추천키워드:
        # NaN 값을 빈 문자열로 대체한 후에 키워드를 포함하는지 확인
        filtered_data = filtered_data[filtered_data['키워드리스트'].fillna('').str.contains(추천키워드)]
        # 만약 추천키워드가 주어진 경우 (None이 아닌 경우), filtered_data를 해당 키워드가 포함된 제목들로 필터링
        # 이때 fillna('')를 사용하여 키워드가 NaN인 경우에도 필터링할 수 있도록 함.
    if 추천주소:
        # 주소에서 구나 군까지만 추출하여 필터링
        filtered_data = filtered_data[filtered_data['주소'].str.contains(추천주소)]

    recommended_data_list = []
    # 빈리스트 생성 이 리스트는 추천된 장소들의 정보를 저장하는데 쓸거임

    for index, row in filtered_data.iterrows():
        # 데이터프레임의 각 행을 하나씩 순회한다.
        lat1, lon1 = row['위도'], row['경도']
        # 현재 행의 위도,경도 값을 불러와서 lat1와 lon1에 할당시킴
        distances = []
        # 빈리스트 생성 현재 장소와 유사한 장소들의 거리정보를 저장할거임
        idx = data_recommend[data_recommend['제목'] == row['제목']].index[0]
        # data_recommend에서 현재 행의 '제목'과 일치하는 제목(장소)의 인덱스를 찾는다.
        sim_scores = list(enumerate(cosine_sim[idx]))
        # cosine_sim 행렬에서 현재 장소와 다른 모든 장소 간의 코사인 유사도를 가져와서 인덱스와 함께 리스트로 변환한다.
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
        # 코사인 유사도를 유사도값 기준으로 내림차순 정렬한다.
        place_index = [i[0] for i in sim_scores if i[0] != idx]  # 제목이 같은 장소는 제외
        # 현재 장소를 제외하고, 다른 장소들의 인덱스를 place_index에 저장
        for i in place_index[:top_n]:
            # place_index에서 상위 top_n 개 만큼의 장소 인덱스를 하나씩 순회함.
            lat2, lon2 = data_recommend.iloc[i]['위도'], data_recommend.iloc[i]['경도']
            # data_recommend에서 해당 인덱스에 해당하는 장소의 '위도'와 '경도' 값을 가져와 lat2와 lon2 변수에 할당.
            # 현재 검토 중인 다른 장소의 위치 정보를 나타낸다.
            distance = haversine_distance(lat1, lon1, lat2, lon2)
            # haversine_distance 함수를 호출하여 현재 장소와 다른 장소 간의 거리를 계산, 거리 정보를 distance 변수에 저장
            if distance != 0:
                # 현재 장소와 추천한 장소의 거리가 0이 아닌 경우에만 실행
                keywords = data_recommend.iloc[i]['키워드리스트'].split(',')[:5] if data_recommend.iloc[i]['키워드리스트'] else []
                # 현재 추천한 장소의"키워드 리스트의" 값을 가져와서 쉼표로 분리한다. 최대 5개까지의 키워드만 추출함.
                # 키워드 값이 없으면 else 써서 빈리스트로 출력되게
                distances.append((data_recommend.iloc[i]['카테고리'], data_recommend.iloc[i]['제목'],
                                  data_recommend.iloc[i]['주소'], distance, keywords))
                # 'distances' 리스트에 추천장소의 ;'제목','주소','거리', '추천키워드','카테고리'를 튜플형태로 추가한다.
                # 튜플 쓰는 이유 : 프로그램 돌아가는 도중에 리스트의 값이 변화되지 않아야하기 때문에
            else:
                pass
            # 이 경우가 아니면 pass해서 넘어가게
        distances.sort(key=lambda x: (x[2], -sim_scores[i][1]))
        # distances의 리스트를 정렬한다. 정렬기준 두가지
        # 거리값을 오름차순으로 정렬
        # 코사인유사도값의 음수값을 내림차순으로 정렬함. 코사인유사도는 음수값으로 갈수록 유사도 높은거같음 정확히모름
        recommended_data_list.append([row['카테고리'], row['제목'], distances])
        # recommended_data_list 리스트에 현재 장소의 추천 카테고리, 제목, 거리정보, 추천키워드가 포함된 distances 리스트를 추가한다.

    return recommended_data_list


def check_cdc_changes():
    with connection.cursor() as cursor:
        cursor.execute("SELECT SEQUENCEID, CATEGORY, KEYWORD, ADDRESS  FROM searchinput")
        result = cursor.fetchall()
        return result


# 주기적으로 변경 내역 확인
while True:
    # DB 데이터 쿼리를 날려 LIST로 가져온다
    new_changes = check_cdc_changes()
    old = new_changes
    # 일정 시간 동안 대기 (예: 5초)
    time.sleep(5)
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

        for item in recommended_distance[:3]:
            카테고리, 제목, 거리정보 = item
            # print(f"카테고리: {카테고리}, 추천장소: {제목}, 주소: {address}\n")
            for category, place, address, distance, keywords in 거리정보:
                keywords_str = ', '.join(keywords)
                print(
                    f"카테고리 : {category},가까운 추천 장소: {place}, \n주소: {address}, \n거리: {distance:.2f} km, \n추천키워드: {keywords_str}\n")
    else:
        print("새로운 값이 없습니다.")
