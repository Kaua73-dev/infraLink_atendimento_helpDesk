package com.infraLink.API.service.webSocket;


import com.infraLink.API.auth.AuthVerifyService;
import com.infraLink.API.dto.request.webSocket.WebSocketSendMessageRequest;
import com.infraLink.API.dto.response.webSocket.WebSocketSendMessageResponse;
import com.infraLink.API.exception.ticket.TicketChatWebSocketUnavailableException;
import com.infraLink.API.exception.ticket.TicketNotFoundException;
import com.infraLink.API.exception.user.UserNotAuthorizedException;
import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.entity.webSocket.WebSocket;
import com.infraLink.API.model.repository.ticket.TicketRepository;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.repository.webSocket.WebSocketRepository;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WebSocketService {

    private final WebSocketRepository webSocketRepository;
    private final AuthVerifyService authVerifyService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public WebSocketService(WebSocketRepository webSocketRepository, AuthVerifyService authVerifyService, TicketRepository ticketRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.webSocketRepository = webSocketRepository;
        this.authVerifyService = authVerifyService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private void sendToTicketTopic(Integer ticketId){
        messagingTemplate.convertAndSend(
                "/topic/ticket/" + ticketId
        );
    }


    public void sendMessage(WebSocketSendMessageRequest request){

        User sender = authVerifyService.getAuthenticate();

        Ticket ticket = ticketRepository.findById(request.ticketId())
                .orElseThrow(TicketNotFoundException::new);

        if(!ticket.getClient().equals(sender) && !ticket.getAttendant().equals(sender)){
            throw new UserNotAuthorizedException();
        }

        if(!ticket.getTicketStatusEnum().equals(TicketStatusEnum.IN_SERVICE)){
            throw new TicketChatWebSocketUnavailableException();
        }

        WebSocket webSocket = new WebSocket();
        webSocket.setContent(request.content());
        webSocket.setLocalDateTime(LocalDateTime.now());
        webSocket.setTicket(ticket);
        webSocket.setUser(sender);

        webSocketRepository.save(webSocket);


        sendToTicketTopic(ticket.getId());




    }




}
