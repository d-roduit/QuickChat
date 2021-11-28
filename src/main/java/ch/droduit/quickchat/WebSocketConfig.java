package ch.droduit.quickchat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * <b>
 *     Implementation of {@link WebSocketMessageBrokerConfigurer} that
 *     configures the behaviour of the Spring's WebSocket message Broker for the
 *     QuickChat application.
 * </b>
 *
 * <p>
 *     This class is used to define the inbound and outbound routes
 *     that the WebSocket clients can use to exchange messages with
 *     the Spring's WebSocket message Broker.
 * </p>
 *
 * @see WebSocketMessageBrokerConfigurer
 * @see MessageBrokerRegistry
 * @see StompEndpointRegistry
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        config.setPreservePublishOrder(true);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/register-websocket")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

}