package com.infraLink.API.service.ticket;


import com.infraLink.API.dto.request.ticket.TicketRequest;
import com.infraLink.API.dto.response.ticket.TicketCreateResponse;
import com.infraLink.API.exception.ticket.TicketAlreadyExistException;
import com.infraLink.API.exception.user.UserNotFoundException;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.factory.implementations.QueueDoubtFactory;
import com.infraLink.API.model.factory.implementations.QueueInstabilityFactory;
import com.infraLink.API.model.factory.implementations.QueueNoServiceFactory;
import com.infraLink.API.model.repository.ticket.TicketRepository;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.roles.queue.QueueTypeEnum;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import com.infraLink.API.service.queue.QueueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final QueueService queueService;
    private final QueueDoubtFactory queueDoubtFactory;
    private final QueueInstabilityFactory queueInstabilityFactory;
    private final QueueNoServiceFactory queueNoServiceFactory;


    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, QueueService queueService, QueueDoubtFactory queueDoubtFactory, QueueInstabilityFactory queueInstabilityFactory, QueueNoServiceFactory queueNoServiceFactory) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.queueService = queueService;
        this.queueDoubtFactory = queueDoubtFactory;
        this.queueInstabilityFactory = queueInstabilityFactory;
        this.queueNoServiceFactory = queueNoServiceFactory;
    }


    // apenas Client
    public TicketCreateResponse createTicket(TicketRequest request, String email, QueueTypeEnum queueTypeEnum){

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if(ticketRepository.findByClient(user).isPresent()){
            throw new TicketAlreadyExistException();
        }

        Queue queue = switch (queueTypeEnum) {
            case DOUBT -> queueDoubtFactory.createQueue();
            case INSTABILITY -> queueInstabilityFactory.createQueue();
            case NO_SERVICE -> queueNoServiceFactory.createQueue();
        };
        queueService.addQueue(queue);

        Ticket ticket = new Ticket();
        ticket.setDescription(request.description());
        ticket.setClient(user);
        ticket.setTicketStatusEnum(TicketStatusEnum.PENDING);
        ticket.setCreatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);

        return new TicketCreateResponse(
                ticket.getClient(),
                ticket.getCreatedAt(),
                ticket.getTicketStatusEnum()
        );


    }


}
