package com.university.medvladbe.model.entity.message;

import com.university.medvladbe.model.entity.account.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
    private String content;
    private Timestamp timeOfSending;
}
