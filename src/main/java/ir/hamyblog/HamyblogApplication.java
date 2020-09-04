package ir.hamyblog;

import ir.hamyblog.entities.User;
import ir.hamyblog.model.Role;
import ir.hamyblog.model.UserRegisterIn;
import ir.hamyblog.services.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HamyblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(HamyblogApplication.class, args);
	}

	@Autowired
	private UserService userService;

	@Bean
	InitializingBean initializingBean() {
		return () -> {
			userService.addUser(new UserRegisterIn("ehsan", "password", "ehsan maddahi"));
			userService.addUser(new UserRegisterIn("omid", "passwd", "omid mahmodi"));
			userService.updateRole("omid", Role.ADMIN);
		};
	}

}
