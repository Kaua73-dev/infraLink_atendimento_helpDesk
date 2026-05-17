package com.infraLink.API.service.queue;


import com.infraLink.API.exception.queue.QueueIsNotAvaiableException;
import com.infraLink.API.exception.queue.QueueThereAreNotNextException;
import com.infraLink.API.model.entity.queue.Queue;
import com.infraLink.API.model.repository.queue.QueueRepository;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.PriorityQueue;

@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    private final PriorityQueue<Queue> queue =
            new PriorityQueue<>(
                    Comparator.comparingInt(Queue::getPriority)
            );

    private Queue current;


    public Queue callNextQueue(){
        if(current != null){
           throw new QueueThereAreNotNextException();
        }

        current = queue.poll();
        return current;
    }

    public void finishQueue(){
        if(current == null){
            throw new QueueIsNotAvaiableException();
        }

        current = null;
    }

    public void addQueue(Queue ticket){
         queue.add(ticket);
    }




}
