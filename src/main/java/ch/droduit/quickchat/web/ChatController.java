package ch.droduit.quickchat.web;

import ch.droduit.quickchat.ChatsAction;
import ch.droduit.quickchat.domain.*;
import ch.droduit.quickchat.dto.ChatDataDto;
import ch.droduit.quickchat.dto.ChatsActionDto;
import ch.droduit.quickchat.helper.CookieHelper;
import ch.droduit.quickchat.helper.SortHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping({"/", "index"})
    public String index(Model model) {
        model.addAttribute("newChat", new Chat());
        model.addAttribute(
                "chats",
                chatRepository.findAll(Sort.by("creationDateTime").ascending()));
        return "index";
    }

    @PostMapping("create")
    public String createChat(@ModelAttribute Chat newChat, HttpServletResponse response) {
        // Sanitize input
        newChat.setName(newChat.getName().trim());

        // Enforce business logic
        if (newChat.getName().length() > 60 || newChat.getName().isEmpty()) {
            return "redirect:/";
        }

        // Save chat in DB
        String randomUUID = UUID.randomUUID().toString();
        newChat.setUUID(randomUUID);
        chatRepository.save(newChat);

        // Create a username cookie to link the username of the user to the chat he created
        Cookie usernameCookie = CookieHelper.createCookie(
                newChat.getUUID(),
                "OP",
                "/chat/" + newChat.getUUID()
        );
        response.addCookie(usernameCookie);

        List<ChatDataDto> chatDataDtoList = new ArrayList<>();
        chatDataDtoList.add(new ChatDataDto(newChat, 1));
        ChatsActionDto chatsActionDto = new ChatsActionDto(chatDataDtoList, ChatsAction.CREATE);
        simpMessagingTemplate.convertAndSend("/topic/chats", chatsActionDto);

        return "redirect:chat/" + newChat.getUUID();
    }

    @GetMapping("chat/{UUID}")
    public String viewChat(@PathVariable("UUID") String UUID, Model model, HttpServletRequest request, HttpServletResponse response) {
        // Fetch data about chat and chat messages from DB
        Chat chat = chatRepository.findChatByUUID(UUID);
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChat_UUID(UUID);
        List<ChatUser> chatUsersList = chatUserRepository.findAllByChat_IdOrderByUsernameAsc(chat.getId());
        SortHelper.sortOPInChatUsers(chatUsersList);

        model.addAttribute("chat", chat);
        model.addAttribute("chatMessages", chatMessages);
        model.addAttribute("chatUsers", chatUsersList);

        // Check if the user has already a username cookie.
        // If so, use it. Create it otherwise.
        String username = CookieHelper.extractCookie(request, UUID);
        if (username == null) {
            int newLastUsernameUsed = chat.getLastUsernameUsed() + 1;
            chat.setLastUsernameUsed(newLastUsernameUsed);
            chatRepository.save(chat);

            Cookie usernameCookie = CookieHelper.createCookie(
                    UUID,
                    String.valueOf(newLastUsernameUsed),
                    "/chat/" + UUID
            );
            username = usernameCookie.getValue();
            response.addCookie(usernameCookie);
        }
        model.addAttribute("username", username);

        return "chat";
    }

}
