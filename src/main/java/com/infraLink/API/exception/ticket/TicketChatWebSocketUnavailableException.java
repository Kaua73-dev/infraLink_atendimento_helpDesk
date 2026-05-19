package com.infraLink.API.exception.ticket;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TicketChatWebSocketUnavailableException extends RuntimeException {
    public TicketChatWebSocketUnavailableException() {
        super("Ticket no is in service");
    }
}
