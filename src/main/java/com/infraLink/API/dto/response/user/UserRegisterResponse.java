package com.infraLink.API.dto.response.user;

import java.time.LocalDateTime;

public record UserRegisterResponse(String name, String email, String cpf, LocalDateTime createAt) {
}
