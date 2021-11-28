package ch.droduit.quickchat.helper;

import ch.droduit.quickchat.domain.ChatUser;

import java.util.List;

/**
 * <b>Helper class for sorting a list of chat users.</b>
 *
 * @see ChatUser
 */
public class SortHelper {

    public static void sortOPInChatUsers(List<ChatUser> chatUsersList) {
        int chatUsersListSize = chatUsersList.size();
        if (chatUsersListSize > 0) {
            ChatUser lastChatUserInList = chatUsersList.get(chatUsersListSize - 1);
            if (lastChatUserInList.getUsername().equals("OP")) {
                ChatUser opChatUser = chatUsersList.remove(chatUsersListSize - 1);
                chatUsersList.add(0, opChatUser);
            }
        }
    }
}
