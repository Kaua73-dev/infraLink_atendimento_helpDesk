package com.infraLink.API.controller.webSocket;

import com.infraLink.API.dto.request.webSocket.WebSocketSendMessageRequest;
import com.infraLink.API.dto.response.webSocket.WebSocketSendMessageResponse;
import com.infraLink.API.service.webSocket.WebSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/chat.send")
    public WebSocketSendMessageResponse sendMessage(WebSocketSendMessageRequest  request){
        return webSocketService.sendMessage(request);
    }




}
