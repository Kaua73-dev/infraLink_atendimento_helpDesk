package com.infraLink.API.dto.request.ticket;

import com.infraLink.API.model.roles.queue.QueueTypeEnum;

public record TicketRequest(String description, String email, QueueTypeEnum queueTypeEnum
) {





}
