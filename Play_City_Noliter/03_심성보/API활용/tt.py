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

url ='https://apis.data.go.kr/6280000/ICRoadVolStat/NodeLink_Trfc_DD'
# access_key = 'ltTwFaSJxSB%2BPC9OpKUj2t%2B9T%2FU5L7cNnjM58GgDJ4MEaEBwrNl0maKqedVcVnm6WwNWh6obsHxaXG7VusspWg%3D%3D'
# access_key = 'ltTwFaSJxSB%2BPC9OpKUj2t%2B9T%2FU5L7cNnjM58GgDJ4MEaEBwrNl0maKqedVcVnm6WwNWh6obsHxaXG7VusspWg%3D%3D'
access_key ='HVgh0GJtfha0bssIG8/oBb7dkuTWWgOnt3o47r4Wa1/SrD6VRDqJ0cOzT/6T4vL3KX4JV0bKzNZl9WqYpOdLJg=='
access_key ='Hye01rA7F4WaD2E/TyNWbUDZQfxwlwgUTrFhECRZ/RwEGkV39JQjj6PXhc19mJdTfgVF44SmRWl6VPnVu3cdpQ=='
# access_key = 'Hye01rA7F4WaD2E%2FTyNWbUDZQfxwlwgUTrFhECRZ%2FRwEGkV39JQjj6PXhc19mJdTfgVF44SmRWl6VPnVu3cdpQ%3D%3D'


pageNo=1
numOfRows=10
YMD=20230816
def get_request_url(url):
    params = {'serviceKey': access_key , 'numOfRows' : 1, 'pageNo' : 1,
        'YMD':YMD
    }
    response = requests.get(url, params=params)
    # response.content # => response.content는 한글이 인코딩된 형식이므로
    #                       response.text 를 응답받기로함
    return response.text





def json_to_df_info(raw_json):
    all_data = []
    column_list = ["statDate","roadName","linkID","nx","ny","obsrValue"]

    for record in raw_json['response']['body']['items']['item']:
        # 이하 블럭을 자동화 해보세요.
        row_data = []
        for column_data in column_list:
            row_data.append(record.get(column_data))
        all_data.append(row_data)

    return column_list, all_data





raw_str_json = get_request_url(url)
#
# if raw_str_json:
#     raw_json = json.loads(raw_str_json)

print(raw_str_json)
# print(raw_json)



