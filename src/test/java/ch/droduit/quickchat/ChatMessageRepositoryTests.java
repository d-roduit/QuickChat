package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatMessage;
import ch.droduit.quickchat.domain.ChatMessageRepository;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <b>Test class for {@link ChatMessageRepository}.</b>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatMessageRepositoryTests {

    @Autowired
    private ChatRepository chatRepository;

    private static Chat chat;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static ChatMessage testChatMessage;

    @Test
    @Order(1)
    public void createTestChatMessage() {
        chat = new Chat();
        chat.setName("Test Chat");
        chat.setCreationDateTime(OffsetDateTime.now());
        chat.setLastUsernameUsed(0);
        chat.setUUID(UUID.randomUUID().toString());
        chatRepository.save(chat);

        testChatMessage = new ChatMessage();
        testChatMessage.setUsername("OP");
        testChatMessage.setMessage("Test message");
        testChatMessage.setSendingDateTime(OffsetDateTime.now());
        testChatMessage.setChat(chat);

        chatMessageRepository.save(testChatMessage);
        assertThat(testChatMessage.getId()).isNotNull();
    }

    @Test
    @Order(2)
    public void findByChat_UUIDShouldReturnChatMessageList() {
        List<ChatMessage> testChatMessageList = chatMessageRepository.findAllByChat_UUID(chat.getUUID());
        assertThat(testChatMessageList).isNotEmpty();
        assertThat(testChatMessageList).hasSize(1);
        assertThat(testChatMessageList.get(0)).isNotNull();
        assertThat(testChatMessageList.get(0).getId()).isEqualTo(testChatMessage.getId());
        assertThat(testChatMessageList.get(0).getUsername()).isEqualTo("OP");
        assertThat(testChatMessageList.get(0).getMessage()).isEqualTo("Test message");
        assertThat(testChatMessageList.get(0).getChat().getId()).isEqualTo(chat.getId());
    }

    @Test
    @Order(3)
    public void deleteTestChatMessage() {
        chatMessageRepository.delete(testChatMessage);
        List<ChatMessage> testChatMessageList = chatMessageRepository.findAllByChat_UUID(chat.getUUID());
        assertThat(testChatMessageList).isEmpty();
    }

}
