package az.bank.msauth.util;

import az.bank.msauth.model.constants.HttpHeaders;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
@Component
public class CookieUtil {
    public void addCookies(Pair<String, String> tokens, HttpServletResponse response){
        addCookies(HttpHeaders.ACCESS_TOKEN,tokens.getFirst(),response);
        addCookies(HttpHeaders.REFRESH_TOKEN,tokens.getSecond(),response);
    }

    private void addCookies(String name, String value, HttpServletResponse response) {
    final ResponseCookie responseCookie = ResponseCookie
            .from(name,value)
            .httpOnly(true)
//            .secure(true)
//            .path("/")
//            .sameSite("Strict")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
    }
}
