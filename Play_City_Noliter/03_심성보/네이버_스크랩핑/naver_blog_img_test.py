
import requests
import os
import sys
import urllib.request
import json
import pandas as pd


id ='PlarbZV3yEXbIyboynxp'

pw='3az9LS7plV'
string2 = '2023 고양호수예술축제'

encText = urllib.parse.quote(string2)




def key_lists():
    print('이곳에 리스트를 받는 코드를 작성')

def db_select():
    print('이곳에 데이터베이스를 불러오는 코드')

url = "https://openapi.naver.com/v1/search/blog?query=" + encText # JSON 결과
# url = "https://openapi.naver.com/v1/search/blog.xml?query=" + encText # XML 결과
request = urllib.request.Request(url)
request.add_header("X-Naver-Client-Id",id)
request.add_header("X-Naver-Client-Secret",pw)
response = urllib.request.urlopen(request)
rescode = response.getcode()
if(rescode==200):
    response_body = response.read()
    print(response_body.decode('utf-8'))
else:
    print("Error Code:" + rescode)

def df_make(raw_json):
    all_data=[]
    column_list=['postdate','bloggername','description','link','title', 'bloggerlink']

    for record in raw_json['items']:
        row_data=[]
        for column_data in column_list:
            row_data.append(record.get(column_data))
        all_data.append(row_data)

    return column_list, all_data








# 리스트를 받아서 이미지를 검색하는 코드
for go in list_go:

    try:
        encText = urllib.parse.quote(go)

        url = "https://openapi.naver.com/v1/search/blog?query=" + encText # JSON 결과
        urlI='https://openapi.naver.com/v1/search/image?query='+ encText
        # url = "https://openapi.naver.com/v1/search/blog.xml?query=" + encText # XML 결과
        request = urllib.request.Request(urlI)
        print(type(request))
        request.add_header("X-Naver-Client-Id",id)
        request.add_header("X-Naver-Client-Secret",pw)
        response = urllib.request.urlopen(request)
        rescode = response.getcode()
        # print(raw_json)

        # 이미지 저장 경로
        savePath = "D:\\sbshim\\PROJECT\\img2\\"

        if (rescode == 200):
            response_body = response.read()
            print()
            print('=======================')
            # print(response_body)
            result = json.loads(response_body)
            print(result)
            img_list = result['items']

            for i, img_list in enumerate(img_list, 1):
                # 이미지링크 확인
                print(img_list['link'])

                # 저장 파일명 및 경로
                FileName = f"{savePath}{go}_{i}.jpg"
                # os.path.join(savePath, savePath + go+"_"str(i) + '.jpg')

                # 파일명 출력
                print('full name : {}'.format(FileName))

                # 이미지 다운로드 URL 요청

                # urllib.request.urlretrieve(img_list['link'], FileName)

            # 다운로드 완료 시 출력
            print("--------download succeeded--------")

        else:
            print("Error Code:" + rescode)
    except:
        continue





