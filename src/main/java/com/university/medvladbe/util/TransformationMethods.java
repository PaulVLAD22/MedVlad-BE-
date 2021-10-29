package com.university.medvladbe.util;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformationMethods{

    static public List<UserDto> userListToUserDtoList(List<User> userList){
        List<UserDto> userDtoList = new ArrayList<>();
        userList.stream().
                filter(inactiveUser -> !inactiveUser.getRole().getName().toString().equals("DOCTOR")).
                forEach(inactiveUser -> {
                    userDtoList.add(inactiveUser.userDtoFromUser());
                });
        return userDtoList;
    }
}