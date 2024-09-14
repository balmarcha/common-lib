package es.bxg.commonlib.dtos.security;

public record AuthenticationResponse(String username,
                                     String accessToken,
                                     String refreshToken,
                                     boolean authenticated) {
}
