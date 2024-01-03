package party.people.repository.keywords;

import org.apache.ibatis.annotations.Mapper;
import party.people.domain.Keywords;

import java.util.List;

@Mapper
public interface KeywordsMapper {

    /* 전체 호출 */
    List<Keywords> findAll();

    /* 키워드 처음 저장하는 로직 */
    void save(Keywords keywords);

    /* 키워드를 수정하는 로직 */
    void update(Keywords keywords);

    /* 카테고리로 키워드 가져오기 */
    Keywords findByCategory(String category);
}
