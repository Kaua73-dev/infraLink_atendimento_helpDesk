package com.infraLink.API.exception.queue;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QueueIsNotAvaiableException extends RuntimeException {
    public QueueIsNotAvaiableException() {
        super("There is no queue available");
    }
}
