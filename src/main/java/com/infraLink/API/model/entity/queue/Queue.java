package com.infraLink.API.model.entity.queue;

import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.roles.queue.QueueTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="queue")
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private QueueTypeEnum queueTypeEnum;

    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
