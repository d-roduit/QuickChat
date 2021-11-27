package ch.droduit.quickchat.web;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatMessage;
import ch.droduit.quickchat.domain.ChatMessageRepository;
import ch.droduit.quickchat.domain.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Arrays;

@Controller
public class WebSocketController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

//    @MessageMapping("/chats")
//    public Chat handleChatCreationDeletion(Chat chat) {
//        return chat;
//    }

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

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Daniel's Disconnect - sessionId : " + event.getSessionId());
    }

    @EventListener
    public void onSubscribeEvent(SessionSubscribeEvent event) {
        String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();
        String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        System.out.println("Daniel's Sub - header : " + event.getMessage().getHeaders());
//        System.out.println("Daniel's Sub - sessionId = " + sessionId + " | destination = " + destination);
    }

}