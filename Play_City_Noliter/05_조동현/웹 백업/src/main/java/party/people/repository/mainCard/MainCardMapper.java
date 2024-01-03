package party.people.repository.mainCard;

import org.apache.ibatis.annotations.Mapper;
import party.people.domain.MainCard;

@Mapper
public interface MainCardMapper {
    /* 메인 카드에 뿌릴 정보 로드 */
    MainCard load(String category);
}
