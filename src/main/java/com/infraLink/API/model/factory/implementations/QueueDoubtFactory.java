package com.infraLink.API.model.factory.implementations;

import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.factory.contracts.QueueFactory;
import com.infraLink.API.model.repository.queue.QueueRepository;
import com.infraLink.API.model.roles.queue.QueueTypeEnum;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class QueueDoubtFactory implements QueueFactory {

    private final QueueRepository queueRepository;

    public QueueDoubtFactory(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    @Override
    public Queue createQueue() {

        Queue queue = new Queue();
        queue.setCreateAt(LocalDateTime.now());
        queue.setQueueTypeEnum(QueueTypeEnum.DOUBT);
        queue.setPriority(3);

        return queueRepository.save(queue);

    }

}
