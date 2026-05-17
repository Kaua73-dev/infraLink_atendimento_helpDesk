package com.infraLink.API.exception.ticket;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketAlreadyExistException extends RuntimeException {
    public TicketAlreadyExistException() {
        super("Ticket already exist");
    }
}
