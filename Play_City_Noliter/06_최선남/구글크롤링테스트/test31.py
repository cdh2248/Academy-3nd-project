from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import TimeoutException, NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import csv
import pandas as pd

# 웹 드라이버 설정 (Headless 모드 추가)
options = webdriver.ChromeOptions()
options.add_argument('--headless')  # 브라우저를 화면에 보이지 않게 설정
driver = webdriver.Chrome('chromedriver', options=options)
wait = WebDriverWait(driver, 60)  # 대기 시간을 60초로 설정

# 구글 검색 페이지 URL
base_url = 'https://www.google.com/search?q='

# 엑셀 파일 경로 입력
excel_file_path = '인천광역시_인천투어_관광지 리스트_현황(준최종).xlsx'

# 엑셀 파일에서 "제목" 열 읽어오기
excel_data = pd.read_excel(excel_file_path)
search_queries = excel_data['제목'].tolist()

data_list = []

# 검색어들에 대한 크롤링 반복
for search_query in search_queries:
    url = base_url + search_query
    driver.get(url)

    original_window = driver.current_window_handle  # 현재 창 핸들 저장

    # 페이지 수 반복 (최대 2 페이지까지)
    for page_num in range(2):
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        results = soup.select('.tF2Cxc')  # 변경된 CSS 선택자(이건 페이지마다 다름)

        for result in results:
            link = result.select_one('a')
            if link:
                link_address = link['href']
                driver.execute_script(f"window.open('{link_address}');")  # 새 창에서 링크 열기

                # 현재 열린 모든 창의 핸들 가져오기 (창 관리 개선)
                all_window_handles = driver.window_handles

                try:
                    new_window_handle = [handle for handle in all_window_handles if handle != original_window][0]
                    driver.switch_to.window(new_window_handle)
                except IndexError:
                    print("새 창을 찾을 수 없습니다.")
                    continue  # 에러 발생 시 건너뛰고 다음 반복으로 이동

                try:
                    # 페이지 로드 완료까지 대기 (Timeout 조정)
                    WebDriverWait(driver, 10).until(EC.alert_is_present())  # 경고창이 나타날 때까지 대기
                    alert = driver.switch_to.alert
                    alert.accept()  # 경고창 닫기
                except TimeoutException:
                    pass  # 경고창이 없는 경우 대기 시간 초과 예외 처리

                try:
                    # 페이지 로드 완료까지 대기 (Timeout 조정)
                    WebDriverWait(driver, 60).until(EC.visibility_of_element_located((By.CSS_SELECTOR, 'body')))

                    # 새 창의 내용을 읽어옴
                    page_html = driver.page_source
                    page_soup = BeautifulSoup(page_html, 'html.parser')
                    content = page_soup.find('body').text.strip()  # body의 내용 추출

                    data = {
                        'search_query': search_query,
                        'content': content
                    }
                    data_list.append(data)

                except TimeoutException:
                    print(f"링크 {link_address}에서 시간이 오래 걸려 건너뜁니다.")
                    continue  # 에러 발생 시 건너뛰고 다음 반복으로 이동

                except NoSuchElementException:
                    print(f"링크 {link_address}에서 내용을 찾을 수 없습니다.")
                    pass

                driver.close()  # 현재 창 닫기
                driver.switch_to.window(original_window)  # 원래 창으로 전환

        try:
            # 클릭할 다음 버튼 찾기
            next_button = driver.find_element(By.ID, 'pnnext')
        except NoSuchElementException:
            print("더 이상 다음 페이지가 없습니다.")
            break

        # 다음 버튼 클릭
        next_button.click()

        print(f"페이지 {page_num + 1}의 크롤링이 완료되었습니다.")  # 페이지별로 크롤링 완료 로그 출력

# 웹 드라이버 종료
driver.quit()

if not data_list:
    print("내용을 찾을 수 없습니다.")
else:
    # 데이터를 CSV 파일에 저장
    output_file = '구글크롤링.csv'
    with open(output_file, 'w', encoding='utf-8-sig', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([data['search_query'], data['content']])
    print(f'내용이 {output_file}에 저장되었습니다.')