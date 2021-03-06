package ch.droduit.quickchat.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>Provides access to chat messages stored in database.</b>
 *
 * @see ChatMessage
 */
@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChat_UUID(String UUID);
}
