package com.infraLink.API.dto.response.ticket;

import com.infraLink.API.dto.response.user.UserClientTicketResponse;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;

import java.time.LocalDateTime;

public record TicketCreateResponse(UserClientTicketResponse client, LocalDateTime createdAt, TicketStatusEnum ticketStatusEnum, Integer ticketId) {
}
