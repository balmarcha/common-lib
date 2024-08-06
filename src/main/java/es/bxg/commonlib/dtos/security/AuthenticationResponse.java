package es.bxg.commonlib.dtos.security;

public record AuthenticationResponse(String username,
                                     String jwt,
                                     boolean status) {
}
