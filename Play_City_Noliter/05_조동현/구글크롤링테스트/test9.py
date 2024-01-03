from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import StaleElementReferenceException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import csv
import time

# 웹 드라이버 설정
driver = webdriver.Chrome('chromedriver')  # 드라이버 경로

# 구글 검색 페이지 URL
base_url = 'https://www.google.com/search?q='
search_query = input('검색어를 입력하세요: ')
url = base_url + search_query

driver.get(url)

# 검색 결과가 로드될 때까지 대기
wait = WebDriverWait(driver, 20)
try:
    wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.tF2Cxc')))  # 변경된 CSS 선택자
except TimeoutException:
    print("검색 결과가 로드되지 않았습니다.")
    driver.quit()
    exit(1)

# 링크 주소를 저장할 리스트
link_addresses = []

# 페이지 수를 기반으로 반복
for page_num in range(5):  # 5 페이지까지 크롤링
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')

    results = soup.select('.tF2Cxc')  # 변경된 CSS 선택자

    for result in results:
        link = result.select_one('a')
        if link:
            link_address = link['href']
            link_addresses.append(link_address)

    try:
        # 클릭할 다음 버튼 찾기
        next_button = driver.find_element(By.ID, 'pnnext')
    except NoSuchElementException:
        print("더 이상 다음 페이지가 없습니다.")
        break

    # 다음 버튼 클릭
    try:
        next_button.click()
    except StaleElementReferenceException:
        # 다음 버튼이 클릭되지 않을 때 대비하여 예외 처리
        print("다음 버튼을 클릭할 수 없습니다. 잠시 대기 후 다시 시도합니다.")
        time.sleep(2)
        next_button = driver.find_element(By.ID, 'pnnext')
        next_button.click()

# 웹 드라이버 종료
driver.quit()

# 각 링크 주소로 접속하여 body 내용 크롤링
data_list = []
for link_address in link_addresses:
    driver = webdriver.Chrome('chromedriver')  # 드라이버 경로
    driver.get(link_address)

    page_html = driver.page_source
    page_soup = BeautifulSoup(page_html, 'html.parser')
    content = page_soup.find('body').text.strip()  # body의 내용 추출

    data = {
        'content': content
    }
    data_list.append(data)

    driver.quit()

if not data_list:
    print("내용을 찾을 수 없습니다.")
else:
    # 데이터를 CSV 파일에 저장
    output_file = f'google_search_contents_{search_query}.csv'
    with open(output_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([search_query, data['content']])
    print(f'내용이 {output_file}에 저장되었습니다.')