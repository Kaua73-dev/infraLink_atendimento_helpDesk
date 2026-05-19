package com.infraLink.API.dto.response.webSocket;

import java.time.LocalDateTime;

public record WebSocketSendMessageResponse(String name, LocalDateTime localDateTime, String content) {
}
