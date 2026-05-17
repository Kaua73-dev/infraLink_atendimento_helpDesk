package com.infraLink.API.controller.ticket;


import com.infraLink.API.dto.request.ticket.TicketRequest;
import com.infraLink.API.dto.response.ticket.TicketCreateResponse;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.factory.implementations.QueueDoubtFactory;
import com.infraLink.API.model.factory.implementations.QueueInstabilityFactory;
import com.infraLink.API.model.factory.implementations.QueueNoServiceFactory;
import com.infraLink.API.model.roles.queue.QueueTypeEnum;
import com.infraLink.API.service.queue.QueueService;
import com.infraLink.API.service.ticket.TicketService;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create")
    public TicketCreateResponse createTicket(@RequestBody TicketRequest request, @NonNull String email, @NonNull QueueTypeEnum queueTypeEnum){
        return ticketService.createTicket(request, email, queueTypeEnum);
    }



}
