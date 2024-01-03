import json
import pandas as pd
import cx_Oracle
from konlpy.tag import Okt

#데이터베이스 저장
def update_keyward_from_oracle(target_url,list_name):
    con = cx_Oracle.connect('party/party@192.168.30.240:1521/xe')
    cur = con.cursor()
    sql = "update placeinfo set 키워드리스트  = :1  WHERE 제목 =:2"
    cur.execute(sql, ( target_url, list_name))
    con.commit()
    cur.close()
    con.close()

# 중요 !! 시작 파일 기준 = start_unit
start_unit=1; LIST_MAX=9; end_unit=4; H=100;

#파일 불러오기
with open('search_list.txt', "r") as f:
    s_list= f.read()

s_list=s_list.split('\n')
len_list=len(s_list)

#초기화
data= [None] * LIST_MAX
json_lists=[None] * LIST_MAX


for c in range(start_unit,end_unit):
    with open(f'save_json[{c}].json', "r") as json_file:
        json_lists[c]=json.load(json_file)
    keys = list(json_lists[c].keys())
    values = list(json_lists[c].values())
    data[c]=pd.DataFrame({"데이터": values},index=keys)
#메인 리스트 만들기
#=====================================
#수동 컨트롤 x=> 0~9까지 처음 돌릴때는 아래 주석 풀기
name_list_count=0; data_count=0; list_c=0;
main_data_list= [None]*len_list
for x in range(start_unit,end_unit):
    for name_list in s_list[int(x*100):]:
        data_list=[]
        for c in range(21):
            try:
                data_list.append(data[x].loc[f'{name_list}{c}'].tolist())
            except:
#                 print('브레이크')
                name_list_count+=1
                break
        main_data_list[x]=data_list
        print('100개 완료')
# ========================================
# 명사 빈도수 저장 딕셔너리
okt = Okt()
main_str = [None]*len_list;line_list = [None]*len_list
test = [None]*len_list;top_n = 50
for c in range(start_unit*100, len_list):
    keys = []
    word_dic = {}
    for x in main_data_list[c]:
        for y in x:
            main_str[c] += y
    line_list[c]=main_str[c].split(' ')
    # 텍스트 내의 명사 추출 및 빈도 계산
    for line in line_list[c]:
        malist = okt.pos(line, norm=True, stem=True)
        for word in malist:
            if word[1] == "Noun" and len(word[0]) >= 2:  # 두 글자 이상인 명사만 고려
                if word[0] not in word_dic:
                    word_dic[word[0]] = 0
                word_dic[word[0]] += 1
    # 가장 많이 사용된 명사 상위 50개만 추출
    keys = sorted(word_dic.items(), key=lambda x: x[1], reverse=True)[:top_n]
    # 결과 출력
    for word, count in keys:
        test[c] += f"{word}/{count},"
    print(test[c])
    update_keyward_from_oracle(test[c], s_list[c])
# ===========================================








