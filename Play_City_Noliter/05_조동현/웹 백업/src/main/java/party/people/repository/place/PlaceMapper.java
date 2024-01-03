package party.people.repository.place;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import party.people.domain.Place;

import java.util.List;

@Mapper
public interface PlaceMapper {

    /* 모든 정보 가져오기 일단..*/
    List<Place>findAll();

    /* 해당 카테고리에 해당하는 리스트 불러오기 */
    List<Place>findByCategory(String category);

    /* 해당 상호명에 해당하는 리스트 불러오기 */
    List<Place>findByTitle(String title);

    /* 해당 키워드가 포함되어 있는 리스트 불러오기 */
    List<Place>findByKeyword(@Param("keywords") String keyword);
    /* 키워드를 리스트로 불러오는거 오버로딩*/
    List<Place>findByKeyword(@Param("keywords") List<String> keywords);


    /* 주소로 검색 (ex) xx구로 검색 */
    List<Place>findByAddress(String address);

    /* 키워드를 새로 갱신! */
    void updateKeyword(@Param("title") String title, @Param("keyword") String keyword);

    List<Place>randon3();

    Place idSearch(Integer id);

}
