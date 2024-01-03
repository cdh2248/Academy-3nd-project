package party.people;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import party.people.config.Config;

@Import(Config.class)
@SpringBootApplication(scanBasePackages = "party.people.web")
public class PartyPeopleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyPeopleApplication.class, args);
	}
}
