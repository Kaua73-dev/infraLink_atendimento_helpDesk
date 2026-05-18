package com.infraLink.API.exception.ticket;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TicketUnavailableException extends RuntimeException {
    public TicketUnavailableException() {
        super("Ticket already processed or in progress");
    }
}
