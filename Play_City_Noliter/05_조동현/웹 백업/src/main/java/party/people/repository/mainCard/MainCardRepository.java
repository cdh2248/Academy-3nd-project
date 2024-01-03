package party.people.repository.mainCard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import party.people.domain.MainCard;

@Repository
@RequiredArgsConstructor
public class MainCardRepository implements MainCardInterface {
    private final MainCardMapper mainCardMapper;

    @Override
    public MainCard load(String category) {
        return mainCardMapper.load(category);
    }
}
