package party.people.web.controller.category;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import party.people.domain.Client;
import party.people.domain.Place;
import party.people.repository.place.PlaceInterface;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final PlaceInterface placeInterface;

    @GetMapping("place")
    public String goPlace(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","place");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);


        return "place/place_thymeleaf";
    }

    @GetMapping("main")
    public String goCategory(Model model){
        /* side lnb 출력용 */
       // model.addAttribute("category","place");
        return "main/main";
    }

    @GetMapping("analysis")
    public String goAnalysis(HttpServletRequest request, Model model){

        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/analysis";
    }



    @GetMapping("invite")
    public String goInvite(HttpServletRequest request, Model model){

        Place place1 = placeInterface.idSearch(1L);
        Place place2 = placeInterface.idSearch(2L);
        Place place3 = placeInterface.idSearch(3L);
        List<Place> placeList = new ArrayList<>();
        placeList.add(place1);
        placeList.add(place2);
        placeList.add(place3);
//        placeList=placeInterface.findAll();



        log.info("goInvite] "+placeList);

        /* side lnb 출력용 */
        model.addAttribute("category","invite_A");
        model.addAttribute("category2",placeList);
        return "invite/invite_A";
    }

//    @GetMapping("category")
//    public String goCategory(Model model){
//
//        return "category/category";
//    }

    public static void loginCheck(HttpServletRequest request, Model model) {
        /* 세션 정보 가져오기 */
        /* create:false => 세션 정보가 있으면 기존 세션정보 로딩, 없으면 null 반환 */
        /* 세션 생성은 LoginController 참조 */
        HttpSession loginInfo = request.getSession(false);
        log.info("세션정보 " + loginInfo);
        if (loginInfo==null) {
            model.addAttribute("client", null);
        } else {
            Client loginClient = (Client) loginInfo.getAttribute("로그인");
            if (loginClient==null) {
                model.addAttribute("client", null);
            } else model.addAttribute("client", loginClient);
        }
    }

}
