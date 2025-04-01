package no.vinny.client;

public record Token (String access_token, String token_type, Long expires_in) {}