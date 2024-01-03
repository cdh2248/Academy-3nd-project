import os
import pandas as pd
import folium
from folium import Marker
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
import time
from PIL import Image


# 지도 그리는 함수
def go_map(map_list):
    # 디폴트 맵 생성, 첫번째 마커 좌표 기준으로 생성
    m = folium.Map(zoom_start=11,
                   location=[map_list[0][0], map_list[0][1]],
                   width=800,
                   height=550)
    # 좌표 리스트 반복해서 마커생성
    for xin in range(len(map_list)):
        Marker(location=[map_list[xin][0], map_list[xin][1]],
               icon=folium.Icon(color='red', icon='star')).add_to(m)
    return m


def go_img():
    # HTML 파일 열기

    driver.get('file://D:\sbshim\Study_Project\project\Play_City_Noliter\스프링부트\hi.html')
    time.sleep(1)
    # 스크린샷으로 저장
    driver.save_screenshot('src/main/resources/templates/images/map_img.jpeg')
    # 브라우저 닫기
    # driver.quit()


X1 = True
# # options = webdriver.ChromeOptions()
# options.add_argument('--headless')  # 화면 표시 없이 실행
driver = webdriver.Chrome()

while X1:
    try:
        time.sleep(10)

        f = open('findTEXT.txt')
        q = f.read()

        a = q.split("\n")
        z = []
        for x in range(len(a)):
            z.append(a[x].split(" "))
        print(z)

        m1 = go_map(z)
        m1.save("hi.html")

        print('html 저장완료')

        go_img()

        print('이미지 저장완료')
        image1 = Image.open('src/main/resources/templates/images/map_img.jpeg')
        print('편집할 이미지 불러오기')
        croppedImage = image1.crop((50, 50, 750, 370))
        # croppedImage
        croppedImage.save('src/main/resources/templates/images/croppedImage.PNG')
        print('편집 이미지 저장완료')

        # z.append(a[x].split(" "))
        # print(z)

        #
        # data = pd.read_csv("hi.txt")
        # x=pd.DataFrame(data)
        # print(x)
        # print(list(x))
        # X1=False
        f.close()


    except Exception:
        print(f"{Exception} \n오류발생!")



