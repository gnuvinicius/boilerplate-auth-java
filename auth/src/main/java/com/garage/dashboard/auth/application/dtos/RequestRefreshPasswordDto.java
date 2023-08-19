package com.garage.dashboard.auth.application.dtos;

public record RequestRefreshPasswordDto(String email, String tokenRefreshPassword, String newPassword) {
}