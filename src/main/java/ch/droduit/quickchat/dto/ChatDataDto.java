package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.domain.Chat;

/**
 * <b>
 *     Object used as a Data Transfer Object (DTO) to link a chat
 *     to its number of users.
 * </b>
 *
 * <p>
 *     This object is only used to hold values and is used to send
 *     the data it contains to the front-end through a WebSocket connection.
 * </p>
 *
 * @see Chat
 * @see ch.droduit.quickchat.domain.ChatUser
 */
public record ChatDataDto(Chat chat, int nbChatUsers) { }
