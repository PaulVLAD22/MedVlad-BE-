package com.university.medvladbe.exception;

public class UserNotActive extends RuntimeException{
    public UserNotActive(){
        super("User is not active");
    }
}
