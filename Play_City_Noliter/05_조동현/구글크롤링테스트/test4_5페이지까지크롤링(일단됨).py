from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common import TimeoutException
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


# Function to save link addresses to a CSV file
def save_links_to_csv(link_addresses, output_file):
    with open(output_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['Link Address'])
        for link_address in link_addresses:
            writer.writerow([link_address])


# Explicitly wait for the search results to load
wait = WebDriverWait(driver, 20)
try:
    wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.tF2Cxc')))  # 변경된 CSS 선택자
except TimeoutException:
    print("검색 결과가 로드되지 않았습니다.")
    driver.quit()
    exit(1)

link_addresses = []

# Crawl links from multiple pages
for page_num in range(5):  # 크롤링할 페이지 수 (0부터 시작)
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')

    results = soup.select('.tF2Cxc')  # 변경된 CSS 선택자

    for result in results:
        link = result.select_one('a')
        if link:
            link_address = link['href']
            link_addresses.append(link_address)

    # 클릭할 다음 버튼 찾기
    next_button = driver.find_element(By.ID, 'pnnext')

    # 다음 버튼이 비활성화되었으면 반복문 종료
    if 'disabled' in next_button.get_attribute('class'):
        break

    # 다음 버튼 클릭
    next_button.click()

# 웹 드라이버 종료
driver.quit()

if not link_addresses:
    print("링크 주소를 찾을 수 없습니다.")
else:
    # Save link addresses to a CSV file
    output_file = 'google_search_links.csv'
    save_links_to_csv(link_addresses, output_file)
    print(f'링크 주소가 {output_file}에 저장되었습니다.')