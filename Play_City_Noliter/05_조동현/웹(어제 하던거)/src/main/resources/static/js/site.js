$(function(){
    //sitemap => gnb로 복사
    $("#sitemap > ul").clone().insertAfter("#sitemap").wrap("<div id='gnb'></div>");
    $("<i></i>").insertAfter("#header .link_area");
    //gnb의 3depth있을 경우 2depth에 아이콘 삽입
    $(".depth3").siblings("a").append("<i class='icon_down'></i>");
    //gnb 마우스 hover시 2depth 노출
    $("#gnb .depth1 > .menu").mouseover(function(){
        $("#gnb .depth2").show(); 
        $("#gnb > ul").addClass("on");
        $("#header").addClass("on");
    });
    //gnb 마우스 leave시 2depth 숨김
    $("#gnb").mouseleave(function(){
        $("#gnb .depth2").hide();
        $("#gnb > ul").removeClass("on");
        $("#header").removeClass("on");
    });
    //sitemap 노출,비노출
    $("#header .btn_bar").click(function(){
        $("#sitemap").show()                            
    })
    $("#sitemap .btn_close").click(function(){
        $("#sitemap").hide()                            
    })
    //sitemap 2depth 아이콘 삽입
    $("#sitemap .depth2 > li > a").append("<i class='icon_arrow'></i>");
    //path삽입
    $("#sitemap .depth1.active > .menu , #sitemap .depth1.active .depth2 > .active > a , #sitemap .depth1.active .depth2 > .active .depth3 .active > a").clone().appendTo("#path");
    //lnb 타이틀 삽입
    $("#sitemap .depth1.active > .menu").clone().appendTo("#lnb .depth1")
    //lnb nav 삽입
    $("#sitemap .depth1.active .depth2").clone().insertAfter("#lnb .depth1").wrap('<nav></nav>');
    $('#lnb nav').addClass('scroll');
    //lnb 3depth 숨김
    $("#lnb .icon_down").click(function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        $(this).parent().siblings(".depth3").toggle();
        $(this).toggleClass("icon_up")
    })
    //페이지 타이틀 삽입
    var lnbtit = $("#path a:last()").text()
		$(".page_tit_area .page_tit").html($(".page_tit_area .page_tit").text() +  lnbtit);
     //lnb 접기
    $("#lnb .lnb_btn").click(function(){
        $("#lnb").toggleClass("off");      
        $(this).toggleClass("open");
        $("#container").toggleClass("full");
    })
    //th 클릭시
    $("thead th").click(function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        $(this).toggleClass('sort down');
        if($(this).siblings('th').hasClass('sort')){
           $(this).siblings('th').removeClass('sort');
        }
    })
   //리스트 선택시
    $(".list_table tbody td").click(function(){
        $(this).parent("tr").addClass("active");
        if($(this).parent("tr").siblings('tr').hasClass('active')){
           $(this).parent("tr").siblings('tr').removeClass('active');
        }
    })
    //날짜 선택
    $( ".datepicker" ).datepicker({
        language: 'ko',
        //showOn:"button",
        showMonthAfterYear: true ,
        dateFormat:'yy-mm-dd',
        nextText:'다음 달',
        prevText:'이전 달',
        yearSuffix : '년',
        dayNames : ['일', '월', '화', '수', '목', '금', '토'],       
        dayNamesMin:['일','월','화','수','목','금','토'],
        dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],      
        monthNamesShort:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
        monthNames : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],      
    });
    //section별 테이블영역 높이값
    $(".col2.layout .list_area .scroll.sticky-table").each(function(){
        var pagingHeight = $(this).siblings(".paging").outerHeight(true)+25 || 20 ;
        var topHeight = $(this).siblings(".top_area").outerHeight(true)-4 || 0 ;
        var searchHeight = $(this).siblings(".search").outerHeight(true) || 0 ;
        var tabHeight = $(this).siblings(".tab").outerHeight(true) || 0 ;
        var minusHeight = Math.ceil(pagingHeight + topHeight + searchHeight + tabHeight + 20);
        var minusHeight2 = Math.ceil(pagingHeight + topHeight + searchHeight + tabHeight + 70);
        $(this).css({ 'height': 'calc(100% - ' + minusHeight+ 'px)' })
        $(this).css({ 'max-height': 'calc(100% - ' + minusHeight2 + 'px)' })
    })//2분할 list영역
    $(".top_col2.layout .list_area .scroll.sticky-table").each(function(){
        var pagingHeight = $(this).siblings(".paging").outerHeight(true)+25 || 20 ;
        var topHeight = $(this).siblings(".top_area").outerHeight(true)-4 || 0 ;
        var searchHeight = $(this).siblings(".search").outerHeight(true) || 0 ;
        var minusHeight = Math.ceil(pagingHeight + topHeight + searchHeight + 20);
        $(this).css({ 'height': 'calc(100% - ' + minusHeight+ 'px)' })
    })//3분할 list영역 
    $(".top_col2.layout .detail_area > .scroll.sticky-table").each(function(){
        var topHeight = $(this).siblings(".top_area").outerHeight(true) || 30 ;
        var pagingHeight = $(this).siblings(".paging").outerHeight(true) || 0;
        var minusHeight = Math.ceil(topHeight + pagingHeight + 40);
        $(this).css({ 'height': 'calc(100% - ' + minusHeight+ 'px)' })
    })//3분할 detail영역
    var tabContentHeight = 
        $('.top_col2 .detail_area .memo_area').outerHeight(true)+
        $('.top_col2 .detail_area .tab').outerHeight(true)+25;
    $('.top_col2 .detail_area .tab_content').css({ 'height': 'calc(100% - ' + tabContentHeight + 'px)' 
    });// 3분할=>하단(탭컨텐츠 영역)
    $(".tab_content > div > .paging").siblings(".scroll.sticky-table").each(function(){
        var pagingHeight = $(this).siblings(".paging").outerHeight(true)+25 || 20 ;
        var topHeight = $(this).parents(".view_wrap").outerHeight(true) || 0 ;
        var minusHeight = Math.ceil(pagingHeight + topHeight + 20);
        //$(this).css({ 'max-height': 'calc(34vh - ' + minusHeight+ 'px)' })
    })
    // 3분할=>하단(탭컨텐츠 영역) 내 테이블
    //var tabContentDetailHeight = 
        //$('.tab_content .paging').outerHeight(true) +10;
        //$('.top_col2 .detail_area .tab_content .paging').siblings('.scroll.sticky-table').css({ 'height': 'calc(100% - ' + tabContentDetailHeight + 'px)' });// 3분할=>하단(탭컨텐츠 영역) 내 테이블
        
    var rightRow2ListHeight=  
        $('.layout.right_row2 .list_area .top_area').outerHeight(true) + 
        $(".layout.right_row2 .list_area .paging").outerHeight(true)+
        50;
    $('.layout.right_row2 .list_area .scroll.sticky-table').css({ 'height': 'calc(100% - ' + rightRow2ListHeight + 'px)' });// 3분할=>우측하단(list영역) 테이블 높이
    
    //탭
    $('.tab a, .tab label').click(function(ev){
        ev.preventDefault();
        ev.stopPropagation();
      var idx = $(this).index();
      $(this).siblings('.tab a, .tab label').removeClass('active');// - active 클래스명
      $(this).addClass('active');
      $(this).parent(".tab").siblings('.tab_content').children("div").removeClass('active');
      $(this).parent(".tab").siblings('.tab_content').children("div").eq(idx).addClass('active');
  }) 
    //팝업닫음
    $(".popup .btn_close").click(function(){
        $(".popup").hide();
    })
    //팝업 이동
    $(".popup.move .box").draggable();

});
 //window 팝업 가운데
function winOpen(mypage, myname, w, h, scroll) {
    var winl = (screen.width - w) / 2;
    var wint = (screen.height - h) / 2;
    winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
    win = window.open(mypage, myname, winprops)
    if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
    }
//input 숫자 ,찍기
function inputNumberFormat(obj) {
     obj.value = comma(uncomma(obj.value));
 }

 function comma(str) {
     str = String(str);
     return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
 }

 function uncomma(str) {
     str = String(str);
     return str.replace(/[^\d]+/g, '');
 }