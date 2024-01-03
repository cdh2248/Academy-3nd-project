package party.people.repository.InviteCard;

import org.apache.ibatis.annotations.Mapper;
import party.people.domain.InviteCard;
import party.people.domain.Keywords;

import java.util.List;

@Mapper
public interface InviteCardMapper {

    /* 저장 */
    void saveCard(InviteCard inviteCard);

    /* clientId로 저장한 정보 로딩 */
    List<InviteCard> loadById(String clientId);

}
