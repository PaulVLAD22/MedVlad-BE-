package com.university.medvladbe.model.entity.account;

import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.model.entity.message.Message;
import com.university.medvladbe.model.entity.question.Question;
import com.university.medvladbe.model.entity.question.QuestionAnswer;
import com.university.medvladbe.model.entity.registration.RegistrationResult;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    private String firstName;
    @ManyToOne
    private Role role;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    private String profilePicture;
    private String licensePicture;
    private boolean active = false;
    @ColumnDefault("0")
    private int points = 0;
    private Date dateOfRegistration;

    @ManyToMany(cascade = {CascadeType.ALL})
    @Fetch(value = FetchMode.SELECT)
    List<QuestionAnswer> answers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "receiver")
    private List<Message> messagesReceived;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Question> questions;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<QuestionAnswer> questionAnswers;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private RegistrationResult registrationResultUser;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin")
    private List<RegistrationResult> registrationResultsAdmin;

    public UserDto userDtoFromUser() {
        return UserDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .active(this.isActive())
                .dateOfRegistration(this.getDateOfRegistration())
                .points(this.points)
                .profilePicture(this.getProfilePicture())
                .licensePicture(this.getLicensePicture())
                .role(this.getRole())
                .username(this.getUsername())
                .build();
    }
}