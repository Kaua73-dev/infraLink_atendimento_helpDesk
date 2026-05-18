package com.infraLink.API.service.ticket;


import com.infraLink.API.auth.AuthVerifyService;
import com.infraLink.API.dto.request.ticket.TicketAttendRequest;
import com.infraLink.API.dto.request.ticket.TicketRequest;
import com.infraLink.API.dto.response.ticket.TicketCreateResponse;
import com.infraLink.API.dto.response.ticket.TicketServedResponse;
import com.infraLink.API.dto.response.user.UserClientAndAttendTicketResponse;
import com.infraLink.API.dto.response.user.UserClientTicketResponse;
import com.infraLink.API.exception.ticket.TicketAlreadyExistException;
import com.infraLink.API.exception.ticket.TicketNotFoundException;
import com.infraLink.API.exception.ticket.TicketUnavailableException;
import com.infraLink.API.exception.user.UserNotFoundException;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.factory.implementations.QueueDoubtFactory;
import com.infraLink.API.model.factory.implementations.QueueInstabilityFactory;
import com.infraLink.API.model.factory.implementations.QueueNoServiceFactory;
import com.infraLink.API.model.repository.ticket.TicketRepository;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import com.infraLink.API.service.queue.QueueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final QueueService queueService;
    private final QueueDoubtFactory queueDoubtFactory;
    private final QueueInstabilityFactory queueInstabilityFactory;
    private final QueueNoServiceFactory queueNoServiceFactory;
    private final AuthVerifyService authVerifyService;



    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, QueueService queueService, QueueDoubtFactory queueDoubtFactory, QueueInstabilityFactory queueInstabilityFactory, QueueNoServiceFactory queueNoServiceFactory, AuthVerifyService authVerifyService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.queueService = queueService;
        this.queueDoubtFactory = queueDoubtFactory;
        this.queueInstabilityFactory = queueInstabilityFactory;
        this.queueNoServiceFactory = queueNoServiceFactory;
        this.authVerifyService = authVerifyService;
    }

    private TicketCreateResponse toResponse(Ticket ticket){
        return new TicketCreateResponse(
                new UserClientTicketResponse(ticket.getClient().getName()),
                ticket.getCreatedAt(),
                ticket.getTicketStatusEnum()
        );
    }



    public TicketCreateResponse createTicket(TicketRequest request){

        User user = authVerifyService.getAuthenticate();

        if(userRepository.findByEmail(user.getEmail()).isEmpty()){
            throw new UserNotFoundException();
        }

        if(ticketRepository.findByClientAndTicketStatusEnum(user, TicketStatusEnum.PENDING).isPresent()){
            throw new TicketAlreadyExistException();
        }


        Ticket ticket = new Ticket();
        ticket.setDescription(request.description());
        ticket.setClient(user);
        ticket.setTicketStatusEnum(TicketStatusEnum.PENDING);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket ticketSaved = ticketRepository.save(ticket);

        Queue queue = switch (request.queueTypeEnum()) {
            case DOUBT -> queueDoubtFactory.createQueue(ticketSaved);
            case INSTABILITY -> queueInstabilityFactory.createQueue(ticketSaved);
            case NO_SERVICE -> queueNoServiceFactory.createQueue(ticketSaved);
        };

        queueService.addQueue(queue);

        return this.toResponse(ticketSaved);

    }


    public List<TicketCreateResponse> getAllTickets(){
        return ticketRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public TicketServedResponse attendTicket(){
        User attendant = authVerifyService.getAuthenticate();

        Queue queue = queueService.callNextQueue();
        Ticket ticket = queue.getTicket();


        if(ticket.getTicketStatusEnum().equals(TicketStatusEnum.IN_SERVICE) || ticket.getTicketStatusEnum().equals(TicketStatusEnum.FINISHED)){
            throw new TicketUnavailableException();
        }

        ticket.setTicketStatusEnum(TicketStatusEnum.IN_SERVICE);
        ticket.setAttendant(attendant);
        ticketRepository.save(ticket);

        return new TicketServedResponse(
                new UserClientAndAttendTicketResponse(ticket.getAttendant().getName(), ticket.getClient().getName()),
                ticket.getCreatedAt(),
                ticket.getTicketStatusEnum()
        );



    }






}
