package com.infraLink.API.model.factory.contracts;

import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.entity.ticket.Ticket;
import org.springframework.stereotype.Component;

@Component
public interface QueueFactory {

    Queue createQueue(Ticket ticket);

}
