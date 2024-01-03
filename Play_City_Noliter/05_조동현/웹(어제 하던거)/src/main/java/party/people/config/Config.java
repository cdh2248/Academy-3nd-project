package party.people.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import party.people.repository.client.ClientInterface;
import party.people.repository.client.ClientMapper;
import party.people.repository.client.ClientRepository;
import party.people.repository.keywords.KeywordsInterface;
import party.people.repository.keywords.KeywordsMapper;
import party.people.repository.keywords.KeywordsRepository;
import party.people.repository.place.PlaceInterface;
import party.people.repository.place.PlaceMapper;
import party.people.repository.place.PlaceRepository;
import party.people.repository.search.SearchInputInterface;
import party.people.repository.search.SearchInputMapper;
import party.people.repository.search.SearchInputRepository;
import party.people.repository.test.TestInterface;
import party.people.repository.test.TestMapper;
import party.people.repository.test.TestRepository;
import party.people.service.keyword.KeywordInSomething;
import party.people.service.keyword.KeywordsMerge;
import party.people.service.login.LoginInterface;
import party.people.service.login.LoginService;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final TestMapper testMapper;
    private final ClientMapper clientMapper;
    private final PlaceMapper placeMapper;
    private final KeywordsMapper keywordsMapper;
    private final SearchInputMapper searchInputMapper;


    /* DB접속 TEST용 BEAN */
    @Bean
    public TestInterface testInterface(){
        return new TestRepository(testMapper);
    }

    /* 회원관리용 BEAN */
    @Bean
    public ClientInterface clientInterface(){
        return new ClientRepository(clientMapper);
    }

    /* 로그인용 BEAN -> 클라이언트 인터페이스와 연결 */
    @Bean
    public LoginInterface loginInterface(){
        return new LoginService(clientInterface());
    }



    /* Place 검색용 BEAN */
    @Bean
    public PlaceInterface placeInterface() {
        return new PlaceRepository(placeMapper);
    }

    /* 서비스를 인터페이스와 연결하는 BEAN */
    @Bean
    public KeywordsMerge keywordsMerge() {
        return new KeywordsMerge(clientInterface(), placeInterface(), keywordsInterface());
    }

    @Bean
    public KeywordInSomething keywordInSomething(){
        return new KeywordInSomething(placeInterface());
    }


    /* Keywords 입력용 BEAN */
    @Bean
    public KeywordsInterface keywordsInterface() {
        return new KeywordsRepository(keywordsMapper);
    }

    /* 검색겨로가 입출력용 Bean */
    @Bean
    public SearchInputInterface searchInputInterface() {
        return new SearchInputRepository(searchInputMapper);
    }
}
