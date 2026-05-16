package com.infraLink.API.dto.request.user;

public record UserRegisterRequest(String name, String email, String cpf, String password) {
}
