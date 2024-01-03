# 목적: 열 이름 재정의, 열순서 재정의
import requests
import json
from datetime import datetime, timedelta
import pandas as pd
import re
import cx_Oracle

# access_key ='bwc3DJ6DBSdRpgK4ZBeKvvb42M0KALQDAzhEhe8AkUP9nwc9VHoO5r6OxJfBrnuNK76bfwk2hAmqEJWaYL+4ag=='
# bwc3DJ6DBSdRpgK4ZBeKvvb42M0KALQDAzhEhe8AkUP9nwc9VHoO5r6OxJfBrnuNK76bfwk2hAmqEJWaYL+4ag==
# bwc3DJ6DBSdRpgK4ZBeKvvb42M0KALQDAzhEhe8AkUP9nwc9VHoO5r6OxJfBrnuNK76bfwk2hAmqEJWaYL%2B4ag%3D%3D
# PFjaHUlboyrhA1UndyRyjsDiJY5rcxa6skKwAtJUtKsZ7qrwv6QgEaiqHdOxRN8awqJGatQsssCkjt%2B2cRxgaw%3D%3D
# PFjaHUlboyrhA1UndyRyjsDiJY5rcxa6skKwAtJUtKsZ7qrwv6QgEaiqHdOxRN8awqJGatQsssCkjt+2cRxgaw==
access_key ='HVgh0GJtfha0bssIG8/oBb7dkuTWWgOnt3o47r4Wa1/SrD6VRDqJ0cOzT/6T4vL3KX4JV0bKzNZl9WqYpOdLJg=='

def search2(word,x):
#     p="\D+["+word+"]\D+"  # 실패한 흔적
    a=str(x)
    rs=a.find(word)
    if rs == -1:
        return False
    else:
        return True



def get_request_url2(x_coodinate, y_coodinate): # 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'
    url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'
    raw_json = []
    c=0
    for x,y in zip(x_coodinate,y_coodinate):
        print(x, y , "dd")
        params = {'serviceKey': access_key ,      # 인증키
                  'numOfRows' : 1000,
                  'pageNo' : 1,
                  'dataType': 'JSON',
                  'base_date':yyyymmdd,          # 년월일
                  'base_time':hhmm,              # 시간 분
                  'nx':x,              # 구로동 좌표 x
                  'ny':y               # 구로동 좌표 y
        }
        response = requests.get(url, params=params) # url 과 요청 데이터를 보내서 주소 반환
    # response.content # => response.content는 한글이 인코딩된 형식이므로
    #                       response.text 를 응답받기로함
        print(response)

        if response:  # 반환받은 값이 있는 경우 True
            if search2("response",response.text[:10]):
                print("리스폰 텍스트 =", response.text)
                print(c)
                c+=1
                raw_json.append(json.loads(response.text))  # 제이슨형식의 문자열을 파이썬의 딕트 타입으로 변환

    return raw_json


def get_update_time_info():
    now = datetime.now()  # 2023-07-10 13:25:30.597281 //현재 local date와 time을 리턴
    num = [2, 5, 8, 11, 14, 17, 20, 23]
    num2 = [0,3,6,9,12,15,18,21]
    num3 = [1,4,7,10,13,16,19,22]
    for number in range(len(num)) :

        if now.hour == num[number]:
            print(f"현재 시간대 정보로 업데이트 합니다.: {now}")
            return now # 현재 시간대 리턴
        elif now.hour == num2[number]:
            update_cycle_min = timedelta(hours=1) # timedelta 를 사용하게 날짜끼리 계산하여 음수가 되더라도 -1day 이런식으로 표현됨
            lastest_time = now - update_cycle_min # now는 현재 시간대 else로 왔다는건 40분이거나 40분 이전 이라는 뜻 update_cycle_min 는 40분 이므로 예시
            # 2023-07-10 13:25:30.597281 - 2023-07-10 13:40:30.597281 연산이 됨 그럼 2023-07-10 12:45:30.597281 이 되므로 12시 데이터를 출력
            print(f"이전 시간대 정보로 업데이트 합니다.: {lastest_time}")
            return lastest_time # 이전 시간대 리턴
        elif now.hour == num3[number]:
            update_cycle_min = timedelta(hours=2)  # timedelta 를 사용하게 날짜끼리 계산하여 음수가 되더라도 -1day 이런식으로 표현됨
            lastest_time = now - update_cycle_min  # now는 현재 시간대 else로 왔다는건 40분이거나 40분 이전 이라는 뜻 update_cycle_min 는 40분 이므로 예시
            # 2023-07-10 13:25:30.597281 - 2023-07-10 13:40:30.597281 연산이 됨 그럼 2023-07-10 12:45:30.597281 이 되므로 12시 데이터를 출력
            print(f"이전 시간대 정보로 업데이트 합니다.: {lastest_time}")
            return lastest_time  # 이전 시간대 리턴

# {'baseDate': '20230717', 'baseTime': '1400', 'category': 'TMP', 'fcstDate': '20230717', 'fcstTime': '1500', 'fcstValue': '27', 'nx': 58, 'ny': 125}
def json_to_df_info(raw_jsons):
    all_data = []
    column_list = ["baseDate","baseTime","category","fcstDate","fcstTime","fcstValue","nx","ny"]
    for raw_json in raw_jsons :
        for record in raw_json['response']['body']['items']['item']:
            row_data = []

            if record['category'] == 'POP' or record['category'] == 'PCP' or record['category'] =='TMP' :

                for column_data in column_list:
                    row_data.append(record.get(column_data))
                all_data.append(row_data)
    return column_list, all_data

def category_df(df):
    df['fcstValue'] = df['fcstValue'].replace('강수없음', '0')
    df['fcstValue'] = df['fcstValue'].astype(str)
    df['fcstValue'] = df['fcstValue'].apply(lambda x: re.sub(r'[^\d.]', '', x))
    df['fcstValue'] = pd.to_numeric(df['fcstValue'])
    df = df.drop_duplicates(subset=['baseDate', 'baseTime', 'fcstDate', 'fcstTime', 'category', 'nx', 'ny'])
    pivoted_df = df.pivot(index=['baseDate', 'baseTime', 'fcstDate', 'fcstTime', 'nx', 'ny'],
                          columns='category',
                          values='fcstValue').reset_index()

    # Rename the pivoted columns if desired
    pivoted_df.rename(columns={'TMP': '기온', 'POP': '강수확률', 'PCP': '강수량'},
                      inplace=True)

    return pivoted_df

def day_df(categorys):
    all_tables = []

    for category in categorys : # PCP 강수량, POP 강수확률, TMP 기온
        tables = {}
        row_date = []
        for fcstDate, group_df in categorys[f'{category}'].groupby('fcstDate'):

            tables[fcstDate] = group_df.reset_index(drop=True)

            row_date.append(tables)

        all_tables.append(row_date)
    return all_tables


def preprocessed_df_to_oracle(df):
    con = cx_Oracle.connect('project_thejoeun/1541@192.168.30.240:1521/xe')
    cur = con.cursor()


    for i in range(0, len(df)):
        sql_insert = '''
            insert into rains(baseDate, baseTime, categorys,  fcstDate, fcstTime,  fcstValue,  nx,   ny, 강수량 , 강수확률, 기온) 
            values(:baseDate, :baseTime, :categorys, :fcstDate, :fcstTime, :fcstValue, :nx, :ny, :PCP, :POP,:TMP)
            '''

        baseDate = df.iloc[i]['baseDate']
        baseTime = df.iloc[i]['baseTime']
        categorys = df.iloc[i]['category']
        fcstDate = df.iloc[i]['fcstDate']
        fcstTime = df.iloc[i]['fcstTime']
        fcstValue = int(df.iloc[i]['fcstValue'])
        nx = float(df.iloc[i]['nx'])
        ny = float(df.iloc[i]['ny'])
        PCP = int(df.iloc[i]['강수량'])
        POP = int(df.iloc[i]['강수확률'])
        TMP = int(df.iloc[i]['기온'])
        cur.execute(sql_insert,
                 (baseDate, baseTime, categorys, fcstDate, fcstTime, fcstValue, nx, ny, PCP, POP, TMP)
                 )

    con.commit()
    cur.close()
    con.close()


# 구로동 좌표
x_coodinate = [37]
y_coodinate = [126]

# 업데이트는 40~60 사이에 이루어짐
request_time = get_update_time_info() # 시간대 리턴 함수
print('1')
mmdd = request_time.strftime("%m%d")

yyyymmdd = request_time.strftime("%Y%m%d")  # request_time() 2023-07-10 13:55:31.534921  현재 날짜와 시간대 반환 strftime("%Y%m%d") 를 사용하여 년 월 일 반환
                                            # strftime() string format time"의 약자로, 날짜와 시간을 원하는 형식으로 변환하는 함
hhmm = request_time.strftime("%H%M")        # 현재 날짜와 시간대에서 시간 하고 분만 반환
print('2')
# raw_json = get_request_url(x_coodinate, y_coodinate) # 데이터를 요청하여 제이슨형식을 텍스트로 받는 함수
print('3')
# column_list, all_data = json_to_df_info(raw_json) # 반환받은 딕트 타입을 키 는 키 끼리 밸류는 밸류끼리 리스트로 묶어서 column_list는 키 리스트 all_data 에는 밸류 리스트로 저장
# df = pd.DataFrame(all_data, columns=column_list) # 컬럼은 키로 밸류는 값으로 프레임 생성
print('4')
# categorys = category_df(df) # 카테고리 내용을 컬럼으로
# print(categorys)
print('5')
# days = day_df(categorys)
# preprocessed_df_to_oracle(df)
