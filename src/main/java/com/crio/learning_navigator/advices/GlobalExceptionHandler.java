package com.crio.learning_navigator.advices;

import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.exception.StudentAlreadyEnrolledForExamException;
import com.crio.learning_navigator.exception.StudentAlreadyEnrolledInSubjectException;
import com.crio.learning_navigator.exception.StudentNotEnrolledForSubjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiError apiError=new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException exception){
        ApiError apiError=new ApiError(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleStudentAlreadyEnrolledForExamException(StudentAlreadyEnrolledForExamException exception){
        ApiError apiError=new ApiError(exception.getMessage(),HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleStudentAlreadyEnrolledInSubjectException(StudentAlreadyEnrolledInSubjectException exception){
        ApiError apiError=new ApiError(exception.getMessage(),HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleStudentNotEnrolledForSubjectException(StudentNotEnrolledForSubjectException exception){
        ApiError apiError=new ApiError(exception.getMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(Exception exception){
        ApiError apiError=new ApiError(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
