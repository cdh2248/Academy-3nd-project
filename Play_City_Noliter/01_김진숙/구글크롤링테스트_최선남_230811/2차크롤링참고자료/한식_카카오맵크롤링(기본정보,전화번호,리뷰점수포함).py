import pandas as pd
import time
from time import sleep
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
import csv

# 카카오 맵 페이지 URL
url = 'https://map.kakao.com/'

# 웹 드라이버 설정
driver = webdriver.Chrome('./chromedriver')  # 드라이버 경로
driver.get(url)

# CSS를 찾을 때까지 대기하는 함수
def time_wait(num, code):
    try:
        wait = WebDriverWait(driver, num).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, code)))
    except:
        print(code, '태그를 찾지 못하였습니다.')
        driver.quit()
    return wait

# 주차장 정보 출력 함수
def parking_list_print():
    time.sleep(0.2)

    parking_list = driver.find_elements(By.CSS_SELECTOR, '.placelist > .PlaceItem')

    for index in range(len(parking_list)):
        names = driver.find_elements(By.CSS_SELECTOR, '.head_item > .tit_name > .link_name')
        types = driver.find_elements(By.CSS_SELECTOR, '.head_item > .subcategory')
        address_list = driver.find_elements(By.CSS_SELECTOR, '.info_item > .addr')
        address = address_list[index].find_elements(By.CSS_SELECTOR, 'p')

        # 전화번호 가져오기
        phone_number = "전화번호 없음"
        try:
            phone_numbers = parking_list[index].find_elements(By.CSS_SELECTOR, '.phone')
            if phone_numbers:
                phone_number = phone_numbers[0].text.strip()
        except:
            pass
        print(phone_number)

        # 리뷰 점수 가져오기
        review_score = "리뷰 점수 없음"
        try:
            review_scores = parking_list[index].find_elements(By.CSS_SELECTOR, '.score > .num')
            if review_scores:
                review_score = review_scores[0].text.strip()
        except:
            pass
        print(review_score)

        parking_name = names[index].text
        print(parking_name)

        parking_type = types[index].text
        print(parking_type)

        addr1 = address[0].text if len(address) > 0 else ""
        print(addr1)

        addr2 = address[1].text[5:] if len(address) > 1 else ""
        print(addr2)

        dict_temp = {
            'name': parking_name,
            'parking_type': parking_type,
            'address1': addr1,
            'address2': addr2,
            'phone_number': phone_number,
            'review_score': review_score
        }

        parking_dict['주차장정보'].append(dict_temp)
        print(f'{parking_name} ...완료')

# CSS를 찾을 때까지 대기
time_wait(10, 'div.box_searchbar > input.query')

# CSV 파일 경로
csv_file = '한식top10.csv'

# CSV 파일 읽기
data = pd.read_csv(csv_file, encoding='euc-kr')

# 브랜드명 리스트
brand_names = data['브랜드명'].tolist()

# 시작시간
start = time.time()
print('[크롤링 시작...]')

# 검색 결과를 CSV 파일로 저장
output_file = '검색결과.csv'

with open(output_file, 'w', encoding='utf-8', newline='') as f:
    writer = csv.writer(f)

    # 헤더 작성
    writer.writerow(['브랜드명', '장소명', '주차장 유형', '주소1', '주소2', '전화번호', '리뷰점수'])

    # 브랜드명별로 검색
    for brand_name in brand_names:
        # 딕셔너리 생성
        parking_dict = {'주차장정보': []}

        # 검색창 찾기
        search = driver.find_element(By.CSS_SELECTOR, 'div.box_searchbar > input.query')
        search.clear()
        search.send_keys(brand_name)  # 검색어 입력
        search.send_keys(Keys.ENTER)  # 엔터버튼 누르기

        sleep(1)

        # 장소 탭 클릭
        place_tab = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, '#info\.main\.options > li.option1 > a')))
        place_tab.send_keys(Keys.ENTER)

        sleep(1)

        # 페이지 로딩 대기
        wait = WebDriverWait(driver, 30)
        wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.placelist > .PlaceItem')))

        # 주차장 리스트 크롤링
        parking_list_print()

        parking_list = driver.find_elements(By.CSS_SELECTOR, '.placelist > .PlaceItem')

        # 페이지 리스트만큼 크롤링
        page = 1  # 현재 크롤링하는 페이지가 전체에서 몇 번째 페이지인지
        page2 = 0  # 1 ~ 5번째 중 몇 번째인지
        error_cnt = 0

        while 1:
            try:
                page2 += 1
                print("**", page, "**")

                # 페이지 번호 클릭
                pagination = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, f'//*[@id="info.search.page.no{page2}"]')))
                pagination.send_keys(Keys.ENTER)

                # 페이지 로딩 대기
                wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.placelist > .PlaceItem')))

                # 주차장 리스트 크롤링
                parking_list_print()

                parking_list = driver.find_elements(By.CSS_SELECTOR, '.placelist > .PlaceItem')

                if len(parking_list) < 15:
                    break
                if not pagination.is_enabled():
                    break

                if page2 % 5 == 0:
                    # 다섯 번째 페이지까지 왔다면 다음 버튼을 누르고 page2 = 0으로 초기화
                    pagination = WebDriverWait(driver, 10).until(
                        EC.presence_of_element_located((By.XPATH, '//*[@id="info.search.page.next"]')))
                    pagination.send_keys(Keys.ENTER)
                    page2 = 0

                page += 1

            except Exception as e:
                error_cnt += 1
                print(e)
                print('ERROR!' * 3)

                if error_cnt > 5:
                    break

        # 데이터 작성
        for item in parking_dict['주차장정보']:
            writer.writerow([brand_name, item['name'], item['parking_type'], item['address1'], item['address2'], item['phone_number'], item['review_score']])

        print(f'{brand_name}의 검색 결과 수집 완료')

print('[데이터 수집 완료]\n소요 시간:', time.time() - start)
driver.quit()

print(f'데이터가 {output_file}에 저장되었습니다.')