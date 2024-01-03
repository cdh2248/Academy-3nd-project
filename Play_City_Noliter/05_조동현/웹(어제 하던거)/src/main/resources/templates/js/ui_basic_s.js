$(function(){
    //$("body").hide();
    // 업종별 대메뉴 활성화(class="active") 메뉴버튼 처리
    $("nav ul li:nth-child(1) a").addClass("active");
    // 사용자가 선택한 메뉴 활성화(class="active")에 따른 페이지 메뉴명 제이쿼리 변경 처리
    $(".fixed_quick_menu dd.active a").clone().prependTo(".lnb h2");
    $(".sub_menu_list li a.active").clone().prependTo(".right_contents h1");
    // $(".right_contents h1 span").appendTo("분석");
    // 페이지 네비게이션 경로 처리
    $(".fixed_quick_menu dd.active a").clone().appendTo(".page_path");
    $(".sub_menu_list li a.active span").clone().appendTo(".page_path");
    $(".right_contents.area ul.summary_card input[type=checkbox]").attr(disabled);
    // 체크 박스 비활성화
    //$(".right_contents.area ul.summary_card input[type=checkbox]").prop("disabled",true);
    // 업체 상세정보 레이어 노출
});
// 업체 상세버튼 클릭시, 상세 정보 페이지 모달 레이어 노출
$(".place_info_layer").click(function(){
    //$("body").hide();
    $(".modal_layer_wrap").addClass("show");
    $(".layer_box a.close_layer,.layer_bg").click(function(){
        $(".modal_layer_wrap").removeClass("show");
    });
});