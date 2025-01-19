package com.crio.learning_navigator.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime createdAt;

    public ApiError(){
        this.createdAt=LocalDateTime.now();
    }
    public ApiError(String message,HttpStatus httpStatus){
        this();
        this.httpStatus=httpStatus;
        this.message=message;
    }
}
