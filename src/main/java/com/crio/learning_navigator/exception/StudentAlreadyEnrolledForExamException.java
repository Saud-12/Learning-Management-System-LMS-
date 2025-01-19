package com.crio.learning_navigator.exception;

public class StudentAlreadyEnrolledForExamException extends RuntimeException{
    public StudentAlreadyEnrolledForExamException(String message){
        super(message);
    }
}
