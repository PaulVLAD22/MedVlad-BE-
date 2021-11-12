package com.university.medvladbe.entity.registration;

import com.university.medvladbe.dto.RegistrationResultDto;
import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="registration_result")
public class RegistrationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User admin;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private boolean verdict;
    private String comment;

    public RegistrationResultDto registrationResultToDto(){
        return RegistrationResultDto.builder()
                .user(user.userDtoFromUser().getUsername())
                .admin(admin.userDtoFromUser().getUsername())
                .verdict(verdict)
                .comment(comment)
                .build();
    }
}
