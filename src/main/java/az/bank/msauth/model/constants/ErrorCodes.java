package az.bank.msauth.model.constants;

public abstract class ErrorCodes {
    public static final String ACCESS_TOKEN_EXPIRED = "auth.expired-access-token";
    public static final String UNEXPECTED_EXCEPTION = "auth.unexpected-exception";
    public static final String REFRESH_TOKEN_EXPIRED = "auth.expired-refresh-token";

    private ErrorCodes() {
    }
}
