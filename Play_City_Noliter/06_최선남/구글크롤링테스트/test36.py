import time
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
import csv
import pandas as pd

options = webdriver.ChromeOptions()
options.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
driver = webdriver.Chrome('chromedriver', options=options)
wait = WebDriverWait(driver, 30)

base_url = 'https://www.google.com/search?q='

excel_file_path = '관광.xlsx'
excel_data = pd.read_excel(excel_file_path)
search_queries = excel_data['제목'].tolist()

data_list = []

for search_query in search_queries:
    url = base_url + search_query
    driver.get(url)

    original_window = driver.current_window_handle

    for page_num in range(2):
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        results = soup.select('.tF2Cxc')

        for result in results:
            link = result.select_one('a')
            if link:
                link_address = link['href']
                driver.execute_script(f"window.open('{link_address}');")
                driver.switch_to.window(driver.window_handles[1])

                # time.sleep() 사용
                time.sleep(10)
                try:
                    page_html = driver.page_source
                    page_soup = BeautifulSoup(page_html, 'html.parser')
                    body_content = page_soup.find('body')
                    if body_content:
                        content = body_content.text.strip()
                        data = {
                            'search_query': search_query,
                            'content': content
                        }
                        data_list.append(data)
                except:
                    pass

                driver.close()
                driver.switch_to.window(original_window)

        try:
            next_button = driver.find_element(By.ID, 'pnnext')
        except NoSuchElementException:
            print("더 이상 다음 페이지가 없습니다.")
            break

        next_button.click()

driver.quit()

if not data_list:
    print("내용을 찾을 수 없습니다.")
else:
    output_file = '구글크롤링.csv'
    with open(output_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([data['search_query'], data['content']])
    print(f'내용이 {output_file}에 저장되었습니다.')