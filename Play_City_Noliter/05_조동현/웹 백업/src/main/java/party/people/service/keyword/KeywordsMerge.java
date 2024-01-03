package party.people.service.keyword;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import party.people.domain.Client;
import party.people.domain.Keywords;
import party.people.domain.Place;
import party.people.repository.client.ClientInterface;
import party.people.repository.keywords.KeywordsInterface;
import party.people.repository.place.PlaceInterface;

import static party.people.service.keyword.keywordToMapLogic.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordsMerge {
    private final ClientInterface clientInterface;
    private final PlaceInterface placeInterface;
    private final KeywordsInterface keywordsInterface;

    /* 클라이언트 객체가 담긴 리스트를 넣으면 키워드를 빈도별 내림차순해 스트링으로 추출해줌 */
    public String clientKeywords(List<Client> all) {
        /* 클라이언트 리스트에 담긴 키워드를 키워드와 빈도로 나누기 위해 해시맵 사용 */
        Map<String, Integer> total = new HashMap<>();
        /* 스트링으로 추출하기위한 변수 생성 */
        String keywords = "";
        /* 제공 받은 리스트를 for문 수행 */
        for (int i = 0; i < all.size(); i++) {
            /* 클라이언트 객체의 키워드만 추출  */
            String one = all.get(i).getKeyword();
            if (one != null) {
                /* 해시맵에 키워드와 빈도로 나누어 저장하는 함수 수행 (컨트롤 클릭으로 메서드 참조가능) */
                total = keywordToMap(total ,one);
            }
        }
        /* 생성된 해시맵을 빈도별로 내림차순한 뒤 다시 스트링으로 변환 (컨트롤 클릭으로 메서드 참조가능) */
        keywords = mapToSortedString(total);
        return keywords;
    }

    /* 플레이스 객체가 담긴 리스트를 넣으면 키워드를 추출해줌 */
    public String placeKeywords(List<Place> all) {
        /* 플레이스 리스트에 담긴 키워드를 키워드와 빈도로 나누기 위해 해시맵 사용 */
        Map<String, Integer> total = new HashMap<>();
        /* 스트링으로 추출하기위한 변수 생성 */
        String keywords = "";
        /* 제공 받은 리스트를 for문 수행 */
        for (int i = 0; i < all.size(); i++) {
            /* 플레이스 객체의 키워드만 추출  */
            String one = all.get(i).getKeyword();
            if (one != null) {
                /* 해시맵에 키워드와 빈도로 나누어 저장하는 함수 수행 (컨트롤 클릭으로 메서드 참조가능) */
                total = keywordToMap(total ,one);
            }
        }
        /* 생성된 해시맵을 빈도별로 내림차순한 뒤 다시 스트링으로 변환 (컨트롤 클릭으로 메서드 참조가능) */
        keywords = mapToSortedString(total);
        return keywords;
    }

    /* 나온 키워드를 DB에 저장하는 로직 */
    public void saveClientToDb(){
        /* 클라이언트 정보 전체 호출 */
        List<Client>clientList = clientInterface.findAll();
        /* 클라이언트 전체 정보를 통합 키워드화 */
        String clientKeywords = clientKeywords(clientList);
        /* 키워드 객체에 정보 담기 */
        Keywords client = new Keywords();
        client.setCategory("회원");
        client.setKeywords(clientKeywords);
        log.info("sendToDb] "+client);
        /* 저장 로직 수행 */
        keywordsInterface.save(client);
    }

    /* 나온 키워드를 DB에 업데이트하는 로직 */
    public void updateClientToDb(){
        /* 클라이언트 정보 전체 호출 */
        List<Client>clientList = clientInterface.findAll();
        /* 클라이언트 전체 정보를 통합 키워드화 */
        String clientKeywords = clientKeywords(clientList);
        /*  키워드 객체에 정보 담기*/
        Keywords client = new Keywords();
        client.setCategory("회원");
        client.setKeywords(clientKeywords);
        /* 업데이트 로직 수행 */
        keywordsInterface.update(client);
    }

    /* 나온 키워드를 DB에 저장하는 로직 */
    public void savePlaceToDb(String categoryOrAdd){
        /* 전달받은 카테고리 혹은 주소로 해당하는 플레이스 객체 리스트 호출 */
        List<Place>placesList = placeInterface.findByCategory(categoryOrAdd);
        if (placesList.size()==0){
            List<Place>placeList = placeInterface.findByAddress(categoryOrAdd);
        }
        /* 전달받은 플레이스 리스트를 통합 키워드화 */
        String placeKeywords = placeKeywords(placesList);
        /* 키워드 객체에 정보 담기 */
        Keywords place = new Keywords();
        place.setCategory(categoryOrAdd);
        place.setKeywords(placeKeywords);
        /* 저장 로직 수행 */
        keywordsInterface.save(place);
    }

    /* 나온 키워드를 DB에 업데이트하는 로직 */
    public void updatePlaceToDb(String categoryOrAdd){
        /* 전달받은 카테고리 혹은 주소로 해당하는 플레이스 객체 리스트 호출 */
        List<Place>placesList = placeInterface.findByCategory(categoryOrAdd);
        if (placesList.size()==0){
            placesList = placeInterface.findByAddress(categoryOrAdd);
        }
        /* 전달받은 플레이스 리스트를 통합 키워드화 */
        String placeKeywords = placeKeywords(placesList);
        /* 키워드 객체에 정보 담기 */
        Keywords place = new Keywords();
        place.setCategory(categoryOrAdd);
        place.setKeywords(placeKeywords);
        /* 저장 로직 수행 */
        keywordsInterface.update(place);
    }


}

