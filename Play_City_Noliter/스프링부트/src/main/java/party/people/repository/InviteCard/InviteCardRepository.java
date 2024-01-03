package party.people.repository.InviteCard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import party.people.domain.InviteCard;
import party.people.domain.Keywords;
import party.people.domain.Page;
import party.people.repository.keywords.KeywordsInterface;
import party.people.repository.keywords.KeywordsMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InviteCardRepository implements InviteCardInterface {

    /* DB에서 가져오기 위해 Mapper 이용 */
    private final InviteCardMapper inviteCardMapper;

    /* 이하 인터페이스에서 가져온 기능을 Mapper에 위임 */
    @Override
    public void saveCard(InviteCard inviteCard){

        inviteCardMapper.saveCard(inviteCard);
    }

    @Override
    public List<InviteCard> loadById(String clientId) {
        return inviteCardMapper.loadById(clientId);
    }

    @Override
    public List<InviteCard> loadByIdPaging(Page page, String clientId) {
        return inviteCardMapper.loadByIdPaging(page, clientId);
    }
}
