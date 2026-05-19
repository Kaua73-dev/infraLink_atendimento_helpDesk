package com.infraLink.API.dto.request.webSocket;

public record WebSocketSendMessageRequest(Integer ticketId, String content) {
}
