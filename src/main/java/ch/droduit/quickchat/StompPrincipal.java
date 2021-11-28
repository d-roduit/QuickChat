package ch.droduit.quickchat;

import java.security.Principal;

/**
 * <b>Represents a STOMP WebSocket connection identifiable by name.</b>
 *
 * <p>
 *     This class is an implementation of the {@link Principal} interface.
 *     It is used in the {@link CustomHandshakeHandler} class to provide
 *     a mean of identification for a WebSocket connection.
 * </p>
 *
 * @see Principal
 * @see CustomHandshakeHandler
 */
public class StompPrincipal implements Principal {

    private final String name;

    StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}