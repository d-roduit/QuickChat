package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <b>Test class for {@link ChatRepository}.</b>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatRepositoryTests {

    @Autowired
    private ChatRepository chatRepository;

    private static Chat testChat;

    @Test
    @Order(1)
    public void createTestChat() {
        testChat = new Chat();
        testChat.setName("Test Chat");
        testChat.setCreationDateTime(OffsetDateTime.now());
        testChat.setLastUsernameUsed(0);
        testChat.setUUID(UUID.randomUUID().toString());

        chatRepository.save(testChat);
        assertThat(testChat.getId()).isNotNull();
    }

    @Test
    @Order(2)
    public void findByUUIDShouldReturnCorrectChat() {
        Chat testChatFetchedFromDB = chatRepository.findChatByUUID(testChat.getUUID());
        assertThat(testChatFetchedFromDB).isNotNull();
        assertThat(testChatFetchedFromDB.getName()).isEqualTo("Test Chat");
        assertThat(testChatFetchedFromDB.getLastUsernameUsed()).isEqualTo(0);
    }

    @Test
    @Order(3)
    public void deleteTestChat() {
        chatRepository.delete(testChat);
        Chat testChatFetchedFromDB = chatRepository.findChatByUUID(testChat.getUUID());
        assertThat(testChatFetchedFromDB).isNull();
    }

}
