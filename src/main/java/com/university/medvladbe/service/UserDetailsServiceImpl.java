package com.university.medvladbe.service;

import com.university.medvladbe.dto.AdminHistoryDto;
import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.RegistrationResultDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.DefinedRole;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.ban.BanRecord;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.entity.registration.RegistrationResult;
import com.university.medvladbe.exception.EmailOrUsernameAlreadyTaken;
import com.university.medvladbe.exception.UserNotActive;
import com.university.medvladbe.repository.*;
import com.university.medvladbe.repository.Questions.QuestionRepository;
import com.university.medvladbe.util.UserMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private final BanRecordRepository banRecordRepository;
    private final OtcService otcService;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;




    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public List<QuestionDto> questionListToQuestionDtoList(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question -> {
            questionDtos.add(
                    QuestionDto.builder()
                            .id(question.getId())
                            .userDto(question.getUser().userDtoFromUser())
                            .content(question.getContent())
                            .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                            .comment(question.getComment())
                            .verdict(question.isVerdict())
                            .postingDate(question.getPostingDate())
                            .build());
        });
        return questionDtos;
    }


    public void registerUser(String email, String username,
                             String password, String role, String licensePicture) {

        if (licensePicture.equals(""))
            licensePicture = null;

        Role userRole = roleRepository.findRoleByName(DefinedRole.valueOf(role));

        List<User> users = userRepository.findAll();
        List<String> emails = new ArrayList<>();
        List<String> usernames = new ArrayList<>();

        users.forEach(user -> {
            emails.add(user.getEmail());
            usernames.add(user.getUsername());
        });
        if (emails.contains(email) || usernames.contains(username)) {
            throw new EmailOrUsernameAlreadyTaken();
        }


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
        // Asta dureaza mult rau pt ca trimite email
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
            emailService.sendTextEmail(user.getEmail(), "Welcome to Medvlad", "Your account has just been created");
        } else {
            userRepository.delete(user);
            emailService.sendTextEmail(user.getEmail(), "Account Deletion", "Your account has just been deleted by admin " + adminUsername);
        }
    }

    public void acceptDoctorRegistration(String adminUsername, String username, String firstName,
                                         String lastName, String comment, boolean verdict) {
        User admin = userRepository.findByUsername(adminUsername);
        User user = userRepository.findByUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (verdict) {
            user.setActive(true);
            RegistrationResult registrationResult = RegistrationResult.builder()
                    .user(user)
                    .admin(admin)
                    .comment(comment)
                    .verdict(true)
                    .build();
            registrationResultRepository.save(registrationResult);
            userRepository.save(user);
            emailService.sendTextEmail(user.getEmail(), "Welcome to Medvlad", "Your account has just been created");

        } else {
            userRepository.delete(user);
            emailService.sendTextEmail(user.getEmail(), "Account Deletion", "Your account has just been deleted by admin " + adminUsername);
        }
    }

    public void deleteUser(String adminUsername, String username, String comment) {
        User admin = userRepository.findByUsername(adminUsername);
        User bannedUser = userRepository.findByUsername(username);
        BanRecord banRecord = BanRecord.builder()
                .bannedEmail(bannedUser.getEmail())
                .bannedUsername(username)
                .reason(comment)
                .admin(admin)
                .build();
        banRecordRepository.save(banRecord);
        userRepository.delete(userRepository.findByUsername(username));
        emailService.sendTextEmail(bannedUser.getEmail(),"Banned Account","Your account has just been banned by admin "+adminUsername+", reason: "+comment);
    }

    public void updateFirstName(String username, String firstName) {
        User user = userRepository.findByUsername(username);
        user.setFirstName(firstName);
        userRepository.save(user);
    }

    public void updateLastName(String username, String lastName) {
        User user = userRepository.findByUsername(username);
        user.setLastName(lastName);
        userRepository.save(user);
    }

    public void updateProfilePicture(String username, String profilePicture) {
        User user = userRepository.findByUsername(username);
        user.setProfilePicture(profilePicture);
        userRepository.save(user);
    }

    public List<UserDto> getInactiveUsers() {
        List<User> inactiveUsers = userRepository.findUserByActiveFalse();
        return UserMethods.userListToUserDtoList(inactiveUsers);
    }

    public AdminHistoryDto getAdminHistory(String adminUsername) {
        List<RegistrationResult> registrationResults = registrationResultRepository.findAllByAdmin_Username(adminUsername);
        List<Question> questions = questionRepository.getQuestionByCheckedTrueAndAdmin_Username(adminUsername);
        List<RegistrationResultDto> registrationResultDtos = new ArrayList<>();
        registrationResults.forEach(registrationResult -> {
            registrationResultDtos.add(registrationResult.registrationResultToDto());
        });
        List<QuestionDto> questionDtos = questionListToQuestionDtoList(questions);
        return AdminHistoryDto.builder().questions(questionDtos).registrationResultList(registrationResultDtos).build();
    }

    public UserDto getInactiveDoctors() {
        Role role = roleRepository.findRoleByName(DefinedRole.DOCTOR);
        try {
            return userRepository.findFirstByActiveFalseAndRole(role).userDtoFromUser();
        } catch (Exception e) {
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
            log.info("Authority: "+user.getRole().getName().toString());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
