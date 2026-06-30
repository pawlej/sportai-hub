package pl.sportaihub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
/**
 * Konfiguracja komunikacji WebSocket z wykorzystaniem protokołu STOMP.
 *
 * <p>Klient łączy się z serwerem przez endpoint {@code /ws}
 * i subskrybuje kanał {@code /topic/activity}, na którym publikowane
 * są zdarzenia dotyczące aktywności w aplikacji.</p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}