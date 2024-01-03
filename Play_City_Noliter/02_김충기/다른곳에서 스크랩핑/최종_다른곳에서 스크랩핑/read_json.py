
import json
import pandas as pd


# JSON 파일에서 데이터 불러오기
file_path = "test_json.json"
with open(file_path, "r") as json_file:
    loaded_data = json.load(json_file)

# 불러온 JSON 데이터 사용하기
print("불러온 JSON 데이터:")
print(loaded_data)
for count in range(885):
    df = pd.DataFrame(loaded_data, index=[1])
print(df)
