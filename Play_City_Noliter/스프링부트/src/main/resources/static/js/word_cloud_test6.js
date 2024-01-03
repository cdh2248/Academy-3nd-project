// 참고 사이트 : https://docs.anychart.com/Basic_Charts/Tag_Cloud
anychart.onDocumentReady(function () {
    var category2 = [[${category2}]]
    console.log(JSON.stringify(category2));
    var keywordData = [];

    if(category2.keyword){
        var keyword = category2.keyword.split(',');
        console.log("불러오니?"+category2.keyword);
        for (var i = 0; i < keyword.length; i++) {
            var wordAndValue = keyword[i].split('/');
            console.log("너어디살아"+wordAndValue);
            var word = wordAndValue[0];
            var value = parseInt(wordAndValue[1]);
            keywordData.push({x: word, value: value})
        }
    }

        var data = [];

        //     data.push({x: keywordData[i].x, value: keywordData[i].x, link: keywordData[i].x});
        for (var i = 0; i < keywordData.length; i++) {
            data.push({x: keywordData[i].x, value: keywordData[i].value, link: keywordData[i].x});
        }
            // {x: "맛집", value: 56, link: "관광";,
            // {x: "lists", value: 44, category: "전시"},
            // {x: "meaning", value: 40, category: "전시"},
            // {x: "useful", value: 36, category: "자연"},
            // {x: "different", value: 32, category: "레저"},
            // {x: "grammar", value: 28, category: "쇼핑"},
            // {x: "teaching", value: 24, category: "음식"},
            // {x: "example", value: 20, category: "숙박"},
            // {x: "thing", value: 12, category: "숙박"},
            // {x: "example", value: 20, category: "숙박"},
            // {x: "thing", value: 12, category: "숙박"}

        console.log("확인" + category2)
        console.log("확인" + keywordData)

        var chart = anychart.tagCloud(data); // create a chart and set the data : 차트 생성

        chart.angles([0]); // configure angles, 태그 클라우드의 단어 배치 각도를 설정. [0]은 하나의 각도만 사용하도록 설정
        // chart.fromAngle(10);
        // chart.toAngle(100);
        // chart.anglesCount(7);
        // chart.angles([0, 30, 90]); // 각도 지정 (0,30,90도 안에서만 나타남)

        chart.textSpacing(5); // set text spacing
        chart.background().fill("transparent"); // 백그라운드 컬러 설정

        // var customColorScale = anychart.scales.linearColor();  // 색상 스케일 생성 및 구성
        // //var customColorScale = anychart.scales.ordinalColor();
        // customColorScale.colors(["#fff","#fff","#47c947"]);
        // chart.colorScale(customColorScale);
        // 색상 범위 추가 및 구성
        //chart.colorRange().enabled(true);
        //chart.colorRange().length("90%");

        chart.mode("spiral");  // set the mode of the tag cloud : spiral, rect
        //chart.legend(true);   // enable the legend
        // configure tooltips
        chart.tooltip().format("{%yPercentOfTotal}% ({%value})\n\n{%category}");

        chart.listen("pointClick", function (e) { // 이벤트 리스너 추가, 페이지 링크 연결
            var url = "//en.wiktionary.org/wiki/" + e.point.get("x");
            window.open(url, "_self");
        });
        chart.animation(true);
        chart.container("word_cloud_1");  // set the container id, 차트를 그릴 HTML 요소의 ID를 지정합니다.
        chart.draw(); // initiate drawing the chart, 위의 설정들을 기반으로 차트를 그립니다.
    });

    // configure the visual settings of the chart
    // chart.normal().fill("#1fadad");
    // chart.hovered().fill("#93bfec");
    // chart.selected().fill("#1f66ad");
    // chart.normal().stroke("#0f5757");
    // chart.hovered().stroke("#0f3357");
    // chart.selected().stroke("#0f3357");
    //chart.normal().fontWeight(600);
    //로그 스케일 생성 후 차트의 값 스케일로 설정, 로그 스케일 활용 (Logarithmic Scale):
    //요소들의 빈도가 큰 범위를 다루는 경우, 가장 적게 나타나는 요소들의 폰트 크기는 너무 작아질 수 있습니다. 이러한 상황을 방지하기 위해 로그 스케일을 활용할 수 있습니다.
    //tagCloud.scale(anychart.scales.log());
