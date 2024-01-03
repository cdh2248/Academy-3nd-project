package party.people.web.controller.category;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jdk.dynalink.beans.StaticClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import party.people.domain.Client;
import party.people.domain.InviteCard;
import party.people.domain.Place;
import party.people.domain.SearchResult;
import party.people.repository.InviteCard.InviteCardInterface;
import party.people.repository.place.PlaceInterface;
import party.people.repository.search.SearchInputInterface;

import static party.people.web.controller.category.CategoryController2_CK.placeImageToBinary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final PlaceInterface placeInterface;
    private final SearchInputInterface searchInputInterface;
    private final InviteCardInterface inviteCardInterface;

    @GetMapping("place")
    public String goPlace(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","place");

        /* Default 검색 */
        model.addAttribute("searchText", "전체");
        
        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        List<SearchResult>allResult = searchInputInterface.loadAll();
        Random random = new Random();
        int randomValue = random.nextInt(allResult.size());

        SearchResult start = allResult.get(randomValue);

        /* 출력 형식이 이중 리스트로 [[PLACE, PLACE, PLACE], [PLACE, PLACE, PLACE], ... ] */
        /* 리스트에 PLACE 3개의 객체가 하나의 리스트로 들어가 있는 형식이어야 함  */
        /* 그래서 DB inputResult 테이블도 내부 3개는 ,로 리스트와 리스트사이는 / 로 구분지어둠 */
        List<List<Place>> finalForm = new ArrayList<>();
        /* 첫 리스트 쪼개기 /로 3개로 구성되어있는 리스트로 만들기 */
        /* ['가,나,다','1,2,3',...,'A,B,C'] */
        if (start.getResult()!=null) {
            List<String> three = Arrays.stream(start.getResult().split("/")).toList();
            /* 위의 리스트를 또 ,단위로 쪼개야 한다 */
            /* 여기서 Strong one의 첫번째 값은 위의 예시 '가,나,다'를 예시로 */

            for (String one : three) {
                /* ,로 스플릿해 나누면 해당 finalList의 값은 ['가','나','다']가 된다 */
                List<String> finalList = Arrays.stream(one.split(",")).toList();
                /* 이 가,나,다 의 정보를 DB에서 찾아 PLACE객체를 담을 예정이므로 새 Place제네릭을 가진 리스트를 생성 */
                List<Place> midForm = new ArrayList<>();

                /* 위의 ['가','나','다']를 하나씩 불러온다 */
                /* 가,나,다로 설명했지만 각각은 돈비어천가, 부평시장 등 상호, 장소명이다. */
                for (String finalOne : finalList) {
                    /* findByTitle은 이름으로 List<Place>를 호출하기때문에 get(0)으로 첫번째 값을 리턴 */
                    Place placeOne = placeInterface.findByTitle(finalOne).get(0);
                    /* '가'로 리턴된 Place객체를 midForm에 담는다 이를 3번 반복 */
                    midForm.add(placeOne);
                }
                /* place객체가 담긴 midForm리스트를 finalForm리스트에 추가한다 */

                finalForm.add(midForm);
            }

            HttpSession searchResult = request.getSession();
            /* "검색결과"라는 키로 세션 값 생성 */
            searchResult.setAttribute("검색결과",finalForm);

            /* 해당 리스트를 타임리프단에 전달 */
            model.addAttribute("searchResult", finalForm);

        }

        // place페이지 오른쪽 카드세트 번호에 글자 색 리스트
        String[] colors = {
                //"#ff0000", "#ff8c00", "#008000", "#0000ff", "#4b0082", "#8b00ff"
                "red", "orange", "green", "blue", "deep_blue", "purple"
        };
        model.addAttribute("colors", colors);


        return "place/place_thymeleaf";
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

    @GetMapping("client/myCard")
    public String goMyPage(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","myCard");



        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        HttpSession session = request.getSession(false);
        Client info = (Client) session.getAttribute("로그인");

        log.info("로그인 세션까지 확인 ");
        List<InviteCard> list = inviteCardInterface.loadById(info.getClientId());
        log.info("리스트 확인 "+list);
        List<Place> listPlace = new ArrayList<>();
        if (!list.isEmpty()){
            for (InviteCard one : list){
                Integer placeId1 = one.getPLACE_ID_1();
                Integer placeId2 = one.getPLACE_ID_2();
                Integer placeId3 = one.getPLACE_ID_3();
                Place place1 = placeInterface.idSearch(placeId1);
                placeImageToBinary(place1);
                Place place2 = placeInterface.idSearch(placeId2);
                placeImageToBinary(place2);
                Place place3 = placeInterface.idSearch(placeId3);
                placeImageToBinary(place3);
                listPlace.add(place1);
                listPlace.add(place2);
                listPlace.add(place3);
                break;
            }
        }
        model.addAttribute("category2", listPlace);



//        Random random = new Random();
//
//        int randomNumber1 = random.nextInt(1,10);
//        int randomNumber2 = random.nextInt(1,800);
//        int randomNumber3 = random.nextInt(1, 800);
//
//
//        List<Place> placeList2 = new ArrayList<>();
//        Place place12 = placeInterface.idSearch(randomNumber1);
//        /* 이미지 다운로드용 에러방지 이미지 주소를 변환 인코딩 */
//        placeImageToBinary(place12);
//        Place place22 = placeInterface.idSearch(randomNumber2);
//        placeImageToBinary(place22);
//        Place place32 = placeInterface.idSearch(randomNumber3);
//        placeImageToBinary(place32);
//        placeList2.add(place12);
//        placeList2.add(place22);
//        placeList2.add(place32);
//
//        place12.keyWord5();
//
//
//        /* 플레이스에서 보내져온 리스트 확인용 세션 */
//        HttpSession test = request.getSession(false);
//        /* 플레이스 리스트 객체를 담을 전역(컨트롤러 한정) 리스트변수 생성 */
//        List<Place>place2 = new ArrayList<>();
//
//        /* 세션 값이 있을 경우 이하 구문 실행 */
//        if (test!=null) {
//            /* 세션 중 플레이스에서 보내온 세션이 있는지 확인하고 있으면 플레이스 리스트 객체에 저장 */
//
//            /* 키값을 "검색결과" 로 받아오면 인바이트에서 List<Place>가 아니라
//               List<List<Place>를 받아오는 경우에 에러가 생겨서
//                세션 키값을 검색결과와 선택결과로 분리 */
//            place2 = (List<Place>) test.getAttribute("선택결과");
//
//            /* 그냥 Invite페이지에 왔을때를 위해 선택결과 세션 초기화 */
//            test.setAttribute("선택결과",null);
//            log.info("세션에서 받아온 정보 확인 "+place2);
//            /* "검색결과"키로 세션 정보를 받은 경우 */
//            if (place2 != null) {
//                /* 이미지 주소 다운 에러 방지용 base64타입 인코딩 객체를 담을 리스트 생성 */
//                List<Place>newImageAddPlace = new ArrayList<>();
//
//                /* 각 객체의 이미지 주소를 다운로드 시 에러방지용으로 base64타입으로 인코딩 */
//                for (Place obj : place2){
//                    placeImageToBinary(obj);
//                    newImageAddPlace.add(obj);
//                }
//                Place pl = newImageAddPlace.get(0);
//
//                /* 해당 리스트를 타임리프단으로 전달 */
//                model.addAttribute("category2", newImageAddPlace);
//                /* 인바이트 제목내용 전달 */
//                model.addAttribute("keylist",pl.keyWordTitle());
//                /* 세션 정보가 없을 경우 랜덤생성된 리스트 타임리프로 전달 */
//            } else model.addAttribute("category2",placeList2);
//        }
//        /* 세션 정보가 없을 경우 랜덤생성된 리스트 타임리프로 전달 */
//        else{
//            model.addAttribute("category2", placeList2);
//            log.info("NULL !!!!");
//        }

        return "client/myCard";
    }


    public static void loginCheck(HttpServletRequest request, Model model) {
        /* 세션 정보 가져오기 */
        /* create:false => 세션 정보가 있으면 기존 세션정보 로딩, 없으면 null 반환 */
        /* 세션 생성은 LoginController 참조 */
        HttpSession loginInfo = request.getSession(false);
        log.info("세션정보 " + loginInfo);


        /* 세션 정보가 없으면 client 객체에 null값 반환 */
        if (loginInfo==null) {
            model.addAttribute("client", null);
        } else {
            /* 세션 정보가 있으면 "로그인"이라는 키로 로그인 회원의 정보 가져오기 */
            Client loginClient = (Client) loginInfo.getAttribute("로그인");
            /* 해당 정보가 없으면 client 객체에 null값 반환 */
            if (loginClient==null) {
                model.addAttribute("client", null);
                /* 로그인 회원에 대한 정보를 client 객체에 담아 반환 */
            } else model.addAttribute("client", loginClient);
        }
    }

}
