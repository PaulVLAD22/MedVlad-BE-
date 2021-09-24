package com.university.medvladbe.exception;

public class BadLogin extends RuntimeException{
    public BadLogin(){
        super("Bad Login");
    }
}
