from urllib.parse import quote_plus
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

baseUrl = 'https://www.google.com/search?q='
plusUrl = input('검색어를 입력하세요: ')
url = baseUrl + quote_plus(plusUrl)

# chromedriver path input
driver = webdriver.Chrome('chromedriver')
driver.get(url)

# Explicitly wait for the search results to load (increased waiting time)
wait = WebDriverWait(driver, 30)
wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, '.rc')))

html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')

results = soup.select('.rc')  # Selecting all search results

for result in results:
    title_element = result.select_one('.LC20lb.DKV0Md')
    if title_element:
        title = title_element.text
        link = result.select_one('a')
        if link:
            print(title)
            print(link['href'])
            print()

print('done')

driver.quit()