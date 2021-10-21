package az.bank.msauth.service;

import az.bank.msauth.model.RefreshTokenDto;

public interface TokenService {
    String generateAccessToken(Long userId);

    String generateRefreshToken(RefreshTokenDto refreshTokenDto);

    Long validateAccessToken(String token);

    RefreshTokenDto validateRefreshToken(String token);
}
