package party.people.web.controller.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.*;
import party.people.repository.keywords.KeywordsInterface;
import party.people.repository.mainCard.MainCardInterface;
import party.people.repository.place.PlaceInterface;
import party.people.repository.test.TestInterface;
import party.people.service.keyword.KeywordsMerge;

import static party.people.web.controller.category.CategoryController.loginCheck;
import static party.people.service.keyword.keywordToMapLogic.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    /* DB 접속 확인용 인터페이스*/
    private final TestInterface testInterface;
    private final PlaceInterface placeInterface;
    private final KeywordsInterface keywordsInterface;
    private final MainCardInterface mainCardInterface;
    private final KeywordsMerge keywordsMerge;

    String MAIN = "main";

    /* home */
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model){
/*
    1. 메인 페이지 카드리스트 내용 전달
*/
        /* 랜덤 클래스 객체 생성 */
        Random random = new Random();
        /* 카테고리 리스트 생성 */
        List<String> randomCategory = Arrays.asList("관광", "전시", "자연", "레저", "쇼핑", "음식", "숙박");
        /* 랜덤 객체를 이용 리스트의 사이즈 수의 범위내에서 랜덤으로 숫자를 생성한다. */
        int randomNumber = random.nextInt(randomCategory.size());
        /* 랜덤으로 생성된 숫자를 이용해 리스트의 카테고리를 불러오고 카테고리를 이용해 키워드를 불러온다. */
        MainCard main = mainCardInterface.load(randomCategory.get(randomNumber));
        /* 어떤 카테고리인지 thymeleaf단에 전달 */
        model.addAttribute("category3", main.getCategory());

        /* 불러온 String 키워드를 /로 스플릿하여 리스트 생성 */
        List<String> three = Arrays.stream(main.getResult().split("/")).toList();
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
            /* place객체가 담긴 midForm리스트를 thymeleaf에 전달한다 */
            model.addAttribute("category2",midForm);
            break;
        }

/*
    2. 워드 클라우드용 리스트 전달
*/
        Keywords keywords = keywordsInterface.findByCategory(randomCategory.get(randomNumber));
        String keyword=keywords.getKeywords();
        Map<String, Integer> map = new HashMap<>();
        keywordToMap(map, keyword);
        List<Map.Entry<String, Integer>> entries = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> title = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            title.add(entry.getKey());
            values.add(entry.getValue());
            count++;
            if (count== entries.size()){
                break;
            }
        }
//        log.info("클라우드 보낼 타이틀 :"+title);
//        log.info("클라우드 보낼 밸류 :"+values);
        model.addAttribute("cloudTitle",title);
        model.addAttribute("cloudValues",values);

//
//        /*
//    3. 슬라이드용
//*/
//
//        /* 슬라이드 생성용 리스트 생성 */
//        List<MainCardDomain> slideList = new ArrayList<>();
//        /* 카테고리별로 슬라이드 돌릴 거기 때문에 카테고리별 리스트 생성 */
//        for (String category : randomCategory) {
//            /* 메인 슬라이드 도메인용 객체 생성 정보는 도메인에서 확인 */
//            MainCardDomain slideDomain = new MainCardDomain();
//            slideDomain.setCategory(category);
//
//            main = mainCardInterface.load(category);
//            /* 어떤 카테고리인지 thymeleaf단에 전달 */
//            model.addAttribute("category3", main.getCategory());
//
//            /* 불러온 String 키워드를 /로 스플릿하여 리스트 생성 */
//            three = Arrays.stream(main.getResult().split("/")).toList();
//            for (String one : three) {
//                /* ,로 스플릿해 나누면 해당 finalList의 값은 ['가','나','다']가 된다 */
//                List<String> finalList = Arrays.stream(one.split(",")).toList();
//                /* 이 가,나,다 의 정보를 DB에서 찾아 PLACE객체를 담을 예정이므로 새 Place제네릭을 가진 리스트를 생성 */
//                List<Place> midForm = new ArrayList<>();
//
//                /* 위의 ['가','나','다']를 하나씩 불러온다 */
//                /* 가,나,다로 설명했지만 각각은 돈비어천가, 부평시장 등 상호, 장소명이다. */
//                for (String finalOne : finalList) {
//                    /* findByTitle은 이름으로 List<Place>를 호출하기때문에 get(0)으로 첫번째 값을 리턴 */
//                    Place placeOne = placeInterface.findByTitle(finalOne).get(0);
//                    /* '가'로 리턴된 Place객체를 midForm에 담는다 이를 3번 반복 */
//                    midForm.add(placeOne);
//                }
//                /* place객체가 담긴 midForm리스트를 thymeleaf에 전달한다 */
//                slideDomain.setMainPlace(midForm);
//                model.addAttribute("placeList",midForm);
//                break;
//            }
//            keywords = keywordsInterface.findByCategory(category);
//            keyword=keywords.getKeywords();
//            Map<String, Integer> slideMap = new HashMap<>();
//            keywordToMap(slideMap, keyword);
//            List<Map.Entry<String, Integer>> slideEntries = slideMap.entrySet().stream()
//                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                    .collect(Collectors.toList());
//            List<String> slideTitle = new ArrayList<>();
//            List<Integer> slideValues = new ArrayList<>();
//            count = 0;
//            for (Map.Entry<String, Integer> entry : slideEntries) {
//                slideTitle.add(entry.getKey());
//                slideValues.add(entry.getValue());
//                count++;
//                if (count== slideEntries.size()){
//                    break;
//                }
//            }
//            slideDomain.setCloudTitle(slideTitle);
//            slideDomain.setCloudValues(slideValues);
//            slideList.add(slideDomain);
//        }
//        model.addAttribute("slideList",slideList);
//






/*
    4. 첫 페이지 호출 시 회원 및 플레이스의 키워드 리스트들을 새롭게 갱신
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
    5. 로그인 비로그인 세션 관련 로직 수행
*/
        /* 세션 정보 가져오기 */
        /* create:false => 세션 정보가 있으면 기존 세션정보 로딩, 없으면 null 반환 */
        /* 세션 생성은 LoginController 참조 */
        HttpSession loginInfo =request.getSession(false);
        log.info("세션정보 " + loginInfo);

        /* 기존 세션 정보가 없으면 home으로 이동 */
        if (loginInfo==null){
            model.addAttribute("client",null);
            return MAIN;
        }

        /* 세션 정보 "로그인"으로 세션 정보를 호출해 Client loginClient 객체를 생성 */
        /* LoginController에서 확인 가능 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");
        log.info("로그인이 되었는가" + loginClient);
        /* 해당 객체가 없다면 home으로 이동 */
        if (loginClient==null){
            model.addAttribute("client",null);
            return MAIN;
        }
        log.info("home] "+loginClient);
        /* 세션에 저장되어있는 로그인 정보를 thymeleaf단에 "client"이름으로 전달 */
        model.addAttribute("client",loginClient);

        /* Oracle 데이터베이스 연결 확인 */
        List<Test> test = testInterface.findAll();

        /* 세션 객체(로그인 정보)가 null이 아니라면 loginHome으로 이동 */
        return MAIN;
    }

    @PostMapping("/")
    public String postHome(HttpServletRequest request, Model model,
//                            @RequestParam(value = "hashTag", required = false) String hashTag,
                            @RequestParam(value = "categorySub", required = false) String categorySub){

//        HttpSession session = request.getSession();
//        log.info("너의집에서 보낸 녀석 "+hashTag);

//        if(hashTag!=null){
//            session.setAttribute("해시",hashTag);
//            return "place/place_thymeleaf";
//        }

        /* 포스트매핑시 로그인 유지 */
        loginCheck(request,model);

        /* 카테고리 정보를 thymeleaf에 전달 */
        model.addAttribute("category3", categorySub);


        /* 카테고리로 키워드 로딩 */
        MainCard main = mainCardInterface.load(categorySub);
        List<String> three = Arrays.stream(main.getResult().split("/")).toList();
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
            model.addAttribute("category2",midForm);
            /* 메인에는 첫번째 리스트만 사용하기 때문에 브레이크 */
            break;
        }


        /*
    2. 워드 클라우드용 리스트 전달
*/
        Keywords keywords = keywordsInterface.findByCategory(categorySub);
        String keyword=keywords.getKeywords();
        Map<String, Integer> map = new HashMap<>();
        keywordToMap(map, keyword);
        List<Map.Entry<String, Integer>> entries = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> title = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            title.add(entry.getKey());
            values.add(entry.getValue());
            count++;
            if (count== entries.size()){
                break;
            }
        }
        log.info("클라우드 보낼 타이틀 :"+title);
        log.info("클라우드 보낼 밸류 :"+values);
        model.addAttribute("cloudTitle",title);
        model.addAttribute("cloudValues",values);



        return "main";
    }

}
