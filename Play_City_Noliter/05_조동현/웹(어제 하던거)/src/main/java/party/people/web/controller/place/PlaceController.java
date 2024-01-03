package party.people.web.controller.place;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.Place;
import party.people.domain.SearchInput;
import party.people.domain.SearchResult;
import party.people.repository.place.PlaceInterface;
import party.people.repository.search.SearchInputInterface;


import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlaceController {
    private final PlaceInterface placeInterface;
    private final SearchInputInterface searchInputInterface;

//    @GetMapping("place")
    public String placeForm(Model model){
        List<Place> findAll = placeInterface.findAll();
        List<Place> findCategory = placeInterface.findByCategory("레저");
        List<Place> findTitle = placeInterface.findByTitle("강화도");
        log.info("placeForm] "+findTitle);

        return "place/placeTest";
    }


    /* 검색창 매핑 */
    @PostMapping("/place")
    public String searchPlace(HttpServletRequest request,
                              @RequestParam(value = "searchForm", required = false) String searchForm,
                              @RequestParam(value = "address", required = false) String address,
                              @RequestParam(value = "categorySubject", required=false) String categorySubject,
                              @RequestParam(value = "hashTag", required = false) String hashTag,
                              Model model){

        /* side lnb출력용 */
        model.addAttribute("category","place");

        log.info("키워드를 받아오자 :" + hashTag);
        /* 검색 결과를 출력하는 로직 */
        log.info("검색내용 :"+ searchForm + "/ 주소내용 : " + address + "/ 카테고리 : " + categorySubject);
        /* 우리의 검색 로직에는 3가지(카테고리, 키워드, 주소)가 들어가니 SearchInput 클래스에 넣는다 */
        SearchInput input = new SearchInput();
        if (address!=null) {
            input.setAddress(address);
            model.addAttribute("searchText", address);
        } else input.setAddress("");
        if (searchForm!=null) {
            input.setKeyword(searchForm);
            model.addAttribute("searchText",searchForm);
        } else {
            if (hashTag !=null) {
                input.setKeyword(hashTag);
                model.addAttribute("searchText", hashTag);
            } else input.setKeyword("");
        }
        if (categorySubject!=null){
            input.setCategory(categorySubject);
            model.addAttribute("searchText", categorySubject);
        } else input.setCategory("");
        /* 이를 searchinput DB Table에 집어넣는다 */
        searchInputInterface.save(input);
        /* 이와 동시에 searchResult DB 탐색을 시작한다 */
        List<SearchResult> old = searchInputInterface.loadAll();
        log.info("올드 사이즈 : "+old.size());
        /* 탐색 결과를 받아올 객체를 미리 생성한다 */
        SearchResult result = new SearchResult();
        /* 반복문을 통해 주기적으로 검색 결과에 응신이 있는지 확인한다. */
        while(true){
            /* 위의 old와 마찬가지 / 로그를 통해 올드 사이즈와 뉴 사이즈의 값을 확인 가능 */
            List<SearchResult> newLoad = searchInputInterface.loadAll();
            log.info("뉴 사이즈 :  " +newLoad.size());
            /* old 리스트와 new 리스트의 크기가 달라졌다는 것은 검색 결과에 대한 응신이 있다는것 */
            if (newLoad.size()!= old.size()){
                /* 맨마지막으로 저장된 리스트의 값을 result에 저장 */
                result = newLoad.get(newLoad.size()-1);
                break;
            }
            /* 대기 초 없이 반복하면 시스템에 무리가 가는 듯하여 1초마다 반복하게끔 실시  */
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* 검색된 결과를 우리 thymeleaf단에 맞게끔 가공하는 로직 */

//        /* 키워드 모음집을 보내는 로직 */
//        List<List<String>> topFinalKeyword = new ArrayList<>();
//        log.info("searchPlace]검색" +result.getSortKeyword());
//        List<String>topKeywords = Arrays.stream(result.getSortKeyword().split("/")).toList();
//        log.info("searchPlace] "+topKeywords);
//        List<String> mid = new ArrayList<>();
//        for (String one : topKeywords){
//            log.info("하나를 찾자 "+one);
//            mid = Arrays.stream(one.split(",")).toList();
//            topFinalKeyword.add(mid);
//        }


        /* 출력 형식이 이중 리스트로 [[PLACE, PLACE, PLACE], [PLACE, PLACE, PLACE], ... ] */
        /* 리스트에 PLACE 3개의 객체가 하나의 리스트로 들어가 있는 형식이어야 함  */
        /* 그래서 DB inputResult 테이블도 내부 3개는 ,로 리스트와 리스트사이는 / 로 구분지어둠 */
        List<List<Place>> finalForm = new ArrayList<>();
        /* 첫 리스트 쪼개기 /로 3개로 구성되어있는 리스트로 만들기 */
        /* ['가,나,다','1,2,3',...,'A,B,C'] */
        if (result.getResult()!=null) {
            List<String> three = Arrays.stream(result.getResult().split("/")).toList();
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
            log.info("searchPlace] " + finalForm);

            /* 해당 리스트를 타임리프단에 전달 */
            model.addAttribute("searchResult", finalForm);

            /* 세션 생성 */
            HttpSession searchResult = request.getSession();
            /* "검색결과"라는 키로 세션 값 생성 */
            searchResult.setAttribute("검색결과",finalForm);
        }

        // place페이지 오른쪽 카드세트 번호에 글자 색 리스트
        String[] colors = {
                "#ff0000", "#ff8c00", "#008000", "#0000ff", "#4b0082", "#8b00ff"
        };
        model.addAttribute("colors", colors);


        /* 검색한 내용을 키워드에 추가하는 로직 => 해당 로직은 완성 됐지만 반복시 데이터가 오염되므로 실 서비스시 주석 해제*/
//        for (Place one : searchResult){
//            String keyword = one.getKeyword();
//            Map<String, Integer> map = new HashMap<>();
//            map = keywordToMap(map, keyword);
//            log.info("오류발생 지점 확인용 ");
//            if(splitSearch!=null){
//                log.info("searchPlace] "+splitSearch);
//                for (String split : splitSearch){
//                    log.info("split] "+split);
//                    if (placeInterface.findByKeyword(splitSearch)!=null) {
//                        addNewKeyword(map, split);
//                    }
//                }
//            }
//            String updated = mapToSortedString(map);
//            placeInterface.updateKeyword(one.getTitle(), updated);
//        }


        return "place/place_thymeleaf";
    }



}
