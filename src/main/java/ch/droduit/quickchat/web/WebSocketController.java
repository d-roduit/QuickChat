package ch.droduit.quickchat.web;

import ch.droduit.quickchat.ChatsAction;
import ch.droduit.quickchat.domain.*;
import ch.droduit.quickchat.dto.ChatDataDto;
import ch.droduit.quickchat.dto.ChatsActionDto;
import ch.droduit.quickchat.helper.SortHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{UUID}/messages")
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
    public void onSubscribeEvent(SessionSubscribeEvent event) {
        String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        String username = SimpMessageHeaderAccessor.wrap(event.getMessage()).getFirstNativeHeader("username");
        Principal principalUser = SimpMessageHeaderAccessor.wrap(event.getMessage()).getUser();

        if (destination != null) {
            if (destination.startsWith("/topic/chat/") && destination.endsWith("/messages")) {
                String chatUUID = destination.replaceFirst("/topic/chat/", "").replaceFirst("/messages", "");
                Chat chat = chatRepository.findChatByUUID(chatUUID);

                if (chat != null) {
                    ChatUser chatUser = chatUserRepository.findByChat_IdAndUsername(chat.getId(), username);
                    boolean isUserAlreadyConnectedOnce = chatUser != null;

                    if (principalUser != null) {
                        if (isUserAlreadyConnectedOnce) {
                            // Redirect to the home page
                            simpMessagingTemplate.convertAndSendToUser(
                                    principalUser.getName(),
                                    "/topic/redirect",
                                    "{\"action\": \"REDIRECT\", \"location\": \"/\"}"
                            );
                            return;
                        }

                        ChatUser newChatUser = new ChatUser(username, principalUser.getName(), chat);
                        chatUserRepository.save(newChatUser);

                        List<ChatUser> chatUsersList = chatUserRepository.findAllByChat_IdOrderByUsernameAsc(chat.getId());
                        SortHelper.sortOPInChatUsers(chatUsersList);

                        simpMessagingTemplate.convertAndSend(
                                "/topic/chat/" + chat.getUUID() + "/users",
                                chatUsersList
                        );

                        List<ChatDataDto> chatDataDtoList = new ArrayList<>();
                        chatDataDtoList.add(new ChatDataDto(chat, chatUsersList.size()));
                        ChatsActionDto chatsActionDto = new ChatsActionDto(chatDataDtoList, ChatsAction.UPDATE);
                        simpMessagingTemplate.convertAndSend("/topic/chats", chatsActionDto);
                    }
                }
            } else if (destination.equals("/topic/chats")) {
                List<Chat> chats = chatRepository.findAll(Sort.by("creationDateTime").ascending());
                List<ChatDataDto> chatDataDtoList = new ArrayList<>();
                for (Chat chat: chats) {
                    int nbChatUsers = chatUserRepository.countByChat_Id(chat.getId());
                    chatDataDtoList.add(new ChatDataDto(chat, nbChatUsers));
                }
                ChatsActionDto chatsActionDto = new ChatsActionDto(chatDataDtoList, ChatsAction.CREATE);
                simpMessagingTemplate.convertAndSendToUser(
                        principalUser.getName(),
                        "/topic/chats",
                        chatsActionDto
                );
            }
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        Principal principalUser = SimpMessageHeaderAccessor.wrap(event.getMessage()).getUser();

        if (principalUser != null) {
            ChatUser chatUser = chatUserRepository.findByPrincipalUserName(principalUser.getName());
            boolean wasChatUserConnectedToChat = chatUser != null;

            if (wasChatUserConnectedToChat) {
                Chat chat = chatUser.getChat();
                chatUserRepository.delete(chatUser);
                List<ChatUser> chatUsersList = chatUserRepository.findAllByChat_IdOrderByUsernameAsc(chat.getId());
                List<ChatDataDto> chatDataDtoList = new ArrayList<>();

                if (chatUsersList.isEmpty()) {
                    chatRepository.delete(chat);

                    chatDataDtoList.add(new ChatDataDto(chat, 0));
                    ChatsActionDto chatsActionDto = new ChatsActionDto(chatDataDtoList, ChatsAction.DELETE);
                    simpMessagingTemplate.convertAndSend("/topic/chats", chatsActionDto);
                    return;
                }

                SortHelper.sortOPInChatUsers(chatUsersList);

                // Send message to update the online users on chat page
                simpMessagingTemplate.convertAndSend(
                        "/topic/chat/" + chat.getUUID() + "/users",
                        chatUsersList
                );

                // Send message to update the nb of online users on index page
                chatDataDtoList.add(new ChatDataDto(chat, chatUsersList.size()));
                ChatsActionDto chatsActionDto = new ChatsActionDto(chatDataDtoList, ChatsAction.UPDATE);
                simpMessagingTemplate.convertAndSend("/topic/chats", chatsActionDto);
            }
        }
    }


}