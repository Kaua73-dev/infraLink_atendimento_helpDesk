package com.infraLink.API.dto.response.ticket;

import com.infraLink.API.dto.response.user.UserClientTicketResponse;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;

import java.time.LocalDateTime;

public record TicketFinishedResponse(UserClientTicketResponse client, UserClientTicketResponse attendant, LocalDateTime createdAt, LocalDateTime finishedAt, TicketStatusEnum ticketStatusEnum) {



}
