package az.bank.msauth.service;

import az.bank.msauth.dao.AuthUserEntity;
import az.bank.msauth.mapper.AuthMapper;
import az.bank.msauth.model.AuthDto;
import az.bank.msauth.model.RefreshTokenDto;
import az.bank.msauth.model.SignUpDto;
import az.bank.msauth.model.exception.AuthException;
import az.bank.msauth.repository.AuthUserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final TokenService tokenService;

    public AuthServiceImpl(AuthUserRepository authUserRepository, TokenService tokenService) {
        this.authUserRepository = authUserRepository;
        this.tokenService = tokenService;
    }

    @Override
    public void singUp(SignUpDto signUpDto) {
        authUserRepository.save(AuthMapper.toEntity(signUpDto));
    }

    @Override
    public Pair<String, String> login(AuthDto authDto) {
        AuthUserEntity authUserEntity = authUserRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new AuthException("auth.authentication-failed", "Authentication failed"));

        if (authUserEntity.getPassword().equals(authDto.getPassword())) {
            return Pair.of(tokenService.generateAccessToken(authUserEntity.getId()),
                    tokenService.generateRefreshToken(
                            new RefreshTokenDto(
                                    authUserEntity.getId(), 5, LocalDateTime.now().plusMinutes(2L))));
        } else {
            throw new AuthException("auth.authentication-failed", "Authentication failed");
        }
    }

    @Override
    public Long validateAccessToken(String accessToken) {
        return tokenService.validateAccessToken(accessToken);
    }

    @Override
    public Pair<String, String> refreshTokens(String refreshToken) {
        RefreshTokenDto refreshTokenDto = tokenService.validateRefreshToken(refreshToken);

        return Pair.of(tokenService.generateAccessToken(refreshTokenDto.getUserId()),
                tokenService.generateRefreshToken(refreshTokenDto));
    }
}
