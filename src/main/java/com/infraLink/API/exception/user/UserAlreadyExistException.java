package com.infraLink.API.exception.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exist");
    }
}
