package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.User;
import ch.droduit.quickchat.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * <b>SpringBootApplication class for the QuickChat application.</b>
 *
 * <p>
 *     This class runs the QuickChat application and insert in database,
 *     using the {@link #insertData()} method, a {@link User} which can
 *     be used for authentication to the administration area.
 * </p>
 */
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
