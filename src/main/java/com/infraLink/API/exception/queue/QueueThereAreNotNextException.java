package com.infraLink.API.exception.queue;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QueueThereAreNotNextException extends RuntimeException {
    public QueueThereAreNotNextException() {
        super("There is no one next in line");
    }
}
