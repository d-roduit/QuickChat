package ch.droduit.quickchat.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>Provides access to users stored in database.</b>
 *
 * @see User
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}