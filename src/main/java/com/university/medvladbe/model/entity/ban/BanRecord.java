package com.university.medvladbe.model.entity.ban;

import com.university.medvladbe.model.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ban_record")
public class BanRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User admin;
    private String bannedUsername;
    private String bannedEmail;
    private String reason;

}
