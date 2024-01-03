package party.people.repository.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import party.people.domain.SearchInput;
import party.people.domain.SearchResult;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SearchInputRepository implements SearchInputInterface{
    /* DB에서 가져오기 위해 Mapper 이용 */
    private final SearchInputMapper searchInputMapper;

    /* 이하 인터페이스에서 가져온 기능을 Mapper에 위임 */

    @Override
    public void save(SearchInput searchInput) {
        searchInputMapper.save(searchInput);
    }

    @Override
    public List<SearchResult> loadAll() {
        return searchInputMapper.loadAll();
    }

    @Override
    public SearchResult findBySequenceId(Long sequenceId) {
        return searchInputMapper.findBySequenceId(sequenceId);
    }
}
