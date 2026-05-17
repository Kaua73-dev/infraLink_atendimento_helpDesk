package com.infraLink.API.dto.response.ticket;

import com.infraLink.API.dto.response.user.UserTicketResponse;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;

import java.time.LocalDateTime;

public record TicketFinishedResponse(UserTicketResponse client, UserTicketResponse attendant, LocalDateTime createdAt, LocalDateTime finishedAt, TicketStatusEnum ticketStatusEnum) {



}
