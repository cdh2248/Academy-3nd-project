package party.people.repository.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import party.people.domain.Client;
import party.people.web.controller.client.formAndDto.ClientUpdateDto;
import party.people.web.controller.client.formAndDto.FindPwForm;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ClientRepository implements ClientInterface{

    /* DB에서 가져오기 위해 Mapper 이용 */
    private final ClientMapper clientMapper;

    /* 이하 인터페이스에서 가져온 기능을 Mapper에 위임 */

    @Override
    public void save(Client client) {
        clientMapper.save(client);
    }

    @Override
    public List<Client> findAll() {
        return clientMapper.findAll();
    }

    @Override
    public Optional<Client> findBySequenceId(Long sequenceId) {
        return clientMapper.findBySequenceId(sequenceId);
    }

    @Override
    public Optional<Client> findByClientId(String clientId) {
        log.info("findByClientId] "+clientId);
        return clientMapper.findByClientId(clientId);
    }

    @Override
    public Optional<Client> findByClientEmail(String clientEmail) {
        return clientMapper.findByClientEmail(clientEmail);
    }

    @Override
    public Optional<Client> findPassword(FindPwForm form) {
        return clientMapper.findPassword(form);
    }

    @Override
    public void update(String clientId, ClientUpdateDto clientUpdateParam) {
        clientMapper.update(clientId, clientUpdateParam);
    }

    @Override
    public void quit(Client client) {
        clientMapper.quit(client);
    }
}
