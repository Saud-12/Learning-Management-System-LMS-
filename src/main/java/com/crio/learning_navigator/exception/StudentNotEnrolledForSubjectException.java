package com.crio.learning_navigator.exception;

public class StudentNotEnrolledForSubjectException extends RuntimeException{
    public StudentNotEnrolledForSubjectException(String message){
        super(message);
    }
}
