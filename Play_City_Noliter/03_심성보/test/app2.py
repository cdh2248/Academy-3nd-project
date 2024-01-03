from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from threading import Thread
from algorithm import recommend_distance, data_recommend

app = Flask(__name__)
socketio = SocketIO(app)

@app.route('/')
def index():
    return render_template('index2.html')

@socketio.on('run_recommendation')  # 클라이언트에서 'run_recommendation' 이벤트를 받았을 때 실행
def run_recommendation(data):
    추천카테고리 = data.get('category', None)
    추천키워드 = data.get('keywords', None)
    추천주소 = data.get('address', None)
    top_n = 3
    recommended_distance = recommend_distance(data_recommend, 추천카테고리, 추천키워드, 추천주소, top_n)
    print("Received data from client:", data)  # 데이터 로그로 출력
    emit('recommendation_result', {'result': recommended_distance})

if __name__ == '__main__':
    socketio.run(app, host='192.168.30.240', port=5000, debug=True)