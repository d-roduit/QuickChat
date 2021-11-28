package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.ChatsAction;
import ch.droduit.quickchat.domain.Chat;

import java.util.List;

/**
 * <b>
 *     Object used as a Data Transfer Object (DTO) to link an action {@link ChatsAction}
 *     to a list of {@link ChatDataDto}.
 * </b>
 *
 * <p>
 *     This object is only used to hold values and is used to send
 *     the data it contains to the front-end through a WebSocket connection.
 * </p>
 *
 * @see ChatsAction
 * @see ChatDataDto
 * @see Chat
 */
public record ChatsActionDto(List<ChatDataDto> chatDataDtoList, ChatsAction action) { }