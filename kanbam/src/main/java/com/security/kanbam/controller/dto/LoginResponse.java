package com.security.kanbam.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
