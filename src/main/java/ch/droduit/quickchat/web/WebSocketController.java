package ch.droduit.quickchat.web;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatMessage;
import ch.droduit.quickchat.domain.ChatMessageRepository;
import ch.droduit.quickchat.domain.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat/{UUID}")
    public ChatMessage handleChatMessageReception(ChatMessage chatMessage, @DestinationVariable String UUID) {
        chatMessage.setMessage(chatMessage.getMessage().trim());

        Chat chat = chatRepository.findChatByUUID(UUID);
        if (chat != null) {
            chatMessage.setChat(chat);
        }
        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

}