package ch.droduit.quickchat.dto;

import ch.droduit.quickchat.ChatsAction;

import java.util.List;

public record ChatsActionDto(List<ChatDataDto> chatDataDtoList, ChatsAction action) { }