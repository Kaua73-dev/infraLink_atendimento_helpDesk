package com.infraLink.API.service.queue;



import com.infraLink.API.exception.queue.QueueIsNotAvailableException;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.repository.queue.QueueRepository;
import com.infraLink.API.model.roles.ticket.TicketStatusEnum;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }


    public Queue callNextQueue(){
       return queueRepository.findFirstByTicket_TicketStatusEnumOrderByPriorityAscCreateAtAsc(
               TicketStatusEnum.PENDING
       ).orElseThrow(QueueIsNotAvailableException::new);

       // gera query no banco, fica salvo no banco e não localmente

    }

    public void addQueue(Queue queue){
       queueRepository.save(queue);
    }



}
