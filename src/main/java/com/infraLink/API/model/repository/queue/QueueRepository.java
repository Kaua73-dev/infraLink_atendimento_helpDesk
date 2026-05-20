package com.infraLink.API.model.repository.queue;

import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.entity.ticket.Ticket;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Integer> {

    Optional<Queue> findFirstByTicket_TicketStatusEnumOrderByPriorityAscCreateAtAsc(
            TicketStatusEnum status
    );

    @Modifying
    @Transactional
    void deleteByTicket(Ticket ticket);

}
