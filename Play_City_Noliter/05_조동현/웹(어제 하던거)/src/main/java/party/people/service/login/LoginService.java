package party.people.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import party.people.domain.Client;
import party.people.repository.client.ClientInterface;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginInterface{

    /* Client Interface 사용을 위해 임포트 */
    private final ClientInterface clientInterface;

    @Override
    public Client login(String clientId, String password) {
        /* 로그인 단에서 ID를 가져와 DB에 데이터가 있는지 확인 및 불러오기 */
        return clientInterface.findByClientId(clientId)
                /* 로그인 단에서 가져온 PASSWORD가 DB에 있는 PASSWORD와 일치하는지 확인 */
                .filter(p->p.getPassword().equals(password))
                /* 일치하면 그 정보를 반환 없으면 NULL값 반환 => 반환 결과는 controller에서 처리 */
                .orElse(null);
    }
}
