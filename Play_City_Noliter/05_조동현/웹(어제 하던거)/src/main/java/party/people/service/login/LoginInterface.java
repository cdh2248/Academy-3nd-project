package party.people.service.login;

import party.people.domain.Client;

public interface LoginInterface {

    /* 로그인 서비스 구현을 위한 인터페이스 ID와 PASSWORD 사용 */
    Client login(String clientId, String password);
}
