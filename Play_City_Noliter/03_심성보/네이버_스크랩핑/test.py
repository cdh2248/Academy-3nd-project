list_A1=['https://blog.naver.com/amelchouchou/223040054070',
 'https://blog.naver.com/k_eunjin0207/222897080949',
 'https://blog.naver.com/anh917759/223149077267',
 'https://blog.naver.com/dkdlatmzp/223070698764',
 'https://blog.naver.com/brosense/223098407969',
 'https://blog.naver.com/leesming/222515383061',
 'https://blog.naver.com/iambest0309/222482771948',
 'https://befreepark.tistory.com/6206',
 'https://blog.naver.com/value831983/223026919827',
 'https://blog.naver.com/ms4897/222489506589']


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



#post-view222482771948 > div
driver = webdriver.Chrome()  # 드라이버 경로
tstroy_main_se='#content > div > div:nth-child(1)'
naver_main_se='#post-view223040054070 > div'

for l in list_A1:
    url = l
    print(url)
    driver.get(url)
    sleep(1)

    #     find_elements(By.CSS_SELECTOR)

    if url.find('naver') == -1:
        x = driver.find_elements(By.CSS_SELECTOR, tstroy_main_se)
        contents = [element.text for element in x]
    else:
        driver.switch_to.frame('mainFrame')
        x = driver.find_elements(By.CSS_SELECTOR, naver_main_se)
        contents = [element.text for element in x]
        driver.switch_to.parent_frame()

    sleep(1)

    print(contents)



