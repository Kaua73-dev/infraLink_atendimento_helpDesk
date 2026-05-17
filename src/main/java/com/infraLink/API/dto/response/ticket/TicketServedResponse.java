package com.infraLink.API.dto.response.ticket;

import com.infraLink.API.dto.response.user.UserTicketResponse;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;

import java.time.LocalDateTime;

public record TicketServedResponse(UserTicketResponse client, UserTicketResponse attendant, LocalDateTime createdAt, TicketStatusEnum ticketStatusEnum) {
}
