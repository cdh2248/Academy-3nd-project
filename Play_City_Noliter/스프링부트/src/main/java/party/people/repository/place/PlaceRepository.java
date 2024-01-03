package party.people.repository.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import party.people.domain.Place;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlaceRepository implements PlaceInterface{

    /* DB에서 가져오기 위해 Mapper 이용 */
    private final PlaceMapper placeMapper;

    /* 이하 인터페이스에서 가져온 기능을 Mapper에 위임 */
    @Override
    public List<Place> findAll() {
        return placeMapper.findAll();
    }

    @Override
    public List<Place> findByCategory(String category) {
        return placeMapper.findByCategory(category);
    }

    @Override
    public List<Place> findByTitle(String title) {
        return placeMapper.findByTitle(title);
    }

    @Override
    public List<Place> findByKeyword(String keyword) {
        return placeMapper.findByKeyword(keyword);
    }

    @Override
    public List<Place> findByKeyword(List<String> keywords) {
        return placeMapper.findByKeyword(keywords);
    }

    @Override
    public List<Place> findByAddress(String address) {
        return placeMapper.findByAddress(address);
    }

    @Override
    public void updateKeyword(String title, String keyword) {
        placeMapper.updateKeyword(title, keyword);
    }

    @Override
    public List<Place> randon3() {
        return placeMapper.randon3();
    }

    @Override
    public Place idSearch(Integer id){
        return placeMapper.idSearch(id);
    }
}
