package az.bank.msauth.controller;

import az.bank.msauth.model.AuthDto;
import az.bank.msauth.model.SignUpDto;
import az.bank.msauth.model.constants.HttpHeaders;
import az.bank.msauth.service.AuthService;
import az.bank.msauth.util.CookieUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    public AuthController(AuthService authService,
                          CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
        this.authService = authService;
    }

    @PostMapping("sign-up")
    public void signUp(@RequestBody SignUpDto signUpDto) {
        authService.singUp(signUpDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody AuthDto authDto,
                      HttpServletResponse response) {
        cookieUtil.addCookies(authService.login(authDto), response);
    }

    @PostMapping("/token/access")
    public void validateAccessToken(@CookieValue(name = HttpHeaders.ACCESS_TOKEN) String accessToken,
                                    HttpServletResponse response) {
        Long userId = authService.validateAccessToken(accessToken);
        response.addHeader(HttpHeaders.USER_ID, userId.toString());
    }

    @PostMapping("/token/refresh")
    public void refreshTokens(@CookieValue(name = HttpHeaders.REFRESH_TOKEN) String refreshToken,
                              HttpServletResponse response) {
        cookieUtil.addCookies(authService.refreshTokens(refreshToken), response);
    }
}
