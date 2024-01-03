from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import TimeoutException, NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import csv
import pandas as pd

# 웹 드라이버 설정
driver = webdriver.Chrome('chromedriver')  # 드라이버 경로
wait = WebDriverWait(driver, 20)

# 구글 검색 페이지 URL
base_url = 'https://www.google.com/search?q='

# 엑셀 파일 경로 입력
excel_file_path = '관광.xlsx'

# 엑셀 파일에서 "제목" 열 읽어오기
excel_data = pd.read_excel(excel_file_path)
search_queries = excel_data['제목'].tolist()

data_list = []

# 검색어들에 대한 크롤링 반복
for search_query in search_queries:
    url = base_url + search_query
    driver.get(url)

    # 페이지 수 반복
    for page_num in range(1):
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        results = soup.select('.tF2Cxc')  # 변경된 CSS 선택자(이건 페이지마다 다름)

        for result in results:
            link = result.select_one('a')
            if link:
                link_address = link['href']
                driver.execute_script(f"window.open('{link_address}');")  # 새 창에서 링크 열기
                driver.switch_to.window(driver.window_handles[1])  # 새로 열린 창으로 전환

                try:
                    wait.until(EC.presence_of_element_located((By.XPATH, 'body')))  # body가 로드될 때까지 대기
                    page_html = driver.page_source
                    page_soup = BeautifulSoup(page_html, 'html.parser')
                    content = page_soup.find('body').text.strip()  # body의 내용 추출
                    data = {
                        'search_query': search_query,
                        'content': content
                    }
                    data_list.append(data)
                except (TimeoutException, NoSuchElementException):
                    print(f"링크 {link_address}에서 내용을 가져오지 못했습니다.")

                driver.close()  # 현재 창 닫기
                driver.switch_to.window(driver.window_handles[0])  # 원래 창으로 전환

        try:
            # 클릭할 다음 버튼 찾기
            next_button = driver.find_element(By.ID, 'pnnext')
        except NoSuchElementException:
            print("더 이상 다음 페이지가 없습니다.")
            break

        # 다음 버튼 클릭
        next_button.click()

# 웹 드라이버 종료
driver.quit()

if not data_list:
    print("내용을 찾을 수 없습니다.")
else:
    # 데이터를 CSV 파일에 저장
    output_file = 'google_search_contents_from_excel.csv'
    with open(output_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([data['search_query'], data['content']])
    print(f'내용이 {output_file}에 저장되었습니다.')