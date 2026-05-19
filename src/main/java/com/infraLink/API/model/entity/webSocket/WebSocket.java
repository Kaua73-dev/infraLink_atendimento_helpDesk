package com.infraLink.API.model.entity.webSocket;


import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="web_socket")
public class WebSocket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2000)
    private String content;

    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



}
