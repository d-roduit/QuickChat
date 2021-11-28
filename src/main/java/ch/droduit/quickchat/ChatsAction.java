package ch.droduit.quickchat;

/**
 * <b>
 *     Represents an operation on a chat
 *     that will be performed by the front-end.
 * </b>
 *
 * <p>
 *     This enumeration is to be used in conjunction with
 *     the {@link ch.droduit.quickchat.dto.ChatsActionDto} class.
 * </p>
 */
public enum ChatsAction {
    CREATE,
    UPDATE,
    DELETE
}
