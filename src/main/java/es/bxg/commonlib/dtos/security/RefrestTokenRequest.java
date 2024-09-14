package es.bxg.commonlib.dtos.security;

import jakarta.validation.constraints.NotBlank;

public record RefrestTokenRequest(@NotBlank String refreshToken){
}
