package com.university.medvladbe.security.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.medvladbe.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/") || request.getServletPath().equals("/token/refresh")) {
            filterChain.doFilter(request, response);// asta nu face nimic , merge la urmatorul filtru din chain-ul de filtere
        } else {
            String autherizationHeader = request.getHeader(AUTHORIZATION);
            if (autherizationHeader != null && autherizationHeader.startsWith("Bearer ")) {
                try {
                    String token = autherizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(Secrets.passwordKey.getBytes());// secret seteaza-l undeva ascuns
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String role = decodedJWT.getClaim("role").toString();
                    log.info("COX " +role);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }
                catch (com.auth0.jwt.exceptions.TokenExpiredException expiredException){
                    response.setHeader("error",expiredException.getMessage());
                    response.setStatus(401);// unathorized
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message",expiredException.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
                catch (Exception exception) {
                    //TODO:: fa pt diferite cazuri
                    log.error(exception.getClass().getName());
                    log.error("Error logging in: {}",exception.getMessage());
                    response.setHeader("error",exception.getMessage());
                    //response.sendError(403);//forbidden
                    response.setStatus(403);
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message",exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            }
            else{
                filterChain.doFilter(request, response);
            }
        }


    }
}
