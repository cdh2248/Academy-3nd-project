package party.people.web.controller.place;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.*;
import party.people.repository.InviteCard.InviteCardInterface;
import party.people.repository.place.PlaceInterface;
import party.people.repository.search.SearchInputInterface;

import static party.people.web.controller.category.CategoryController.loginCheck;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class placeSaveController {
    private final PlaceInterface placeInterface;
    private final SearchInputInterface searchInputInterface;
    private final InviteCardInterface inviteCardInterface;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PostMapping("saveCard")
    public String inviteCard(@ModelAttribute("inviteCard")InviteCard inviteCard, HttpServletRequest request, Model model){

        /* 로그인 해제 방지 */
        loginCheck(request, model);

        log.info("객체 오니? " + inviteCard);


        /* 저장할 때 쓸 객체 생성 */
        InviteCard letInviteCard = new InviteCard();

        /* 저장 정보 제목 */
        if(inviteCard.getTitle()!=null && !inviteCard.getTitle().equals(",")){
            /* 왠지 모르지만 저장 정보가 ,찍히고 2개씩 와서.. 이렇게 처리한다 */
            letInviteCard.setTitle(inviteCard.getTitle().split(",")[0]);}
        else letInviteCard.setTitle("");

        log.info("placeSaveController] letInviteCard.getTITLE() = " + letInviteCard.getTitle());
        log.info("위에는 렛"+inviteCard.getTitle());

        /* 모임 내용 */
        if(inviteCard.getMeetingContent() !=null && !inviteCard.getMeetingContent().equals(",")){
         letInviteCard.setMeetingContent(inviteCard.getMeetingContent().split(",")[0]);}
        else letInviteCard.setMeetingContent("");

        /* 참가 인원 */
        if((inviteCard.getMeetingParticipants()!=null && !inviteCard.getMeetingParticipants().equals(","))){
           letInviteCard.setMeetingParticipants(inviteCard.getMeetingParticipants().split(",")[0]);}
        else letInviteCard.setMeetingParticipants("");
        /* 약속 날짜 */
        if((inviteCard.getTargetDate()!=null && !inviteCard.getTargetDate().equals(","))){
            letInviteCard.setTargetDate(inviteCard.getTargetDate().split(",")[0]);}
        else letInviteCard.setTargetDate("");
        /* 약속 시간 */
        if((inviteCard.getTargetTime()!=null && !inviteCard.getTargetDate().equals(","))){
          letInviteCard.setTargetTime(inviteCard.getTargetTime().split(",")[0]);}
        else letInviteCard.setTargetTime("");

        /* 카드스킨 */
        if(inviteCard.getCardSkin()!=null && !inviteCard.getCardSkin().equals(",")){
            letInviteCard.setCardSkin(inviteCard.getCardSkin().split(",")[0]);}
        else letInviteCard.setCardSkin("");

        //클라이언트 아이디 저장
        HttpSession loginId = request.getSession(false);
        Client login = (Client) loginId.getAttribute("로그인");
        letInviteCard.setClientId(login.getClientId());
        if (inviteCard.getPlaceId1()!=null){
        letInviteCard.setPlaceId1(inviteCard.getPlaceId1());}
        if(inviteCard.getPlaceId2()!=null){
        letInviteCard.setPlaceId2(inviteCard.getPlaceId2());}
        if(inviteCard.getPlaceId3()!=null){
        letInviteCard.setPlaceId3(inviteCard.getPlaceId3());}





//        log.info("placeSaveController] datetype = " + dateOn);

        log.info("받아온 녀석 제목 = " + letInviteCard.getTitle());
        log.info("받아온 녀석 객체 일번아이디 = " + letInviteCard.getPlaceId1());

        log.info("받아온 녀석의 아이디 = " + letInviteCard.getClientId());

//        inviteCard.setCLIENT_ID("tester");
        if (letInviteCard.getClientId()!=null)
            log.info("저장전 최종 객체 확인"+letInviteCard);
        inviteCardInterface.saveCard(letInviteCard);

//        return "";
    return "redirect:/client/myCard";

    }




//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @PostMapping("saveCard")
////    public String inviteCard(@ModelAttribute("inviteCard")InviteCard inviteCard,@RequestParam("datetype") String datetype){
//
//    public String inviteCard(@RequestParam("MEETING_PARTICIPANTS")String MEETING_PARTICIPANTS,@@RequestParam(""),@RequestParam(""),@RequestParam(""),
//                             @RequestParam("datetype") String datetype ){
//        System.out.println("저장중");
//        System.out.println(inviteCard.getPLACE_ID_1());
//
//        log.info("placeSaveController] datetype = " + datetype);
//
//        log.info("placeSaveController] inviteCard = " + inviteCard.getTITLE());
//        log.info("placeSaveController] inviteCard = " + inviteCard.getPLACE_ID_1());
//
//        log.info("placeSaveController] inviteCard = " + inviteCard.getCLIENT_ID());
//
//        inviteCard.setCLIENT_ID("tester");
//        inviteCardInterface.saveCard(inviteCard);
//
//
//        return "redirect:/invite/invite_A";
//    }
//    @GetMapping("saveCard")
//    public String inviteGet(){
//        return "invite/invite_A";
//    }




}
