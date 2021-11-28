package ch.droduit.quickchat;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * <b>
 *     Implementation of {@link DefaultHandshakeHandler} which
 *     add the assignment of a random UUID to each WebSocket connection.
 * </b>
 *
 * <p>
 *     The assignment of a random UUID to each connection is done
 *     to ensure the traceability of the clients during the WebSocket
 *     connection life.
 * </p>
 *
 * @see DefaultHandshakeHandler
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // Generate principal with UUID as name
        return new StompPrincipal(UUID.randomUUID().toString());
    }

}