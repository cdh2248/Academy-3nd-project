package party.people.web.controller.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import party.people.domain.Client;
import party.people.domain.Keywords;
import party.people.domain.Place;
import party.people.domain.Test;
import party.people.repository.keywords.KeywordsInterface;
import party.people.repository.test.TestInterface;
import party.people.service.keyword.KeywordsMerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    /* DB 접속 확인용 인터페이스*/
    private final TestInterface testInterface;
    private final KeywordsInterface keywordsInterface;
    private final KeywordsMerge keywordsMerge;


    /* home */
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model){
        /* 임시 place가 메인일때만 사용 */
        model.addAttribute("category","place");

/*
    1. 첫 페이지 호출 시 회원 및 플레이스의 키워드 리스트들을 새롭게 갱신
*/
        /* 기존 유무 확인 위해 카테고리 갱신 */
        List<String> interestCheckbox = Arrays.asList("관광","자연","레저","쇼핑","음식","숙박","전시");
        List<String> regionCheckbox = Arrays.asList("연수구","남동구","부평구","계양구","서구","동구","중구","미추홀구","강화군","옹진군");
        /* 위 리스트를 addAll로 mergedList를 통합 */
        List<String> mergedList = new ArrayList<>(interestCheckbox);
        mergedList.addAll(regionCheckbox);
        /* "회원"도 중복유무를 확인하기 위해 임시로 리스트에 삽입 */
        mergedList.add("회원");

        /* DB에서 통합 키워드리스트 불러오기 */
        List<Keywords>all = keywordsInterface.findAll();


        /* Flag 변수 -> DB에 있는지 확인 유무를 체크하는 용도 */
        Boolean check = false;
        /* 위에 리스트가 이미 통합 DB에 있는지 확인 */
        for (Keywords word : all){
            for (String one : mergedList){
                if (word.getCategory().equals(one)){
                    check=true;
                }
            }
        }
        /* 여기까진 문제 없음 */

        /* 중복 유무를 확인했으니 place객체 string값을 전달하기 위해 place랑 상관없는 "회원"다시 제거 */
        mergedList.remove("회원");
        /* 이미 DB에 없으면 False save로 저장 / 있으면 True update */
        if (!check){ /* DB에 없으면 false -> !사용해 true로 전환해 save service 실시 */
            log.info("check "+check);
            /* 회원 키워드 저장 */
            keywordsMerge.saveClientToDb();
            /* 플레이스 키워드 for문으로 카테고리, 지역 별 저장 */
            for (String one : mergedList){
                keywordsMerge.savePlaceToDb(one);
            }
        } else { /* DB에 있으면 true -> save하면 중복 열이 생기므로 update service 실시 */
            /* 회원 키워드 업데이트 */
            keywordsMerge.updateClientToDb();
            /* 플레이스 키워드 for문으로 카테고리, 지역 별 저장 */
            for (String one : mergedList) {
                keywordsMerge.updatePlaceToDb(one);
            }
        }
/*
    2. 로그인 비로그인 세션 관련 로직 수행
*/
        /* 세션 정보 가져오기 */
        /* create:false => 세션 정보가 있으면 기존 세션정보 로딩, 없으면 null 반환 */
        /* 세션 생성은 LoginController 참조 */
        HttpSession loginInfo =request.getSession(false);
        log.info("세션정보 " + loginInfo);

        /* 기존 세션 정보가 없으면 home으로 이동 */
        if (loginInfo==null){
            model.addAttribute("client",null);
            return "place/place_thymeleaf";
        }

        /* 검색결과 다중 세션 여부 확인용 */
        List<List<Place>>place = (List<List<Place>>) loginInfo.getAttribute("검색결과");
        log.info("다중세션 여부 확인 " + place);


        /* 세션 정보 "로그인"으로 세션 정보를 호출해 Client loginClient 객체를 생성 */
        /* LoginController에서 확인 가능 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");
        log.info("로그인이 되었는가" + loginClient);
        /* 해당 객체가 없다면 home으로 이동 */
        if (loginClient==null){
            model.addAttribute("client",null);
            return "place/place_thymeleaf";
        }
        log.info("home] "+loginClient);
        /* 세션에 저장되어있는 로그인 정보를 thymeleaf단에 "client"이름으로 전달 */
        model.addAttribute("client",loginClient);

        /* Oracle 데이터베이스 연결 확인 */
        List<Test> test = testInterface.findAll();

        /* 세션 객체(로그인 정보)가 null이 아니라면 loginHome으로 이동 */
        return "place/place_thymeleaf";
    }

}
