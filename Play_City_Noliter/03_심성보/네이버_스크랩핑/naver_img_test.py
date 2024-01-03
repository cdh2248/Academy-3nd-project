import urllib.request
import json
import pandas as pd
import os
import cx_Oracle
from io import BytesIO


def select_from_oracle2():
    con = cx_Oracle.connect('party/party@192.168.30.206:1521/xe')
    cur = con.cursor()

    sql_select = '''
            select * from 데이터
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


def df_make(raw_json):
    all_data=[]
    column_list=['postdate','bloggername','description','link','title', 'bloggerlink']

    for record in raw_json['items']:
        row_data=[]
        for column_data in column_list:
            row_data.append(record.get(column_data))
        all_data.append(row_data)

    return column_list, all_data


column_list=['index',	'f_name',	'place',	'open_date',	'close_date',	'info',	'host_or',	'PHONE_NUMBER',	'ADRESS',	'LATITUDE',	'HARDNESS',	'x',	'y',	'go']
data=select_from_oracle2()
df=pd.DataFrame(data, columns=column_list)
df=df.sort_values('index')
df = df['f_name']
print(df)
list_go=list(df)


url = 'https://openapi.naver.com/v1/datalab/search'
url_b='https://openapi.naver.com/v1/search/blog'
url_b1='https://openapi.naver.com/v1/search/blog.json'

id ='PlarbZV3yEXbIyboynxp'

pw='3az9LS7plV'

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
                # 이 부분에 데이터베이스에 url을 저장하는 코드 생성
                # urllib.request.urlretrieve(img_list['link'], FileName)

            # 다운로드 완료 시 출력
            print("--------download succeeded--------")

        else:
            print("Error Code:" + rescode)
    except:
        continue
# column_list, all_data = df_make(raw_json)

# df = pd.DataFrame(all_data, columns=column_list)

# print(df)
