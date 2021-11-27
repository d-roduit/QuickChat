package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.ChatOperationAction;
import ch.droduit.quickchat.domain.Chat;

public record ChatOperationDto(Chat chat, ChatOperationAction action) { }