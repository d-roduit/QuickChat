package ch.droduit.quickchat.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>Provides access to chats stored in database.</b>
 *
 * @see Chat
 */
@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findChatByUUID(String UUID);
    List<Chat> findAll(Sort sort);
}
