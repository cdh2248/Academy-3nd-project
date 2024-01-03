from urllib.parse import quote_plus
from bs4 import BeautifulSoup
from selenium import webdriver

baseUrl = 'https://www.google.com/search?q='
plusUrl = input('검색어를 입력하세요: ')
url = baseUrl + quote_plus(plusUrl)

# chromedriver path input
driver = webdriver.Chrome('chromedriver')
driver.get(url)
driver.implicitly_wait(10)

html = driver.page_source
soup = BeautifulSoup(html)

v = soup.select('.yuRUbf')

for i in v:
    print(i.select_one('.LC20lb.DKV0Md').text)
    print(i.a.attrs['href'])
    print()

print('done')

driver.close()