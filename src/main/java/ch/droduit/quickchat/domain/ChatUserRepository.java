package ch.droduit.quickchat.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Long> {
    ChatUser findByChat_IdAndUsername(long chatId, String username);
    ChatUser findByPrincipalUserName(String principalUserName);
    List<ChatUser> findAllByChat_IdOrderByUsernameAsc(long chatId);
}
