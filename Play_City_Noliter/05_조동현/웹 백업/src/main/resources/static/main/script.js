document.addEventListener("DOMContentLoaded", function () {
    const text = "이것은 한글 텍스트입니다. 한글로 작성된 단어들이 워드클라우드로 표현됩니다. 한글은 아름다운 언어입니다.";

    const options = {
        list: text.split(" ").map(word => ({ text: word, weight: 10 + Math.random() * 90 })),
        gridSize: 10,
        fontFamily: "Nanum Gothic", // 한글 폰트 사용 예시
        color: "random-dark",
        backgroundColor: "white"
    };

    WordCloud(document.getElementById("wordCloud"), options);
});
