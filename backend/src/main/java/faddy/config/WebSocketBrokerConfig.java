package faddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp protocol 지원 x -> SockJs로 소켓동신 지원
        // stomp 접속 url : /ws
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SocketJS 를 연결한다는 설정

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 바로 broker 호출
        registry.enableSimpleBroker("/sub"); // queue : message 1:1 , topic : message: 1:n

        // broker 호출 전 message 가공을 위한 url
        registry.setApplicationDestinationPrefixes("/pub");
    }


}
