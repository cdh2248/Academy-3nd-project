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
    public void allPlaceSort(){
        /* 정렬할 때 사용할 맵 생성 */

        /* 전체 리스트 불러오기 */
        List<Place> allList = placeInterface.findAll();
        for (Place one : allList){
            Map<String, Integer>mapKeyword = new HashMap<>();
            String keyword = one.getKeyword();
            log.info("정렬전 키워드 "+keyword);
            mapKeyword = keywordToMap(mapKeyword, keyword);
            String sortedKeyword = mapToSortedString(mapKeyword);
            log.info("정렬후 키워드 "+sortedKeyword);
            System.out.println(sortedKeyword);
            String title = one.getTitle();
            placeInterface.updateKeyword(title,sortedKeyword);
        }

    }


    public void addToKeyword(){
        List<Place> allList = placeInterface.findAll();
        for(int i=0; i<allList.size(); i++){
            Map<String, Integer> map = new HashMap<>();
            String title = allList.get(i).getTitle();
            String category = allList.get(i).getCategory();
            String smallCategory = allList.get(i).getSmallCategory();
            String address = allList.get(i).getAddress();
            List<String>splitAdd = regexAddress(address);
            String keyword = allList.get(i).getKeyword();
            if (keyword!=null) {
                map = keywordToMap(map, keyword);
            }
            addNewKeyword(map,title);
            addNewKeyword(map,category);
            addNewKeyword(map,smallCategory);
            for (String one : splitAdd){
                addNewKeyword(map,one);
            }
            String mergedKeyword= mapToString(map);
            System.out.println(mergedKeyword);
            placeInterface.updateKeyword(title,mergedKeyword);
        }

    }


    public static List<String> regexAddress(String input) {
        List<String>regexAddress = new ArrayList<>();
        while(true) {
            System.out.println("추출대상: "+ input);
            String regex = "([가-힣]+)(?:구|동|군|읍|면)\\s";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            boolean extract = matcher.find();
            if (extract){
                String one = matcher.group();
                regexAddress.add(one);
                System.out.println("추출결과: "+one);
                input = input.replace(one, "");
            } else {
                System.out.println("더 이상 없음");
                break;
            }
        }
        return regexAddress;
    }
}

