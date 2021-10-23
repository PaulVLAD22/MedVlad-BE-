package com.university.medvladbe.service;

import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.DefinedRole;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.registration.RegistrationResult;
import com.university.medvladbe.exception.BadLogin;
import com.university.medvladbe.exception.UserNotActive;
import com.university.medvladbe.repository.RegistrationResultRepository;
import com.university.medvladbe.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final RegistrationResultRepository registrationResultRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String email, String username,
                             String password, String role, String licensePicture) {

        if (licensePicture.equals(""))
            licensePicture = null;

        Role userRole = roleRepository.findRoleByName(DefinedRole.valueOf(role));

        User user = User.builder()
                .password(passwordEncoder.encode(password))
                .username(username)
                .email(email)
                .role(userRole)
                .licensePicture(licensePicture)
                .build();

        userRepository.save(user);
    }
    //TODO:: de testat
    public void acceptUserRegistration(String adminUsername, String username,
                                       String comment, boolean verdict){
        User admin = userRepository.findByUsername(adminUsername);
        User user = userRepository.findByUsername(username);
        if (verdict){
            user.setActive(true);
            RegistrationResult registrationResult = RegistrationResult.builder()
                    .user(user)
                    .admin(admin)
                    .comment(comment)
                    .verdict(true)
                    .build();
            registrationResultRepository.save(registrationResult);
        }
        else{
            userRepository.delete(user);
        }

    }

    public List<UserDto> getInactiveUsers(){
        List <User> inactiveUsers = userRepository.findUserByActiveFalse();
        List <UserDto> inactiveUsersDtos = new ArrayList<>();
        inactiveUsers.stream().
                filter(inactiveUser -> !inactiveUser.getRole().getName().toString().equals("DOCTOR")).
                forEach(inactiveUser -> {inactiveUsersDtos.add(inactiveUser.userDtoFromUser());});
        return inactiveUsersDtos;
    }
    public UserDto getInactiveDoctors(){
        List <User> inactiveUsers = userRepository.findUserByActiveFalse();
        inactiveUsers.forEach(System.out::println);
        List <UserDto> inactiveUsersDtos = new ArrayList<>();
        inactiveUsers.stream().
                filter(inactiveUser -> inactiveUser.getRole().getName().toString().equals("DOCTOR")).
                forEach(inactiveUser -> {inactiveUsersDtos.add(inactiveUser.userDtoFromUser());});

        // verifica daca il da pe primujl inscris
        return inactiveUsersDtos.get(0);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        else if (!user.isActive()){
            throw new UserNotActive();
        }
        else {
            log.info("User found in database");
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            log.info(user.getRole().toString());
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName().toString()));
            log.info("Password:" + user.getPassword());

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
