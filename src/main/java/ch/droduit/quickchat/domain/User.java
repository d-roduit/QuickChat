package ch.droduit.quickchat.domain;

import javax.persistence.*;

/**
 * <b>Represents a means of authentication to access the administration dashboard.</b>
 * <p>
 *     This user is in no way linked to the operation of the chats.
 *     It is ONLY a means of authentication to access the administration dashboard.
 * </p>
 *
 * @see UserRepository
 */
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    public User() {}

    public User(String email, String passwordHash, String role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}