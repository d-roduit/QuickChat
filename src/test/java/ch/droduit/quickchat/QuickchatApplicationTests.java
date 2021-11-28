package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.ChatMessageRepository;
import ch.droduit.quickchat.domain.ChatRepository;
import ch.droduit.quickchat.domain.ChatUserRepository;
import ch.droduit.quickchat.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuickchatApplicationTests {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ChatUserRepository chatUserRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertThat(chatRepository).isNotNull();
		assertThat(chatUserRepository).isNotNull();
		assertThat(chatMessageRepository).isNotNull();
		assertThat(userRepository).isNotNull();
	}
}
