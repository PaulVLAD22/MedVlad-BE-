package com.university.medvladbe.entity.account;

import com.university.medvladbe.dto.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

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
    private long token;
    @ColumnDefault("0")
    private int adminPoints=0;
    @ColumnDefault("0")
    private int doctorPoints=0;
    private Date dateOfRegistration;

    public UserDto userDtoFromUser() {
        return UserDto.builder()
                .email(this.getEmail())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .active(this.isActive())
                .dateOfRegistration(this.getDateOfRegistration())
                .adminPoints(this.getAdminPoints())
                .doctorPoints(this.getDoctorPoints())
                .profilePicture(this.getProfilePicture())
                .licensePicture(this.getLicensePicture())
                .role(this.getRole())
                .token(this.getToken())
                .username(this.getUsername())
                .build();
    }
}