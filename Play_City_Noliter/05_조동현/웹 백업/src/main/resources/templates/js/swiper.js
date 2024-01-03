$(function(){
    //https://velog.io/@yeonjin1357/JS-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-Swiper.js%EC%9D%98-%EB%AA%A8%EB%93%A0-%EA%B2%83
    const mySwiper = new Swiper('.swiper-container', {
        pagination: {
            el: '.swiper-pagination',
            type: 'progressbar' //type: 페이징 종류 (bullets, fraction, progressbar, custom)
        }
    });
});