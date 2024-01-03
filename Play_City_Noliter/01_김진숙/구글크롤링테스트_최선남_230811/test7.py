from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import csv

# 웹 드라이버 설정
driver = webdriver.Chrome('chromedriver')  # 드라이버 경로

# 구글 검색 페이지 URL
base_url = 'https://www.google.com/search?q='
search_query = input('검색어를 입력하세요: ')
url = base_url + search_query

driver.get(url)


# CSV 파일에 데이터를 저장하는 함수
def save_data_to_csv(data_list, output_file):
    with open(output_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([search_query, data['content']])


# 검색 결과가 로드될 때까지 대기
wait = WebDriverWait(driver, 20)
try:
    wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.tF2Cxc')))  # 변경된 CSS 선택자
except TimeoutException:
    print("검색 결과가 로드되지 않았습니다.")
    driver.quit()
    exit(1)

data_list = []
visited_links = set()

# 페이지 수를 기반으로 반복
for page_num in range(5):  # 5 페이지까지 크롤링
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')

    results = soup.select('.tF2Cxc')  # 변경된 CSS 선택자

    for result in results:
        link = result.select_one('a')
        if link:
            link_address = link['href']
            if link_address not in visited_links:
                visited_links.add(link_address)
                driver.get(link_address)  # 링크로 이동

                # 페이지 내용 가져오기
                page_html = driver.page_source
                page_soup = BeautifulSoup(page_html, 'html.parser')
                content = page_soup.find('body').text.strip()  # body의 내용 추출

                data = {
                    'content': content
                }
                data_list.append(data)

    # 다음 페이지 번호 클릭
    try:
        next_page = driver.find_element(By.CSS_SELECTOR, '#pnnext')
        next_page.click()
    except NoSuchElementException:
        print("더 이상 다음 페이지가 없습니다.")
        break

# 웹 드라이버 종료
driver.quit()

if not data_list:
    print("내용을 찾을 수 없습니다.")
else:
    # 데이터를 CSV 파일에 저장
    output_file = f'google_search_contents_{search_query}.csv'
    save_data_to_csv(data_list, output_file)
    print(f'내용이 {output_file}에 저장되었습니다.')