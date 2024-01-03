package party.people.repository.keywords;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import party.people.domain.Keywords;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class KeywordsRepository implements KeywordsInterface {

    /* DB에서 가져오기 위해 Mapper 이용 */
    private final KeywordsMapper keywordsMapper;

    /* 이하 인터페이스에서 가져온 기능을 Mapper에 위임 */
    @Override
    public List<Keywords> findAll() {
        return keywordsMapper.findAll();
    }

    @Override
    public void save(Keywords keywords) {
        keywordsMapper.save(keywords);
    }

    @Override
    public void update(Keywords keywords) {
        keywordsMapper.update(keywords);
    }

    @Override
    public Keywords findByCategory(String category) {
        return keywordsMapper.findByCategory(category);
    }
}
