from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from threading import Thread
from algorithm import recommend_distance, place_df
import json
import asyncio
import websockets

print(place_df)
async def handle(websocket, path):  # websocket 는 연결 객체 path는 클라이언트가 요청한 웹소켓 url경로
    print("연결성공")
    connected = set()  # 연결된 클라이언트를 추적하기 위한 집합
    connected.add(websocket)  # 클라이언트 연결을 집합에 추가

    data = await websocket.recv()

    print(data)
    추천카테고리 = None
    추천키워드 = data
    추천주소 = None
    top_n = 3
    recommended_distance = recommend_distance(place_df, 추천카테고리, 추천키워드, 추천주소, top_n)
    print("Received data from client:", data)  # 데이터 로그로 출력



    try:
        dataList = json.dumps(recommended_distance)
        for client in connected:
            try:
                await client.send(dataList)
            except websockets.exceptions.ConnectionClosed:
                print("Client 접속 해제")
    finally:
        if websocket in connected:
            connected.remove(websocket)


print("서버 구동 시작")
start_server = websockets.serve(handle, 'localhost', 8060)  # 웹소켓 서버 객체 생성 handle 는 연결될 때 실행되는 함수
asyncio.get_event_loop().run_until_complete(start_server)  # 완료 될 때 까지 함수 실행
asyncio.get_event_loop().run_forever()  # 이벤트 루프를 시작하고 무한정 실행하는데 사용
