
import requests
import os
import sys
import urllib.request
import json
import pandas as pd
import cx_Oracle


id ='PlarbZV3yEXbIyboynxp'
pw='3az9LS7plV'

# 검색할 리스트
with open('search_list.txt', 'r') as f:
    search_list = f.read()
    f.close()
search_list_s=search_list.split('\n')
#아래는 테스트 데이터
# search_list_s=['돈비어천가','동암아구해물탕','등나무가든','마포소금구이']
def update_image_from_oracle(target_url,list_name,count):
    con = cx_Oracle.connect('party/party@192.168.30.240:1521/xe')
    cur = con.cursor()
    # 데이터2 -> 테이블명 변경
    if count ==1:
        sql = "update placeinfo set 이미지주소1  = :1  WHERE 제목 =:2"
        cur.execute(sql, ( target_url, list_name))
    else:
        sql = "update placeinfo set 이미지주소2  = :1  WHERE 제목 =:2"
        cur.execute(sql, (target_url, list_name))

    con.commit()
    cur.close()
    con.close()



count=0

for go in search_list_s:

    try:
        encText = urllib.parse.quote(go)
        urlI='https://openapi.naver.com/v1/search/image?display=2&query='+ encText
        request = urllib.request.Request(urlI)
        # print(type(request))
        request.add_header("X-Naver-Client-Id",id)
        request.add_header("X-Naver-Client-Secret",pw)
        response = urllib.request.urlopen(request)
        rescode = response.getcode()

        if (rescode == 200):
            response_body = response.read()
            print()
            print('=======================')
            # print(response_body)
            result = json.loads(response_body)
            # print(result)
            img_list = result['items']

            for i, img_list in enumerate(img_list, 1):
                # 이미지링크 확인
                print(f"{img_list['link']} {i}")
                print(search_list_s[count])
                update_image_from_oracle(img_list['link'],search_list_s[count],i)

        else:
            print("Error Code:" + rescode)
        count += 1
    except:
        print()
        count+=1
        continue






