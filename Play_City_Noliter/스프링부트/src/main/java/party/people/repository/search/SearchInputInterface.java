package party.people.repository.search;

import party.people.domain.SearchInput;
import party.people.domain.SearchResult;

import java.util.List;

public interface SearchInputInterface {


    /* 검색어를 검색어 입력 데이터 테이블에 입력하는 함수 */
    void save(SearchInput searchInput);

    /* 검색어 입력 후 결과를 받아보기 위해 검색결과 테이블을 조회하는 함수 */
    List<SearchResult> loadAll();

    /* 검색 결과 테이블에서 시퀀스 id로 정보를 가져오는 함수 */
    SearchResult findBySequenceId(Long sequenceId);
}
