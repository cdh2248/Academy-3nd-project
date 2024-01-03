$(function(){
    //$("body").hide();
    // 업종별 대메뉴 활성화(class="active") 메뉴버튼 처리
    $("nav ul li:nth-child(1) a").addClass("active");
    // 사용자가 선택한 메뉴 활성화(class="active")에 따른 페이지 메뉴명 제이쿼리 변경 처리
    $(".fixed_quick_menu dd.active a").clone().prependTo(".lnb h2");
    $(".fixed_quick_menu dd.active a").clone().prependTo(".left_contents_center .page_title > h1");
    $(".sub_menu_list li a.active").clone().prependTo(".right_contents h1");
    // $(".right_contents h1 span").appendTo("분석");
    // 페이지 네비게이션 경로 처리
    $(".fixed_quick_menu dd.active a").clone().appendTo(".page_path");
    $(".sub_menu_list li a.active span").clone().appendTo(".page_path");
    $(".place .page_path > span").prepend("인천 ");
    // 체크 박스 비활성화
    //$(".right_contents.area ul.summary_card input[type=checkbox]").prop("disabled",true);
    // 업체 상세정보 레이어 노출
});
// 업체 상세버튼 클릭시, 상세 정보 페이지 모달 레이어 노출


$(".place_info_layer.item_1").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_1").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_1").remove();
    });
});







$(".place_info_layer.item_2").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_2").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_2").remove();
    });
});

$(".place_info_layer.item_3").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_3").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_3").remove();
    });
});

// 모임카드 스킨 라디오 버튼 선택(checked)

// INVITE > 모임카드 미리보기 버튼 클릭시 최종 모임카드 디자인 적용된 레이어 노출하기
$(".ai_card.design .confirm").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.card").addClass("show");
    // 카드 디자인 스킨 복사
    // 모임카드 미리보기 버튼 클릭시, 인바이트 사용자가 작성한 최종 내용 카드 디자인 스킨 입혀서 보여주기
    //$(".right_contents.area .ai_card_skin input[type=radio]:checked").clone().appendTo(".right_contents .page_title h1");
    //$(".right_contents.area .ai_card_skin input[type=radio]:checked").parent().parent().parent().clone().appendTo(".modal_layer_wrap.card .layer_contents");
    $(".right_contents.area .card_list_wrap input[type=radio]:checked").parent().next("dl").clone().appendTo(".modal_layer_wrap.card .layer_contents");
    $(".left_contents_center .ai_card.design").clone().appendTo(".modal_layer_wrap.card .layer_contents .ai_card_skin dd.body");
    // $(".modal_layer_wrap.card .ai_card_skin").prepend("<dt></dt>");
    // $(".modal_layer_wrap.card .layer_contents .ai_card.design .card_list > label > input[type=checkbox]:unchecked").parent().remove();
    $(".modal_layer_wrap.card .card_map_wrap").insertAfter($(".modal_layer_wrap.card .ai_card.design dd.body"));
    //$(".modal_layer_wrap.card .layer_contents .ai_card.design .card_list > label > input[type=radio]:checked").parent().remove();
    $(".modal_layer_wrap.card .layer_contents .ai_card.design dd.footer").remove();
    //$(".modal_layer_wrap.card .ai_card_skin > dd.body > input[type=radio]").remove();
    $(".modal_layer_wrap.card .form_info .skin").remove();
    $(".modal_layer_wrap.card .ai_card.design .summary_card li.subject > input[type=checkbox]").not(".modal_layer_wrap.card .ai_card.design .summary_card li.subject > input[type=checkbox]:checked").parent().parent().parent().hide();
    //$(".modal_layer_wrap.card .ai_card_skin > dd.body > label").remove();
    $(".layer_bg").click(function(){
        $(".modal_layer_wrap.card").removeClass("show")
        $(".modal_layer_wrap.card .layer_contents .ai_card_skin").remove();
    });
});
// var skinChecked = $(".ai_card_skin input[type=radio]:checked").val();
// skinChecked.clone().appendTo(".modal_layer_wrap.card .layer_contents");


$(".place_info_layer.item_11").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_11").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_11").remove();
    });
});

$(".place_info_layer.item_12").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_12").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_12").remove();
    });
});




$(".place_info_layer.item_13").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_13").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_13").remove();
    });
});



$(".place_info_layer.item_21").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_21").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_21").remove();
    });
});

$(".place_info_layer.item_22").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_22").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_22").remove();
    });
});






$(".place_info_layer.item_23").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_23").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_23").remove();
    });
});




$(".place_info_layer.item_33").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_33").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_33").remove();
    });
});


$(".place_info_layer.item_32").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_32").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_32").remove();
    });
});


$(".place_info_layer.item_31").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_31").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_31").remove();
    });
});


$(".place_info_layer.item_41").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_41").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_41").remove();
    });
});


$(".place_info_layer.item_42").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_42").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_42").remove();
    });
});


$(".place_info_layer.item_43").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_43").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_43").remove();
    });
});


$(".place_info_layer.item_53").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_53").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_53").remove();
    });
});

$(".place_info_layer.item_52").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_52").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_52").remove();
    });
});

$(".place_info_layer.item_51").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_51").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_51").remove();
    });
});


$(".place_info_layer.item_61").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_61").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_61").remove();
    });
});

$(".place_info_layer.item_62").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_62").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_62").remove();
    });
});

$(".place_info_layer.item_63").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap.detail").addClass("show");
    $(".layer_contents.item_63").clone().appendTo(".modal_layer_wrap.detail .layer_box");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap.detail").removeClass("show");
        $(".modal_layer_wrap.detail .layer_contents.item_63").remove();
    });
});