import json
import pandas as pd
from konlpy.tag import Okt
import codecs
import cx_Oracle
import re
from konlpy.tag import Okt


#파일 불러오기
with open('search_list.txt', "r") as f:
    s_list= f.read()
s_list=s_list.split('\n')
json_lists=[]
data=[]
for c in range(8):
    with open(f'save_json[{c}].json', "r") as json_file:
        json_lists.append(json.load(json_file))
    keys = list(json_lists[c].keys())
    values = list(json_lists[c].values())
    data.append(pd.DataFrame({"데이터": values},index=keys))

pattern = r'\d+'

#메인 리스트 만들기
#=====================================
# name_list_count=0
# data_count=0
# # main_data_list=[]
# list_c=0
# x=8
# for name_list in s_list[int(x*100):]:
#     data_list=[]
#     for c in range(21):
#
#         try:
#             data_list.append(data[x].loc[f'{name_list}{c}'].tolist())
#         except:
#             print('브레이크')
#             name_list_count+=1
#             break
#     main_data_list.append(data_list)
#=====================================
#
# ========================================
# name_list_count=0
# data_count=0
# # main_data_list=[]
# list_c=0
# x=8
# for name_list in s_list[int(x*100):]:
#     data_list=[]
#     for c in range(21):
#
#         try:
#             data_list.append(data[x].loc[f'{name_list}{c}'].tolist())
#         except:
#             print('브레이크')
#             name_list_count+=1
#             break
#     main_data_list.append(data_list)
# ===========================================




okt = Okt()




