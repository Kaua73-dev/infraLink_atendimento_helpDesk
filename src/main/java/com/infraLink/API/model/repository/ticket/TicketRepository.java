package com.infraLink.API.model.repository.ticket;


import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByClientAndTicketStatusEnum(User user, TicketStatusEnum ticketStatusEnum);


}
