package ch.droduit.quickchat.domain;

import javax.persistence.*;

@Entity
@Table(name = "ChatMessages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public ChatMessage() {}

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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", chat=" + chat +
                '}';
    }
}
