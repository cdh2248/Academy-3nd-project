# 목적: 열 이름 재정의, 열순서 재정의
import requests
import json
from datetime import datetime, timedelta

import pandas as pd
import cx_Oracle
from flask import Flask, request
from flask_restful import Resource, Api
from flask import Response
import asyncio
import websockets
import aioconsole




access_key ='HVgh0GJtfha0bssIG8/oBb7dkuTWWgOnt3o47r4Wa1/SrD6VRDqJ0cOzT/6T4vL3KX4JV0bKzNZl9WqYpOdLJg=='

import threading
import time


app = Flask(__name__)
app.debug = True
api = Api(app)

class CreateUser(Resource):
    def get(self):
        return {'status': 'success user','hi':'hihi'}

class CreateUser2(Resource):
    def get(self):
        return {'status': 'success user2'}

class Multi(Resource):
    def get(self,num):
        return {'result':num*10}

class Plus(Resource):
    def get(self,num):
        return {'result':num}



x_coodinate = "55"
y_coodinate = "125"
url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'
# 업데이트는 40~60 사이에 이루어짐
def get_request_url(url):
    params = {'serviceKey': access_key , 'numOfRows' : 10, 'pageNo' : 1,
        'dataType': 'JSON','base_date':yyyymmdd,'base_time':hhmm,
        'nx':x_coodinate, 'ny':y_coodinate
    }
    response = requests.get(url, params=params)
    # response.content # => response.content는 한글이 인코딩된 형식이므로
    #                       response.text 를 응답받기로함
    return response.text
def get_update_time_info():
    now = datetime.now()

    if now.minute >= 40 and now.minute < 60:
        print(f"현재 시간대 정보로 업데이트 합니다.: {now}")
        return now
    else:
        update_cycle_min = timedelta(minutes=40)
        lastest_time = now - update_cycle_min
        print(f"이전 시간대 정보로 업데이트 합니다.: {lastest_time}")
        return lastest_time

def json_to_df_info(raw_json):
    all_data = []
    column_list = ["baseDate","baseTime","category","nx","ny","obsrValue"]

    for record in raw_json['response']['body']['items']['item']:
        # 이하 블럭을 자동화 해보세요.
        row_data = []
        for column_data in column_list:
            row_data.append(record.get(column_data))
        all_data.append(row_data)

    return column_list, all_data

def preprocess_df(df):

    df.insert(0,'date_time', df['baseDate']+df['baseTime'])

    p_df = pd.pivot_table(df, index='date_time', columns=['category'],values='obsrValue')
    nx = df.loc[0,'nx']
    ny = df.loc[0,'ny']
    date_time = df.loc[0,'baseDate'] + ' ' + df.loc[0,'baseTime']
    p_df.insert(0,'date_time',[date_time])
    p_df.insert(1,'nx',[nx])
    p_df.insert(2,'ny',[ny])

    p_df.rename(columns={
        'date_time':'DATE_TIME', 'nx':'NX', 'ny': 'NY', 'PTY':'강수형태',
        'REH':'습도','RN1':'시간1_강수량','T1H':'기온',
        'UUU':'동서바람성분','VEC':'풍향','VVV':'남북바람성분','WSD':'풍속'
       }, inplace=True)

    redefined_columns = ['DATE_TIME','NX', 'NY', '기온', '시간1_강수량', '강수형태',
                         '습도', '풍속', '풍향', '동서바람성분', '남북바람성분']
    p_df = p_df[redefined_columns]
    return p_df

def preprocessed_df_to_oracle(df):
    con = cx_Oracle.connect('project_thejoeun/1541@localhost:1521/xe')
    cur = con.cursor()
    # sql_insert = '''
    #         insert into weather(DATE_TIME, NX, NY, 시간1_강수량, 강수형태, 기온, 습도, 풍향,풍속,동서바람성분,남북바람성분)
    #         values(:DATE_TIME, :NX, :NY, :시간1_강수량, :강수형태, :기온, :습도, :풍향,:풍속,:동서바람성분,:남북바람성분)
    #         '''
    sql_insert = '''
            insert into weather(DATE_TIME, NX, NY, 시간1_강수량, 강수형태, 기온, 습도, 풍향,풍속,동서바람성분,남북바람성분) 
            values(:DATE_TIME, :NX, :NY, :시간1_강수량, :강수형태, :기온, :습도, :풍향,:풍속,:동서바람성분,:남북바람성분)
            '''
    DATE_TIME = df.iloc[0]['DATE_TIME']
    NX = int(df.iloc[0]['NX'])  # int 값에 대해서는 int 형으로 변환해줘야 한다.
    NY = int(df.iloc[0]['NY'])
    시간1_강수량 = df.iloc[0]['시간1_강수량'] # 현재 데이터 프레임의 행인덱스가 date_time이므로 loc가 안된다.
    강수형태 = df.iloc[0]['강수형태']
    기온 = df.iloc[0]['기온']
    습도 = df.iloc[0]['습도']
    풍향 = df.iloc[0]['풍향']
    풍속 = df.iloc[0]['풍속']
    동서바람성분 = df.iloc[0]['동서바람성분']
    남북바람성분 = df.iloc[0]['남북바람성분']

    cur.execute(sql_insert,
                (   DATE_TIME, NX, NY, 시간1_강수량, 강수형태, 기온,
                    습도, 풍향, 풍속, 동서바람성분, 남북바람성분 )
                )



    con.commit()
    cur.close()
    con.close()






def select_from_oracle():
    con = cx_Oracle.connect('project_thejoeun/1541@localhost:1521/xe')
    cur = con.cursor()
    # sql_insert = '''
    #         insert into weather(DATE_TIME, NX, NY, 시간1_강수량, 강수형태, 기온, 습도, 풍향,풍속,동서바람성분,남북바람성분)
    #         values(:DATE_TIME, :NX, :NY, :시간1_강수량, :강수형태, :기온, :습도, :풍향,:풍속,:동서바람성분,:남북바람성분)
    #         '''
    sql_select = '''
            select * from weather
             '''
    # DATE_TIME = df.iloc[0]['DATE_TIME']
    # NX = int(df.iloc[0]['NX'])  # int 값에 대해서는 int 형으로 변환해줘야 한다.
    # NY = int(df.iloc[0]['NY'])
    # 시간1_강수량 = df.iloc[0]['시간1_강수량'] # 현재 데이터 프레임의 행인덱스가 date_time이므로 loc가 안된다.
    # 강수형태 = df.iloc[0]['강수형태']
    # 기온 = df.iloc[0]['기온']
    # 습도 = df.iloc[0]['습도']
    # 풍향 = df.iloc[0]['풍향']
    # 풍속 = df.iloc[0]['풍속']
    # 동서바람성분 = df.iloc[0]['동서바람성분']
    # 남북바람성분 = df.iloc[0]['남북바람성분']
    #

    rs = cur.execute(sql_select)

    data=[]
    for record in rs:
        row=[]
        for source in range(0, len(record)):
            row.append(record[source])
        data.append(row)




    con.commit()
    cur.close()
    con.close()
    return data





def rain_info_get():
    request_time = get_update_time_info()
    yyyymmdd = request_time.strftime("%Y%m%d")
    hhmm = request_time.strftime("%H%M")

    # 구로동 좌표


    raw_str_json = get_request_url(url)

    if raw_str_json:
        raw_json = json.loads(raw_str_json)



    column_list, all_data = json_to_df_info(raw_json)

    df = pd.DataFrame(all_data, columns=column_list)

    # df.to_json(.json')

    with open(f'rain_data_{hhmm}.json', 'w', encoding='utf8') as outfile:
        readable_result = json.dumps( raw_str_json, indent=4, sort_keys=True, ensure_ascii=False)
        outfile.write(readable_result)
        print('json file saved')


    df_preprocessed = preprocess_df (df)

    preprocessed_df_to_oracle(df_preprocessed)









request_time = get_update_time_info()
yyyymmdd = request_time.strftime("%Y%m%d")
hhmm = request_time.strftime("%H%M")


def scheduler_a():
    print('\n')
    # while True:
    for i in range(1000000):

        rain_info_get()
        time.sleep(10)  # 수집주기 1초 /
        print("\t\t\t\t\tAAA 데이터 데이터 수집:%s" % i)










def print_main_menu():
    print('\n1. 부평구 실시간 초단기 날씨 데이터 구축')
    print('2. CCC 실시간 데이터 구축[미구축]')
    print('3. CCC 실시간 데이터 구축[미구축]')
    print('4. 스케줄러 종료')
    print('* 엔터: 메뉴 업데이트\n')

# app.run(host='192.168.30.240')
    # app.run(host='localhost')


#     5000 포트가 기본 >> http://192.168.30.199:5000/user
api.add_resource(CreateUser, '/user')
api.add_resource(CreateUser2, '/user2')



def playapp():
    # app.run(host='192.168.30.240')
    pass




def main_program():
    while True:
        print_main_menu()
        print('아래행에 메뉴입력: ')
        selection = input()
        if selection == '':  continue
        else:                menu_num = int(selection)

        if(menu_num == 1):
            t = threading.Thread(target=scheduler_a, daemon=True)
            t.start()

        elif(menu_num == 2):
            # t = threading.Thread(target=scheduler_b, daemon=True)
            # t.start()
            pass
        elif(menu_num == 3):
            print('오라클 셀렉ㅌ,')
            data = select_from_oracle()

            print(data)
            pass
        elif(menu_num == 4):
            break
        elif (menu_num == 0):
            continue







if __name__ == '__main__':
    # app.run(host='localhost')
    Response(main_program())
    t = threading.Thread(target=playapp())
    t.start()














