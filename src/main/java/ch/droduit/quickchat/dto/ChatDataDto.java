package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.domain.Chat;

public record ChatDataDto(Chat chat, int nbChatUsers) { }
