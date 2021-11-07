package com.university.medvladbe.exception;

public class AlreadyLikedComment extends RuntimeException{
    public AlreadyLikedComment(){
        super("User already liked comment");
    }
}