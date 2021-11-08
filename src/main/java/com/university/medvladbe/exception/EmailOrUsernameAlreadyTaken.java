package com.university.medvladbe.exception;

public class EmailOrUsernameAlreadyTaken extends RuntimeException{
    public EmailOrUsernameAlreadyTaken(){
        super("Email or username already taken");
    }
}
