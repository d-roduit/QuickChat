package ch.droduit.quickchat.domain;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ChatMessages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition="TEXT")
    private String message;

    @Generated(GenerationTime.INSERT)
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "timestamp with time zone DEFAULT now()")
    private OffsetDateTime sendingDateTime;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public ChatMessage() {}

    public ChatMessage(String username, String message, OffsetDateTime sendingDateTime, Chat chat) {
        this.username = username;
        this.message = message;
        this.sendingDateTime = sendingDateTime;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getSendingDateTime() {
        return sendingDateTime;
    }

    public void setSendingDateTime(OffsetDateTime sendingDateTime) {
        this.sendingDateTime = sendingDateTime;
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
                ", message='" + message + '\'' +
                ", sendingDateTime=" + sendingDateTime +
                ", chat=" + chat +
                '}';
    }
}
