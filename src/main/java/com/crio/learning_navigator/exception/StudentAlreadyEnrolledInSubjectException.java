package com.crio.learning_navigator.exception;

public class StudentAlreadyEnrolledInSubjectException extends RuntimeException{

    public StudentAlreadyEnrolledInSubjectException(String message){
        super(message);
    }
}
