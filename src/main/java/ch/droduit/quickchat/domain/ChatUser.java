package ch.droduit.quickchat.domain;

import javax.persistence.*;

/**
 * <b>Represents a participant in a chat.</b>
 *
 * @see Chat
 * @see ChatUserRepository
 */
@Entity
@Table(name = "ChatUsers")
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String principalUserName;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public ChatUser() {}

    public ChatUser(String username, String principalUserName, Chat chat) {
        this.username = username;
        this.principalUserName = principalUserName;
        this.chat = chat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrincipalUserName() {
        return principalUserName;
    }

    public void setPrincipalUserName(String principalUserName) {
        this.principalUserName = principalUserName;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", principalUserName='" + principalUserName + '\'' +
                ", chat=" + chat +
                '}';
    }
}
