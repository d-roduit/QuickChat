package ch.droduit.quickchat.web;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatMessage;
import ch.droduit.quickchat.domain.ChatMessageRepository;
import ch.droduit.quickchat.domain.ChatRepository;
import ch.droduit.quickchat.helper.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebSocketController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat/{UUID}")
    public ChatMessage handleChatMessageReception(ChatMessage chatMessage, @DestinationVariable String UUID) {
        Chat chat = chatRepository.findChatByUUID(UUID);
        if (chat != null) {
            chatMessage.setChat(chat);
        }
        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

}