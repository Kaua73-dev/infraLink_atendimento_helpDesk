package com.infraLink.API.controller.queue;


import com.infraLink.API.model.repository.queue.QueueRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueRepository queueRepository;

    public QueueController(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }


    // regra de criação para cada tipo aqui



}
