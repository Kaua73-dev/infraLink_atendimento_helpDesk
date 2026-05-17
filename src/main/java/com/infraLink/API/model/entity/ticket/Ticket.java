package com.infraLink.API.model.entity.ticket;


import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User client;

    @ManyToOne
    private User attendant;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnum ticketStatusEnum;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;



}
