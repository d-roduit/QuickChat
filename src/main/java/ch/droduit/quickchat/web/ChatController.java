package ch.droduit.quickchat.web;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatMessage;
import ch.droduit.quickchat.domain.ChatMessageRepository;
import ch.droduit.quickchat.domain.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping({"/", "index"})
    public String index(Model model) {
        model.addAttribute("newChat", new Chat());
        model.addAttribute(
                "chats",
                chatRepository.findAll(Sort.by("creationDateTime").descending()));
        return "index";
    }

    @GetMapping("tryChat")
    public String tryChat() { return "try_chat"; }

    @PostMapping("create")
    public String createChat(@ModelAttribute Chat newChat, HttpServletResponse response) {
        // Sanitize input
        newChat.setName(newChat.getName().trim());

        // Enforces business logic
        if (newChat.getName().length() > 60) {
            return "redirect:/";
        }

        // Saves chat in DB
        String randomUUID = UUID.randomUUID().toString();
        newChat.setUUID(randomUUID);
        chatRepository.save(newChat);

        // Create a username cookie to link the username of the user to the chat he created
        Cookie usernameCookie = createCookie(
                newChat.getUUID(),
                "OP",
                "/chat/" + newChat.getUUID()
        );
        response.addCookie(usernameCookie);

        return "redirect:chat/" + newChat.getUUID();
    }

    @GetMapping("chat/{UUID}")
    public String viewChat(@PathVariable("UUID") String UUID, Model model, HttpServletRequest request, HttpServletResponse response) {
        // Fetch data about chat and chat messages from DB
        Chat chat = chatRepository.findChatByUUID(UUID);
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChat_UUID(UUID);
        model.addAttribute("chat", chat);
        model.addAttribute("chatMessages", chatMessages);

        // Check if the user has already a username cookie.
        // If so, use it. Create it otherwise.
        String username = extractCookie(request, UUID);
        if (username == null) {
            int newLastUsernameUsed = chat.getLastUsernameUsed() + 1;
            chat.setLastUsernameUsed(newLastUsernameUsed);
            chatRepository.save(chat);

            Cookie usernameCookie = createCookie(
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

    private String extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie.getValue();
        }
        return null;
    }

    private Cookie createCookie(String UUID, String value, String path) {
        Cookie cookie = new Cookie(UUID, value);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath(path);
        return cookie;
    }

}
