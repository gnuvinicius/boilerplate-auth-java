package com.garage.auth.applications.dtos;

public record RequestRefreshPasswordDto(String email, String tokenRefreshPassword, String newPassword) {
}