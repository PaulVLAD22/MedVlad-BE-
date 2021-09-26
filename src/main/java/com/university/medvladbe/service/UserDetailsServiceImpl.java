package com.university.medvladbe.service;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.exception.BadLogin;
import com.university.medvladbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(String username){
        return userRepository.findByUsername(username);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        } else {
            log.info("User found in database");
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            log.info(user.getRoles().toString());
            user.getRoles().forEach(role ->{
                authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
            });

            log.info("Password:"+user.getPassword());
            // eu am u nsingur role , daca nu te descurci schimba la Lista
            //TODO:: MEREU SPUNE CA PAROLA E GRESITA , REZOLVA
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
