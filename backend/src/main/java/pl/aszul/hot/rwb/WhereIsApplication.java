package pl.aszul.hot.rwb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class WhereIsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WhereIsApplication.class, args);
	}

/*	@Bean
	public CommandLineRunner demo(StudentRepository repository, CourseRepository crepository, SUserRepository urepository) {
		return (args) -> {

			// Create users with BCrypt encoded password (user/user, admin/admin)
			SUser SUser1 = new SUser("user", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "USER");
			SUser SUser2 = new SUser("admin", "$2a$08$bCCcGjB03eulCWt3CY0AZew2rVzXFyouUolL5dkL/pBgFkUH9O4J2", "ADMIN");
			urepository.save(SUser1);
			urepository.save(SUser2);
		};
	}*/

}
