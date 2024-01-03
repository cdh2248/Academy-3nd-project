package party.people.repository.InviteCard;

import party.people.domain.InviteCard;
import party.people.domain.Keywords;
import party.people.domain.Page;

import java.util.List;

public interface InviteCardInterface {

    /* 저장 */
    void saveCard(InviteCard inviteCard);

    /* clientId로 저장한 정보 로딩 */
    List<InviteCard> loadById(String clientId);

    /* 페이징을 위한 정보 로딩 */
    List<InviteCard> loadByIdPaging(Page page, String clientId);
}
