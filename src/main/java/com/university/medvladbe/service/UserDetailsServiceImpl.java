package com.university.medvladbe.service;

import com.university.medvladbe.dto.AdminHistoryDto;
import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.RegistrationResultDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.DefinedRole;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.entity.registration.RegistrationResult;
import com.university.medvladbe.exception.UserNotActive;
import com.university.medvladbe.repository.QuestionRepository;
import com.university.medvladbe.repository.RegistrationResultRepository;
import com.university.medvladbe.repository.RoleRepository;
import com.university.medvladbe.repository.UserRepository;
import com.university.medvladbe.util.UserMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final QuestionRepository questionRepository;
    private final RegistrationResultRepository registrationResultRepository;
    private final PasswordEncoder passwordEncoder;


    public List<QuestionDto> questionListToQuestionDtoList(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question -> {
            questionDtos.add(
                    QuestionDto.builder()
                            .id(question.getId())
                            .userDto(question.getUser().userDtoFromUser())
                            .content(question.getContent())
                            .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                            .build());
        });
        return questionDtos;
    }


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
                .dateOfRegistration(new Date(System.currentTimeMillis()))
                .build();

        userRepository.save(user);
    }

    public void acceptUserRegistration(String adminUsername, String username,
                                       String comment, boolean verdict) {
        User admin = userRepository.findByUsername(adminUsername);
        User user = userRepository.findByUsername(username);
        if (verdict) {
            user.setActive(true);
            RegistrationResult registrationResult = RegistrationResult.builder()
                    .user(user)
                    .admin(admin)
                    .comment(comment)
                    .verdict(true)
                    .build();
            registrationResultRepository.save(registrationResult);
        } else {
            userRepository.delete(user);
        }
    }

    public void deleteUser(String username){
        userRepository.delete(userRepository.findByUsername(username));
    }

    public void updateFirstName(String username, String firstName){
        User user = userRepository.findByUsername(username);
        user.setFirstName(firstName);
        userRepository.save(user);
    }
    public void updateLastName(String username, String lastName){
        User user = userRepository.findByUsername(username);
        user.setLastName(lastName);
        userRepository.save(user);
    }
    public void updateProfilePicture(String username, String profilePicture){
        User user = userRepository.findByUsername(username);
        user.setProfilePicture(profilePicture);
        userRepository.save(user);
    }

    public List<UserDto> getInactiveUsers() {
        List<User> inactiveUsers = userRepository.findUserByActiveFalse();
        return UserMethods.userListToUserDtoList(inactiveUsers);
    }
    public AdminHistoryDto getAdminHistory(String adminUsername){
        List <RegistrationResult> registrationResults = registrationResultRepository.findAllByAdmin_Username(adminUsername);
        List <Question> questions = questionRepository.getQuestionByCheckedTrueAndAdmin_Username(adminUsername);
        List <RegistrationResultDto> registrationResultDtos = new ArrayList<>();
        registrationResults.forEach(registrationResult -> {
            registrationResultDtos.add(registrationResult.registrationResultToDto());
        });
        List <QuestionDto> questionDtos = questionListToQuestionDtoList(questions);
        return AdminHistoryDto.builder().questions(questionDtos).registrationResultList(registrationResultDtos).build();
    }

    public UserDto getInactiveDoctors() {
        Role role = roleRepository.findRoleByName(DefinedRole.DOCTOR);
        try {
            return userRepository.findFirstByActiveFalseAndRole(role).userDtoFromUser();
        }catch (Exception e){
            return UserDto.builder().build();
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        } else if (!user.isActive()) {
            throw new UserNotActive();
        } else {
            log.info("User found in database");
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            log.info(user.getRole().toString());
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName().toString()));
            log.info("Password:" + user.getPassword());

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
