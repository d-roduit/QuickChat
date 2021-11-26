package ch.droduit.quickchat.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findChatByUUID(String UUID);
    List<Chat> findAll(Sort sort);
}
