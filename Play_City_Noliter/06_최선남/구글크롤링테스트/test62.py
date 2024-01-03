from bs4 import BeautifulSoup
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
import csv
import requests
import time
import pandas as pd

# Google Custom Search API 설정
CX = 'f3d1510f2ba7c43af'
API_KEY = 'AIzaSyBrb_GNxcT5thFJS46soBtyV1VPk4XpJ60'

# 엑셀 파일에서 "제목" 열 읽어오기
excel_file_path = '인천광역시_인천투어_관광지 리스트_현황(준최종).xlsx.'  # 엑셀 파일 경로 설정
excel_data = pd.read_excel(excel_file_path)
search_queries = excel_data['제목'].tolist()

# Google Custom Search API로 검색하여 링크 주소 가져오기
def get_links(query, num_results=10):
    service = build("customsearch", "v1", developerKey=API_KEY)
    result = service.cse().recommend_list(q=query, cx=CX, num=num_results).execute()
    if 'items' in result:
        links = [item['link'] for item in result['items']]
        return links
    return []

# CSV 파일에 데이터를 저장하는 함수
def save_data_to_csv(data_list, output_file):
    with open(output_file, 'w', encoding='utf-8-sig', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['검색어', '내용'])
        for data in data_list:
            writer.writerow([data['search_query'], data['content']])

data_list = []

# 쿼터 제한을 고려하여 검색 결과를 분할하여 처리
for search_query in search_queries:
    print(f"검색어: {search_query}")
    total_results = get_links(search_query)
    batch_size = 5  # 한 번에 처리할 검색 결과 수
    num_batches = (len(total_results) + batch_size - 1) // batch_size

    for batch_num in range(num_batches):
        start_idx = batch_num * batch_size
        end_idx = min((batch_num + 1) * batch_size, len(total_results))
        batch_results = total_results[start_idx:end_idx]

        for link in batch_results:
            try:
                response = requests.get(link, timeout=10)  # 설정한 시간 내에 응답이 없으면 건너뜀
                response.raise_for_status()
                soup = BeautifulSoup(response.content, 'html.parser')

                # body 태그가 없는 경우를 처리
                body = soup.find('body')
                if body:
                    content = body.get_text().strip()  # body의 내용 추출
                    data = {
                        'search_query': search_query,
                        'content': content
                    }
                    data_list.append(data)
                    print(f"검색어: {search_query}, 수집 완료")
                else:
                    print(f"링크 {link}에서 body 태그가 없어서 건너뜁니다.")
            except requests.exceptions.Timeout:
                print(f"링크 {link}에서 응답 시간 초과로 인해 건너뜁니다.")
            except requests.exceptions.RequestException as e:
                print(f"링크 {link}에서 내용을 가져오지 못했습니다: {e}")

            # API 요청 간격을 두고 처리 (쿼터 제한 고려)
            time.sleep(1)  # 1초 간격으로 처리

# 데이터를 CSV 파일에 저장
if data_list:
    output_file = '찢어죽이고_사지를분해시킬_구글크롤링.csv'
    save_data_to_csv(data_list, output_file)
    print(f'내용이 {output_file}에 저장되었습니다.')
else:
    print("내용을 찾을 수 없습니다.")