package party.people.repository.place;

import party.people.domain.Place;

import java.util.List;

public interface PlaceInterface {

    /* 모든 정보 가져오기 일단..*/
    List<Place>findAll();

    /* 해당 카테고리에 해당하는 리스트 불러오기 */
    List<Place>findByCategory(String category);

    /* 해당 상호명에 해당하는 리스트 불러오기 */
    List<Place>findByTitle(String title);

    /* 해당 키워드가 포함되어 있는 리스트 불러오기 */
    List<Place>findByKeyword(String keyword);

    /* 키워드를 리스트로 불러오는거 오버로딩*/
    List<Place>findByKeyword(List<String>keywords);


    /* 주소로 검색 (ex) xx구로 검색 */
    List<Place>findByAddress(String address);

    /* 키워드를 새로 갱신! */
    void updateKeyword(String title,String keyword);

    List<Place>randon3();

    Place idSearch(Integer id);

}
