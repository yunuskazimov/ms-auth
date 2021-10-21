package az.bank.msauth.service;


import az.bank.msauth.model.AuthDto;
import az.bank.msauth.model.SignUpDto;
import org.springframework.data.util.Pair;

public interface AuthService {
    void singUp(SignUpDto signUpDto);

    Pair<String, String> login(AuthDto authDto);

    Long validateAccessToken(String accessToken);

    Pair<String, String> refreshTokens(String refreshToken);
}
