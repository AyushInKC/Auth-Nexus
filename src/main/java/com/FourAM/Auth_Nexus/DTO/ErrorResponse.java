package com.FourAM.Auth_Nexus.DTO;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        HttpStatus status,
        int statusCode
) {
}
