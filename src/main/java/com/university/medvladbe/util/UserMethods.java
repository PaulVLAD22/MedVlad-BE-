package com.university.medvladbe.util;

import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.model.entity.account.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserMethods {

    static public List<UserDto> userListToUserDtoList(List<User> userList){
        List<UserDto> userDtoList = new ArrayList<>();
        userList.stream().
                filter(inactiveUser -> !inactiveUser.getRole().getName().toString().equals("DOCTOR")).
                forEach(inactiveUser -> {
                    userDtoList.add(inactiveUser.userDtoFromUser());
                });
        return userDtoList;
    }
    static public String getCurrentUsername() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

}