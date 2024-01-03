package party.people.service.keyword;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import party.people.domain.Place;
import party.people.repository.place.PlaceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static party.people.service.keyword.keywordToMapLogic.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordInSomething {
    private final PlaceInterface placeInterface;

    /* 이 페이지 서비스들은 최초 실행 한 번만 되어야 함 */
    /* 안 그러면 DB 오염 됨 */

    /* 모든 플레이스의 키워드리스트를 내림차순 정렬하는 서비스 */
    /* 이 서비스는 DB오염과는 상관 없음 */
    public void allPlaceSort(){

        /* 전체 리스트 불러오기 */
        List<Place> allList = placeInterface.findAll();

        /* 가져온 리스트 하나하나의 객체 불러오기 */
        for (Place one : allList){
            /* 정렬할 때 사용할 맵 생성 */
            /* for문 안에서 생성해줘야 포문이 시작할 때마다 초기화된다 */
            Map<String, Integer>mapKeyword = new HashMap<>();
            String keyword = one.getKeyword();
            log.info("정렬전 키워드 "+keyword);

            /* 사용할 키워드를 정렬용 맵에 삽입 */
            /* 자세한 로직은 함수이름 컨트롤 클릭 */
            mapKeyword = keywordToMap(mapKeyword, keyword);

            /* 맵을 내림차순 정렬한 후 다시 키워드로 변환 */
            /* 자세한 로직은 함수이름 컨트롤 클릭 */
            String sortedKeyword = mapToSortedString(mapKeyword);
            log.info("정렬후 키워드 "+sortedKeyword);

            /* 키워드 업데이트에는 제목이 필요하므로 제목 변수 저장 */
            String title = one.getTitle();
            /* 제목변수로 DB를 찾아서 정렬된 키워드로 업데이트 */
            /* 해당 update는 미리 title에 중복값이 없음을 미리 확인하고 title로 사용한 것! */
            placeInterface.updateKeyword(title,sortedKeyword);
        }
    }


    /* 팀장님의 오더에 따라 키워드리스트에 제목과 카테고리 소분류, 지역구(정규식 적용) 등을 추가해줌 */
    /* 절대 재사용 금지 DB 오염됨 */
    public void addToKeyword(){
        /* 모든 리스트 호출 */
        List<Place> allList = placeInterface.findAll();
        for(int i=0; i<allList.size(); i++){
            /* 맵 생성 */
            Map<String, Integer> map = new HashMap<>();
            /* 추가할 데이터 변수 저장  */
            String title = allList.get(i).getTitle();
            String category = allList.get(i).getCategory();
            String smallCategory = allList.get(i).getSmallCategory();
            String address = allList.get(i).getAddress();
            /* 주소는 구와 동 군읍면 등으로 추가할 거기때문에 정규식 적용 */
            /* 정규식에 적용에 대한 내용은 함수이름 컨트롤 클릭 */
            List<String>splitAdd = regexAddress(address);
            /* 키워드 호출 */
            String keyword = allList.get(i).getKeyword();
            /* 키워드를 해시맵으로 변환 */
            if (keyword!=null) {
                /* 자세한 내용은 함수명 컨트롤 우클릭 */
                map = keywordToMap(map, keyword);
            }
            /* 맵에 keyword를 추가 */
            /* 자세한 내용은 함수명 컨트롤 우클릭 */
            addNewKeyword(map,title);
            addNewKeyword(map,category);
            addNewKeyword(map,smallCategory);
            for (String one : splitAdd){
                addNewKeyword(map,one);
            }
            /* 추가한 맵을 다시 DB삽입용 키워드로 변환 */
            String mergedKeyword= mapToString(map);

            /* 제목변수로 DB를 찾아서 추가된 키워드로 업데이트 */
            placeInterface.updateKeyword(title,mergedKeyword);
        }

    }


    public static List<String> regexAddress(String input) {
        /* 정규식 적용 분리될 리스트 생성 */
        List<String>regexAddress = new ArrayList<>();
        while(true) {
            System.out.println("추출대상: "+ input);
            /* 한글로 시작해서 구동군읍면 중에 하나를 만나면 그 해당 부분을 추출 */
            String regex = "([가-힣]+)(?:구|동|군|읍|면)\\s";
            /* 정규식 적용하는 패턴 */
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            /* 정규식에 해당 문구가 있으면 true 호출 */
            boolean extract = matcher.find();
            if (extract){
                /* 해당 문구를 one 변수에 삽입 */
                String one = matcher.group();
                /* 해당 문구를 리스트에 추가 */
                regexAddress.add(one);
                System.out.println("추출결과: "+one);
                /* 해당 문구를 원래 주소에서 제거 */
                input = input.replace(one, "");
            } else {
                /* 더 이상 정규식에 해당하는 문구가 없으면 종료 */
                System.out.println("더 이상 없음");
                break;
            }
        }
        return regexAddress;
    }
}

