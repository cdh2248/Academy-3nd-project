package party.people.repository.mainCard;

import party.people.domain.MainCard;

public interface MainCardInterface {
    /* 메인 카드에 뿌릴 정보 로드 */
    MainCard load(String category);
}
