package com.university.medvladbe.dto;

import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private Timestamp timeOfSending;
}