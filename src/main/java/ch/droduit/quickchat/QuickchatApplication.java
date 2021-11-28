package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.User;
import ch.droduit.quickchat.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuickchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickchatApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Bean
	public CommandLineRunner insertData() {
		return (args) -> {
			userRepository.save(new User(
					"admin@quickchat.com",
					"$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C",
					"ADMIN"));
		};
	}
}
