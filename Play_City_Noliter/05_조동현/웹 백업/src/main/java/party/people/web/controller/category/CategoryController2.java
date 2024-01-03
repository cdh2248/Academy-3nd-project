package party.people.web.controller.category;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.InviteCard;
import party.people.domain.Place;
import party.people.repository.InviteCard.InviteCardInterface;
import party.people.repository.place.PlaceInterface;
import java.util.Random;

import static party.people.web.controller.category.CategoryController.loginCheck;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryController2 {

    private final PlaceInterface placeInterface;
    private final InviteCardInterface inviteCardInterface;




//    @GetMapping("invite")
    public String goInvite(HttpServletRequest request, Model model){

        Random random = new Random();

        /* 로그인 세션 유지용 메서드 */
        /* 메서드 이름 컨트롤 클릭으로 내용 확인 가능 */
        loginCheck(request,model);

        /* side lnb 출력용 */
        model.addAttribute("category","invite");

        int randomNumber1 = random.nextInt(1,10);

        int randomNumber2 = random.nextInt(1,800);

        int randomNumber3 = random.nextInt(1, 800);


        List<Place> placeList2 = new ArrayList<>();
        Place place12 = placeInterface.idSearch(randomNumber1);
        Place place22 = placeInterface.idSearch(randomNumber2);
        Place place32 = placeInterface.idSearch(randomNumber3);
        placeList2.add(place12);
        placeList2.add(place22);
        placeList2.add(place32);

        place12.keyWord5();

        List<Place> pl = new ArrayList<>();


        HttpSession test = request.getSession(false);
        if (test!=null) {


            List<List<Place>> place2 = (List<List<Place>>) test.getAttribute("검색결과");

            pl= place2.get(0);

        }
        else{
            log.info("NULL !!!!");
        }


        if (test!=null) {
            model.addAttribute("category2", pl);
            model.addAttribute("keyList",pl.get(0).keyWordTitle());
        }else{

            model.addAttribute("category2", placeList2);
        }
        System.out.println(pl);


        return "invite/invite_A";
    }



//    @GetMapping("/saveCard")
    public String resultPage(HttpServletRequest request, @ModelAttribute("card") InviteCard inviteCard) {

        inviteCardInterface.saveCard(inviteCard);


//        model.addAttribute("data", data);



        return "";
    }



}
