package com.infraLink.API.model.factory.contracts;

import com.infraLink.API.dto.request.queue.QueueRequest;
import com.infraLink.API.model.entity.queue.Queue;
import org.springframework.stereotype.Component;

@Component
public interface QueueFactory {

    Queue createQueue();

}
