package com.university.medvladbe.entity.account;

import com.university.medvladbe.dto.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    @ManyToOne
    private Role role;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    private String profilePicture;
    private boolean active;
    private long token;
    private int adminPoints;
    private int doctorPoints;
    private Date dateOfRegistration;

    public UserDto userDtoFromUser() {
        return UserDto.builder()
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .active(this.isActive())
                .dateOfRegistration(this.getDateOfRegistration())
                .adminPoints(this.getAdminPoints())
                .doctorPoints(this.getDoctorPoints())
                .profilePicture(this.getProfilePicture())
                .role(this.getRole())
                .token(this.getToken())
                .username(this.getUsername())
                .build();
    }
}