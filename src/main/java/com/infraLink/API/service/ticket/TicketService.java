package com.infraLink.API.service.ticket;


import com.infraLink.API.auth.AuthVerifyService;
import com.infraLink.API.dto.request.ticket.TicketAttendRequest;
import com.infraLink.API.dto.request.ticket.TicketRequest;
import com.infraLink.API.dto.response.ticket.TicketCreateResponse;
import com.infraLink.API.dto.response.ticket.TicketFinishedResponse;
import com.infraLink.API.dto.response.ticket.TicketServedResponse;
import com.infraLink.API.dto.response.user.UserClientAndAttendTicketResponse;
import com.infraLink.API.dto.response.user.UserClientTicketResponse;
import com.infraLink.API.dto.response.webSocket.ChatClosedResponse;
import com.infraLink.API.exception.ticket.*;
import com.infraLink.API.exception.user.UserNotAuthorizedException;
import com.infraLink.API.exception.user.UserNotFoundException;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.factory.implementations.QueueDoubtFactory;
import com.infraLink.API.model.factory.implementations.QueueInstabilityFactory;
import com.infraLink.API.model.factory.implementations.QueueNoServiceFactory;
import com.infraLink.API.model.repository.queue.QueueRepository;
import com.infraLink.API.model.repository.ticket.TicketRepository;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import com.infraLink.API.service.queue.QueueService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final QueueRepository queueRepository;
    private final SimpMessagingTemplate messagingTemplate;



    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, QueueService queueService, QueueDoubtFactory queueDoubtFactory, QueueInstabilityFactory queueInstabilityFactory, QueueNoServiceFactory queueNoServiceFactory, AuthVerifyService authVerifyService, QueueRepository queueRepository, SimpMessagingTemplate messagingTemplate) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.queueService = queueService;
        this.queueDoubtFactory = queueDoubtFactory;
        this.queueInstabilityFactory = queueInstabilityFactory;
        this.queueNoServiceFactory = queueNoServiceFactory;
        this.authVerifyService = authVerifyService;
        this.queueRepository = queueRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private TicketCreateResponse toResponse(Ticket ticket){
        return new TicketCreateResponse(
                new UserClientTicketResponse(ticket.getClient().getName()),
                ticket.getCreatedAt(),
                ticket.getTicketStatusEnum(),
                ticket.getId()
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
        return ticketRepository.
                findByTicketStatusEnumNot(TicketStatusEnum.FINISHED)
                .stream()
                .map(this::toResponse)
                .toList();
    }



    @Transactional
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

        queueRepository.deleteByTicket(ticket);


        return new TicketServedResponse(
                new UserClientAndAttendTicketResponse(ticket.getAttendant().getName(), ticket.getClient().getName()),
                ticket.getCreatedAt(),
                ticket.getTicketStatusEnum()
        );
    }

    public TicketFinishedResponse finishService(Integer ticketId){
        User user = authVerifyService.getAuthenticate();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);


        if(!ticket.getAttendant().getId().equals(user.getId())){
            throw new UserNotAuthorizedException();
        }

        if(!ticket.getTicketStatusEnum().equals(TicketStatusEnum.IN_SERVICE)){
            throw new TicketChatWebSocketUnavailableException();
        }

        ticket.setTicketStatusEnum(TicketStatusEnum.FINISHED);
        ticket.setFinishedAt(LocalDateTime.now());
        ticketRepository.save(ticket);


        messagingTemplate.convertAndSend(
                "/topic/ticket/" + ticket.getId(),
                new ChatClosedResponse(
                        "Service closed"
                )
        );

        return new TicketFinishedResponse(
                new UserClientAndAttendTicketResponse(ticket.getAttendant().getName(), ticket.getClient().getName()),
                ticket.getCreatedAt(),
                ticket.getFinishedAt(),
                ticket.getTicketStatusEnum()

                );
    }




}
