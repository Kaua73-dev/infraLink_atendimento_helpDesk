package com.infraLink.API.model.repository.webSocket;

import com.infraLink.API.model.entity.webSocket.WebSocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketRepository extends JpaRepository<WebSocket, Integer> {



}
