package es.bxg.commonlib.dtos.security;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(@NotBlank String username,
                                    @NotBlank String password){
}
