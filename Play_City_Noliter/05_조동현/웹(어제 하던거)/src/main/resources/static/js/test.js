const socket = new WebSocket("ws://localhost:8060");
console.log("웹소켓 대기중")
socket.onopen = () => {
    console.log("웹소켓 연결!");

const form = document.getElementById("search-form")
const submit = document.getElementById('run-button')
console.log(form)
console.log(submit)

function hello(e) {
    e.preventDefault()
    console.log(form.childNodes[1].childNodes[1].value)
    const data = form.childNodes[1].childNodes[1].value
    socket.send(data)
}

submit.addEventListener("click", hello)

};

socket.onmessage = function(event) {
    console.log("데이터 전달 완료")
    const data = JSON.parse(event.data)
    console.log(data)
    console.log(data[0][2][0])
    console.log(data[0][2][0][1])
    console.log(data[0][2][0][2])
    console.log(data[0][2][0][4])
    console.log(data[0][2][0][4][0])
    console.log(data[0][2][0][4][1])
    console.log(data[0][2][0][4][3])
    console.log(data[0][2][0][4][4])
    const ul = document.getElementsByClassName("summary_card")
    ul[0].childNodes[1].childNodes[1].innerText = data[0][2][0][1]
    ul[0].childNodes[3].childNodes[0].setAttribute("src", "")
    ul[0].childNodes[7].innerText = data[0][2][0][2]
    ul[0].childNodes[11].innerText = data[0][2][0][4][0]

    console.log(ul[0].childNodes[9])
    console.log(ul[0].childNodes[11])
    console.log(ul[0].childNodes[13])
};