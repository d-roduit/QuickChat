package ch.droduit.quickchat;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatRepository;
import ch.droduit.quickchat.domain.ChatUser;
import ch.droduit.quickchat.domain.ChatUserRepository;
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
 * <b>Test class for {@link ChatUserRepository}.</b>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatUserRepositoryTests {

    @Autowired
    private ChatRepository chatRepository;

    private static Chat chat;

    @Autowired
    private ChatUserRepository chatUserRepository;

    private static ChatUser testChatUser;

    @Test
    @Order(1)
    public void createTestChatUser() {
        chat = new Chat();
        chat.setName("Test Chat");
        chat.setCreationDateTime(OffsetDateTime.now());
        chat.setLastUsernameUsed(0);
        chat.setUUID(UUID.randomUUID().toString());
        chatRepository.save(chat);

        testChatUser = new ChatUser();
        testChatUser.setUsername("OP");
        testChatUser.setPrincipalUserName(UUID.randomUUID().toString());
        testChatUser.setChat(chat);

        chatUserRepository.save(testChatUser);
        assertThat(testChatUser.getId()).isNotNull();
    }

    @Test
    @Order(2)
    public void findAllByChat_IdOrderByUsernameAscShouldReturnChatUserList() {
        List<ChatUser> testChatUserList = chatUserRepository.findAllByChat_IdOrderByUsernameAsc(chat.getId());
        assertThat(testChatUserList).isNotEmpty();
        assertThat(testChatUserList).hasSize(1);
        assertThat(testChatUserList.get(0)).isNotNull();
        assertThat(testChatUserList.get(0).getId()).isEqualTo(testChatUser.getId());
        assertThat(testChatUserList.get(0).getUsername()).isEqualTo("OP");
        assertThat(testChatUserList.get(0).getChat().getId()).isEqualTo(chat.getId());
    }

    @Test
    @Order(3)
    public void deleteTestChatUser() {
        chatUserRepository.delete(testChatUser);
        List<ChatUser> testChatUserList = chatUserRepository.findAllByChat_IdOrderByUsernameAsc(chat.getId());
        assertThat(testChatUserList).isEmpty();
    }

}
