package party.people.web.controller.place;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.Client;
import party.people.domain.Place;
import party.people.domain.SearchInput;
import party.people.domain.SearchResult;
import party.people.repository.client.ClientInterface;
import party.people.repository.place.PlaceInterface;
import party.people.repository.search.SearchInputInterface;
import party.people.web.controller.client.formAndDto.ClientUpdateDto;

import static party.people.web.controller.category.CategoryController.loginCheck;
import java.util.*;

import static party.people.service.keyword.keywordToMapLogic.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlaceController {
    private final PlaceInterface placeInterface;
    private final SearchInputInterface searchInputInterface;
    private final ClientInterface clientInterface;



    /* 검색창 매핑 */
    @PostMapping("/place")
    /* 다양한 방식으로 검색 입력을 RequestParam으로 받는다, required=false는 null 예외 방지용으로 사용 */
    public String searchPlace(HttpServletRequest request,
                              @RequestParam(value = "searchForm", required = false) String searchForm,
                              @RequestParam(value = "address", required = false) String address,
                              @RequestParam(value = "categorySubject", required=false) String categorySubject,
                              @RequestParam(value = "hashTag", required = false) String hashTag,
                              @RequestParam(value = "buttonId", required = false) String buttonId,
                              Model model) {
        /* 기존 세션 정보 로딩 */
        HttpSession session = request.getSession(false);
        log.info("해시태그를 제대로 받는가 "+hashTag);

        /* side lnb출력용 */
        model.addAttribute("category", "place");

        loginCheck(request, model);

        /* 세션 정보가 있을 경우 로직 수행 */
        if (session != null) {

            /* "검색결과"키로 리스트 로딩 */
            List<List<Place>> place2 = (List<List<Place>>) session.getAttribute("검색결과");

            /* 모임카드 편집하기 버튼 클릭시 로직 수행용 객체 */
            List<Place> selected = new ArrayList<>();
            /* 버튼 클릭시 아래 로직 수행 */
            if (buttonId != null) {
                /* 스트링으로 넘어오는 id를 int로 변경 */
                int numberId = Integer.parseInt(buttonId);

                /* 세션으로 받아온 리스트에서 버튼아이디-1로 모임카드 편집하기 버튼에서 가지고있는 객체들 저장 */
                /* 리스트의 인덱스는 0에서 시작하지만 iterStat.count는 1부터 시작하기 때문에 -1을 해줘야 함 */
                selected = place2.get(numberId - 1);
                /* 세션 생성 */
                HttpSession searchResult = request.getSession();
                /* "선택결과"라는 키로 세션 값 재생성 */
                searchResult.setAttribute("선택결과", selected);
                /* 인바이트 페이지로 사용자 리다이렉트 */
                return "redirect:/invite";
            }
            log.info("선택된 녀석을 보자" + selected);
//        }




            String prevCategory = (String) session.getAttribute("카테");
            // 새로운 카테고리 값이 전달되면 세션에 저장합니다.
            if (categorySubject != null && !categorySubject.equals(prevCategory)) {
                session.setAttribute("카테", categorySubject);
            }

            // 카테고리 값이 없으면 세션에 저장된 이전 카테고리 값을 사용합니다.
            if (categorySubject == null && prevCategory != null) {
                categorySubject = prevCategory;
            }
            log.info("카테고리 서브젝트 " + categorySubject);

            /* 검색창 카테고리 출력용 */
            model.addAttribute("category2", categorySubject);

            String prevAddrress = (String) session.getAttribute("주소");
            // 새로운 카테고리 값이 전달되면 세션에 저장합니다.
            if (address != null && !address.equals(prevAddrress)) {
                session.setAttribute("주소", address);
            }

            // 카테고리 값이 없으면 세션에 저장된 이전 카테고리 값을 사용합니다.
            if (address == null && prevAddrress != null) {
                address = prevAddrress;
            }
            log.info("주소는 " + address);

            /* 검색창 카테고리 출력용 */
            model.addAttribute("category2", categorySubject);
        }





        /* 검색 결과를 출력하는 로직 */
        /* 우리의 검색 로직에는 3가지(카테고리, 키워드, 주소)가 들어가니 SearchInput 클래스에 넣는다 */
        SearchInput input = new SearchInput();
        List<String> searchTextList = new ArrayList<>();
        if (categorySubject != null) {
            input.setCategory(categorySubject);
            model.addAttribute("searchText", categorySubject);
            searchTextList.add(categorySubject);
        } else input.setCategory("");
        if (address != null) {
            if (address.equals("null")) {
                return "redirect:/place";
            } else {
                input.setAddress(address);
                model.addAttribute("address", address);
                model.addAttribute("searchText", address);
                searchTextList.add(address);
            }
        } else {
            input.setAddress("");
            model.addAttribute("address", "null");
        }
        if (searchForm != null) {
            input.setKeyword(searchForm);
            model.addAttribute("searchText", searchForm);
            searchTextList.add(searchForm);
        } else {
            if (hashTag != null) {
                input.setAddress("");
                input.setCategory("");
                input.setKeyword(hashTag);
                searchTextList.add(hashTag);
                model.addAttribute("searchText", hashTag);
            } else input.setKeyword("");
        }


        /* 세션으로 카테고리, 지역 등 중복 체킹 돼었을때 검색어 출력 로직 */
        if (searchTextList!=null) {
            String searchText = new String();
            int count = 0;
            for (String oneText : searchTextList) {
                if (count == 0)
                    searchText = searchText + oneText;
                else searchText = searchText + " / " + oneText;
                count++;
            }
            model.addAttribute("searchText", searchText);
        }

        /* 이를 searchinput DB Table에 집어넣는다 */
        searchInputInterface.save(input);
        /* 이와 동시에 searchResult DB 탐색을 시작한다 */
        List<SearchResult> old = searchInputInterface.loadAll();
        log.info("올드 사이즈 : " + old.size());
        /* 탐색 결과를 받아올 객체를 미리 생성한다 */
        SearchResult result = new SearchResult();
        /* 반복문을 통해 주기적으로 검색 결과에 응신이 있는지 확인한다. */
        while (true) {
            /* 위의 old와 마찬가지 / 로그를 통해 올드 사이즈와 뉴 사이즈의 값을 확인 가능 */
            List<SearchResult> newLoad = searchInputInterface.loadAll();
            log.info("뉴 사이즈 :  " + newLoad.size());
            /* old 리스트와 new 리스트의 크기가 달라졌다는 것은 검색 결과에 대한 응신이 있다는것 */
            if (newLoad.size() != old.size()) {
                /* 맨마지막으로 저장된 리스트의 값을 result에 저장 */
                result = newLoad.get(newLoad.size() - 1);
                break;
            }
            /* 대기 초 없이 반복하면 시스템에 무리가 가는 듯하여 0.5초마다 반복하게끔 실시  */
            try {
                Thread.sleep(500); // 0.5초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* 검색된 결과를 우리 thymeleaf단에 맞게끔 가공하는 로직 */

        /* 출력 형식이 이중 리스트로 [[PLACE, PLACE, PLACE], [PLACE, PLACE, PLACE], ... ] */
        /* 리스트에 PLACE 3개의 객체가 하나의 리스트로 들어가 있는 형식이어야 함  */
        /* 그래서 DB inputResult 테이블도 내부 3개는 ,로 리스트와 리스트사이는 / 로 구분지어둠 */
        List<List<Place>> finalForm = new ArrayList<>();
        /* 첫 리스트 쪼개기 /로 3개로 구성되어있는 리스트로 만들기 */
        /* ['가,나,다','1,2,3',...,'A,B,C'] */
        if (result.getResult() != null) {
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
//            log.info("searchPlace] " + finalForm);
            HttpSession searchResult = request.getSession();
            /* "검색결과"라는 키로 세션 값 생성 */
            searchResult.setAttribute("검색결과", finalForm);

            /* 해당 리스트를 타임리프단에 전달 */
            model.addAttribute("searchResult", finalForm);

            /* 검색 결과가 널이 아니면 키워드에 검색어 추가 */
            /* 컨트롤 클릭으로 함수 확인 가능 */
            /* DB 오염 방지로 아직 DB 수정은 주석 처리 돼있음*/
            updateKeywordBySearchResult(input, finalForm);

            /* 세션 정보가 있을 경우 로직 수행 */
            if (session != null) {
                Client client = (Client) session.getAttribute("로그인");
                if (client != null) {
                    String keyword = client.getKeyword();
                    Map<String, Integer> map = new HashMap<>();
                    map = keywordToMap(map, keyword);
                    List<String> splitSearch = new ArrayList<>();
                    splitSearch.add(input.getCategory());
                    splitSearch.add(input.getKeyword());
                    splitSearch.add(input.getAddress());
                    for (String split : splitSearch) {
                        log.info("split] " + split);
                        if (!split.equals("")) {
                            addNewKeyword(map, split);
                        }
                    }
                    String updated = mapToSortedString(map);
                    log.info("아이디로 검색 정렬 결과 : "+updated);
                    log.info("너의 아이디는?"+ client.getClientId());
                    ClientUpdateDto param = new ClientUpdateDto();
                    param.setPassword2(client.getPassword());
                    param.setClientEmail(client.getClientEmail());
                    param.setKeyword(updated);
                    log.info("DTO 내용 "+ param);
                    clientInterface.update(client.getClientId(),param);
                    Client updatedClient = clientInterface.findByClientId(client.getClientId()).orElse(null);
                    log.info("제대로 업데이트 됐는지 확인 "+updatedClient);
                    session.setAttribute("로그인",updatedClient);
                }
            }


        } else {
            List<SearchResult> allResult = searchInputInterface.loadAll();
            Random random = new Random();
            int randomValue = random.nextInt(allResult.size());

            SearchResult start = allResult.get(randomValue);

            /* 출력 형식이 이중 리스트로 [[PLACE, PLACE, PLACE], [PLACE, PLACE, PLACE], ... ] */
            /* 리스트에 PLACE 3개의 객체가 하나의 리스트로 들어가 있는 형식이어야 함  */
            /* 그래서 DB inputResult 테이블도 내부 3개는 ,로 리스트와 리스트사이는 / 로 구분지어둠 */
//            List<List<Place>> finalForm = new ArrayList<>();
            /* 첫 리스트 쪼개기 /로 3개로 구성되어있는 리스트로 만들기 */
            /* ['가,나,다','1,2,3',...,'A,B,C'] */
            if (start.getResult() != null) {
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
                log.info("searchPlace] " + finalForm);

                HttpSession searchResult = request.getSession();
                /* "검색결과"라는 키로 세션 값 생성 */
                searchResult.setAttribute("검색결과", finalForm);

                /* 해당 리스트를 타임리프단에 전달 */
                model.addAttribute("searchNull", "null");
                model.addAttribute("searchResult", finalForm);
            }

        }


        // place페이지 오른쪽 카드세트 번호에 글자 색 리스트
        String[] colors = {
                //"#ff0000", "#ff8c00", "#008000", "#0000ff", "#4b0082", "#8b00ff"
                "red", "orange", "green", "blue", "deep_blue", "purple"
        };
        model.addAttribute("colors", colors);




        return "place/place_thymeleaf";
    }

    public void updateKeywordBySearchResult(SearchInput input, List<List<Place>> finalForm) {
        /* 검색한 내용을 키워드에 추가하는 로직 => 해당 로직은 완성 됐지만 반복시 데이터가 오염되므로 실 서비스시 주석 해제*/
        for (List<Place> oneList : finalForm) {
            for (Place one : oneList) {
                String keyword = one.getKeyword();
                Map<String, Integer> map = new HashMap<>();
                map = keywordToMap(map, keyword);
                List<String> splitSearch = new ArrayList<>();
                splitSearch.add(input.getCategory());
                splitSearch.add(input.getKeyword());
                splitSearch.add(input.getAddress());
                for (String split : splitSearch) {
//                    log.info("split] " + split);
                    if (!split.equals("")) {
                        addNewKeyword(map, split);
                    }
                }
                String updated = mapToSortedString(map);
//                log.info("최종 검색 정렬 결과 "+updated);
//                log.info("너는 누구니" + one.getTitle());

                placeInterface.updateKeyword(one.getTitle(), updated);

            }
        }
    }


}
