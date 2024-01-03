import cx_Oracle
import time

# Oracle DB 접속 정보 설정
connection = cx_Oracle.connect("party/party@192.168.30.240:1521/xe")


# 변경 내역을 캡처하는 쿼리 실행
def check_cdc_changes():
    with connection.cursor() as cursor:
        cursor.execute("SELECT SEQUENCEID, CATEGORY, KEYWORD, ADDRESS  FROM ASDF")
        result = cursor.fetchall()
        return result


# 주기적으로 변경 내역 확인
while True:
    # DB 데이터 쿼리를 날려 LIST로 가져온다
    new_changes = check_cdc_changes()
    old = new_changes
    # 일정 시간 동안 대기 (예: 5초)
    time.sleep(3)
    new_changes = check_cdc_changes()
    if old!=new_changes:
        print("새로운 값이 들어왔습니다")
        new_db = (new_changes[len(new_changes)-1])
        print(new_db)
        # 새로운 값 처리 로직 추가
        print(type(new_db))
        for one in new_db:
            print(one)

    else:
        print("새로운 값이 없습니다.")
