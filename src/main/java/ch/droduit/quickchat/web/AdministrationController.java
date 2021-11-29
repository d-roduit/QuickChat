package ch.droduit.quickchat.web;

import ch.droduit.quickchat.domain.Chat;
import ch.droduit.quickchat.domain.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <b>
 *     Controller class to manage all HTTP requests
 *     related to the administration area.
 * </b>
 *
 * @see ch.droduit.quickchat.domain.User
 */
@Controller
public class AdministrationController {

    @Autowired
    private ChatRepository chatRepository;

    @RequestMapping("admin")
    public String login() {
        // Deny access to the login page to the users already logged in.
        // Redirect them to the dashboard view instead.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "admin-login";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("logout")
    public String logout() {
        return "admin-login";
    }

    @GetMapping("admin/dashboard")
    public String adminDashboard(Model model) {
        List<Chat> chats = chatRepository.findAll(Sort.by("id").ascending());
        model.addAttribute("chats", chats);
        return "admin-dashboard";
    }
}
