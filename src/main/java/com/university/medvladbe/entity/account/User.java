package com.university.medvladbe.entity.account;

import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.message.Message;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.entity.registration.RegistrationResult;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    private long token;
    @ColumnDefault("0")
    private int points=0;
    private Date dateOfRegistration;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "receiver")
    private List<Message> messagesReceived;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "admin")
    private List<RegistrationResult> registrationResultAdmin;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user")
    private RegistrationResult registrationResultUser;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Question> questions;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<QuestionAnswer> questionAnswers;



    public UserDto userDtoFromUser() {
        return UserDto.builder()
                .email(this.getEmail())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .active(this.isActive())
                .dateOfRegistration(this.getDateOfRegistration())
                .points(this.points)
                .profilePicture(this.getProfilePicture())
                .licensePicture(this.getLicensePicture())
                .role(this.getRole())
                .token(this.getToken())
                .username(this.getUsername())
                .build();
    }
}