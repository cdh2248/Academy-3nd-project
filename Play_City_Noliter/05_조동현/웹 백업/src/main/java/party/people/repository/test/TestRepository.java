package party.people.repository.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import party.people.domain.Test;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TestRepository implements TestInterface {

    private final TestMapper testMapper;

    @Override
    public List<Test> findAll() {
        return testMapper.findAll();
    }
}
