//package com.university.medvladbe.web;
//
//import com.university.medvladbe.service.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.messaging.*;
//import org.springframework.messaging.simp.stomp.*;
//import org.springframework.messaging.support.*;
//import org.springframework.security.authentication.*;
//import org.springframework.stereotype.*;
//
//import javax.naming.*;
//
//@Component
//public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
//    private static final String USERNAME_HEADER = "login";
//    private static final String PASSWORD_HEADER = "passcode";
//
//    // https://stackoverflow.com/questions/45405332/websocket-authentication-and-authorization-in-spring
//    @Override
//    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
//        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        System.out.println("DA");
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//            System.out.println("ADA");
//            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
//            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
//
//            final UsernamePasswordAuthenticationToken user = authenticationToken;
//
//            accessor.setUser(user);
//        }
//        return message;
//    }
//}
