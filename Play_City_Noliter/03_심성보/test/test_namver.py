from selenium import webdriver
from selenium.webdriver.common.by import By
from PIL import Image
import time
import pandas as pd
from time import sleep

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
import cx_Oracle
import urllib.request
from io import BytesIO




column_list=['index',	'f_name',	'place',	'open_date',	'close_date',	'info',	'host_or',	'PHONE_NUMBER',	'ADRESS',	'LATITUDE',	'HARDNESS',	'x',	'y',	'go']

xp1='//*[@id="_pcmap_list_scroll_container"]/ul/li[9]/div[1]/a[1]/div/div/span[1]'
xp1_2='//*[@id="_pcmap_list_scroll_container"]/ul/li[2]/div[1]/a[1]'
xp2='//*[@id="app-root"]/div/div/div/div[5]/div/div/div/div/a[2]/span'
driver = webdriver.Chrome()  # 드라이버 경로
p1 = '//*[@id="_pcmap_list_scroll_container"]/ul/li[3]/div[1]/a[1]/div/div/span'
p2 = '//*[@id="_pcmap_list_scroll_container"]/ul/li[3]/div[1]/div/div/span[2]'
p3 = '//*[@id="_pcmap_list_scroll_container"]/ul/li[3]/div[1]/div/div/span[3]'
p4 = '//*[@id="_pcmap_list_scroll_container"]/ul/li[3]/div[1]/div/div/span[4]'
p5 = '//*[@id="_pcmap_list_scroll_container"]/ul/li[3]/div[3]/div/div/div/div[1]/div/a/div/span'
testp='//*[@id="app-root"]/div/div/div/div[5]/div/div/div/div'
url1 = 'https://map.naver.com/v5/'


driver.get(url1)
sleep(3)

num='강원도 '


row_co=[]

pp2 = '#container > shrinkable-layout > div > app-base > search-input-box > div > div.search_box'

x = driver.find_element(By.CSS_SELECTOR, pp2)
y = x.find_element(By.TAG_NAME, 'input')
y.clear()
y.send_keys(num+' 맛집')
y.send_keys(Keys.ENTER)
print(num+' 맛집', ' 검색 완료')


sleep(3)
set2 = '#_pcmap_list_scroll_container > ul > li:nth-child(3) > div.CHC5F'
sleep(1)
# driver.switch_to.parent_frame
driver.switch_to.frame('searchIframe')
xx = driver.find_elements(By.CSS_SELECTOR, set2)

# 정보 저장하기
print('정보 저장 시작')
x1 = driver.find_elements(By.XPATH, p1)  # 가게 이름
x2 = driver.find_elements(By.XPATH, p2)  # 별점
x3 = driver.find_elements(By.XPATH, p3)  # 리뷰갯수
x4 = driver.find_elements(By.XPATH, p4)  # 추가태그
x5 = driver.find_elements(By.XPATH, p5)  # 리뷰

row_co.append(x1[0].text)
row_co.append(x2[0].text)
row_co.append(x3[0].text)
row_co.append(x4[0].text)
row_co.append(x5[0].text)

print(row_co)



#식당 클릭
q1 = driver.find_element(By.XPATH, xp1)
q1.click()
sleep(1)

#메뉴 클릭
print('메뉴 클릭 시도')
driver.switch_to.parent_frame()
sleep(1)
driver.switch_to.frame('entryIframe')
# q12 = driver.find_element(By.XPATH, xp2)


str1 = driver.find_element(By.XPATH, testp).text

pnum=4
print('메뉴 골라내기')
nums = len(str1.split(' '))
for num1 in range(nums):
    if str1.split(' ')[num1] == '사진':
        pnum = num1



testpp = f'//*[@id="app-root"]/div/div/div/div[5]/div/div/div/div/a[{pnum+1}]/span'
print('사진 게시판 선택 시도')
q13=driver.find_element(By.XPATH, testpp)    # 사진 게시판 선택


sleep(1)
q13.click()
print('사진 클릭성공')
sleep(3)



count=0
img_s3 = f'//*[@id="업체_{count}"]'
q13= driver.find_element(By.XPATH, img_s3)
MyImg1 = q13.get_attribute('src')
# img1 = Image.open(MyImg1)
# img_cropped.show()
req = urllib.request.Request(MyImg1, headers={"User-Agent": "Mozilla/5.0"})
res = urllib.request.urlopen(req).read()
urlopen_img = Image.open(BytesIO(res))
urlopen_img.show()

x=+1






sleep(10)







