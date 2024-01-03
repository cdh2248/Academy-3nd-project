from selenium import webdriver
from selenium.webdriver.common.by import By
from PIL import Image
import time
import pandas as pd
from time import sleep
import json
import re

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
import cx_Oracle
import urllib.request
from io import BytesIO
data=pd.read_csv('row_data.csv')
data.drop('Unnamed: 0',axis=1,inplace=True)
t=data['0'].values.tolist()
driver = webdriver.Chrome()  # 드라이버 경로
se2='#post-view222482771948 > div'
tstroy_main_se='#content > div > div:nth-child(1)'

all_data = {}
count = 0


# 특수문자 제거 정규식
def str_c(a):
    sstr = str(a).replace("'", '')
    sstr = sstr.replace('\\n', ' ')
    sstr = sstr.replace("  ", ' ')
    sstr = re.sub(r'[!@#$%^\[\]&*(),.?":{}|<>/]', ' ', sstr)
    sstr = sstr.replace("  ", ' ')
    sstr = re.sub(r'[^가-힣\s]', ' ', sstr)
    sstr = sstr.replace("  ", ' ')
    sstr = sstr.replace("  ", ' ')

    return sstr

with open('search_list.txt', 'r') as f:
    search_list = f.read()
    f.close()
search_list_s=search_list.split('\n')
print(search_list_s)

for counting in range(9):
    counting_ck=counting*100
    # if counting_ck > 783:
        # counting_ck=782
    print(f'{counting_ck}\n\n\n\n\n\n')
    all_data={}
    for name_list in t[0+int(counting_ck):100+int(counting_ck)]:
        count2 = 0
        names = json.loads(name_list)

        for name in names['items']:
            print(f"{name['link']}")
            url = name['link']
            driver.get(url)
            sleep(1)

            #     find_elements(By.CSS_SELECTOR)

            if url.find('naver') == -1:
                x = driver.find_elements(By.CSS_SELECTOR, tstroy_main_se)
                contents = [element.text for element in x]
            else:
                test_url = url.split('/')
                naver_main_se = f'#post-view{test_url[4]} > div'
                driver.switch_to.frame('mainFrame')
                x = driver.find_elements(By.CSS_SELECTOR, naver_main_se)
                contents = [element.text for element in x]
                driver.switch_to.parent_frame()

            sleep(1)
            st = str_c(contents)
            try:
              # contents=f'{search_list_s[count]}{count2}'
              print(f'{count}')
            # st = str_c(contents)
              all_data[f'{search_list_s[count]}{count2}'] = contents
            except:
                print('종료')
                continue

            print(contents)
            count2 += 1
        count += 1
    with open(f'save_json[{counting}].json', 'w') as f:
        json.dump(all_data, f)
        print('저장완료')



# 제이슨 형식으로 저장
# with open('test_json.json', 'w') as f:
#     json.dump(all_data,f)
    # f.write(str(all_data))

# df=pd.DataFrame(all_data)

# df.to_csv("test")
