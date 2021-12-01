//package com.university.medvladbe.web;
//
//import com.auth0.jwt.*;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.*;
//import com.auth0.jwt.interfaces.*;
//import com.university.medvladbe.util.*;
//import lombok.extern.slf4j.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.*;
//import org.springframework.messaging.*;
//import org.springframework.messaging.simp.config.*;
//import org.springframework.messaging.simp.stomp.*;
//import org.springframework.messaging.support.*;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.*;
//import org.springframework.security.core.authority.*;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.support.*;
//
//import javax.servlet.http.*;
//import java.security.*;
//import java.util.*;
//
//@Configuration
//@Slf4j
//@EnableWebSocketMessageBroker
//public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {
//    @Autowired
//    private AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
//
//    @Override
//    public void configureClientInboundChannel(final ChannelRegistration registration) {
//        registration.setInterceptors(authChannelInterceptorAdapter);
//    }
//
//    @Override
//    public void registerStompEndpoints(final StompEndpointRegistry registry){
//        registry.addEndpoint("/chat").setAllowedOriginPatterns("https://localhost:3000")
//                .setHandshakeHandler(new DefaultHandshakeHandler() {
//
//                    @Override
//                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                        //NU SE APELEAZA
//                        // AICI TREBUIE SA SETEZI Principalul
//                        System.out.println(request.getURI().getQuery());
//                        String username = UserMethods.getCurrentUsername();
//                        System.out.println(" username determinat : "+username);
//                        return super.determineUser(request, wsHandler, attributes);
//                    }
//
//                    public boolean beforeHandshake(
//                    ServerHttpRequest request,
//                    ServerHttpResponse response,
//                    WebSocketHandler wsHandler,
//                    Map attributes) throws Exception {
//
//                if (request instanceof ServletServerHttpRequest) {
//                    System.out.println("ADA");
//                    ServletServerHttpRequest servletRequest
//                            = (ServletServerHttpRequest) request;
//                    HttpSession session = servletRequest
//                            .getServletRequest().getSession();
//                    attributes.put("sessionId", session.getId());
//                }
//                return true;
//            }}).withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(final MessageBrokerRegistry registry){
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic/", "/queue/");
//    }
//
//}
