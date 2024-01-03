import cx_Oracle
import pandas as pd
import urllib.request
import json
import requests
import os
import sys

#데이터 불러오기
#데이터 불러오기
def select_from_oracle():
    con = cx_Oracle.connect('party/party@192.168.30.240:1521/xe')
    cur = con.cursor()

    sql_select = '''
            select 제목 from PLACEINFO
            ORDER BY ID ASC
             '''

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


data = select_from_oracle()
print(data)







id ='PlarbZV3yEXbIyboynxp'
pw='3az9LS7plV'

# 네이버 블로그 검색 API 최초 1회 실행
blog_lists = []  # 데이터를 저장할 리스트 생성
count = 0
for list_name in data:
    encText = urllib.parse.quote(str(list_name))
    print(list_name)
    url = "https://openapi.naver.com/v1/search/blog?display=20&query=" + encText  # JSON 결과
    # url = "https://openapi.naver.com/v1/search/blog.xml?query=" + encText # XML 결과
    request = urllib.request.Request(url)
    request.add_header("X-Naver-Client-Id", id)
    request.add_header("X-Naver-Client-Secret", pw)
    response = urllib.request.urlopen(request)
    rescode = response.getcode()
    if (rescode == 200):
        response_body = response.read()
        print(response_body.decode('utf-8'))
        blog_lists.append(response_body.decode('utf-8'))
        # blog_lists2[f'{list_name}'] = response_body.decode('utf-8')

    else:
        print("Error Code:" + rescode)

data_low=pd.DataFrame(blog_lists)
data_low.to_csv('row_data.csv', encoding='utf8')