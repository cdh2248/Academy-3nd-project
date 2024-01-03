package party.people.repository.keywords;

import party.people.domain.Keywords;

import java.util.List;

public interface KeywordsInterface {

    /* 전체 호출 */
    List<Keywords> findAll();

    /* 통합 키워드 저장하는 로직 */
    void save(Keywords keywords);

    /* 통합 키워드 수정하는 로직 */
    void update(Keywords keywords);

    /* 카테고리로 키워드 가져오기 */
    Keywords findByCategory(String category);
}
