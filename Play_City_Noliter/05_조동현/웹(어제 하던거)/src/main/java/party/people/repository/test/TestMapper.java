package party.people.repository.test;

import org.apache.ibatis.annotations.Mapper;
import party.people.domain.Test;

import java.util.List;

@Mapper
public interface TestMapper {

    List<Test> findAll();
}
