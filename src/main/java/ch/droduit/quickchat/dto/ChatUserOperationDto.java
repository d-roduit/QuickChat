package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.ChatUserOperationAction;
import ch.droduit.quickchat.domain.ChatUser;

public record ChatUserOperationDto(ChatUser chatUser, ChatUserOperationAction action) { }