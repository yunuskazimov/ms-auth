package az.bank.msauth.controller;

import az.bank.msauth.model.ErrorDto;
import az.bank.msauth.model.exception.AuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static az.bank.msauth.model.constants.ErrorCodes.ACCESS_TOKEN_EXPIRED;
import static az.bank.msauth.model.constants.ErrorCodes.UNEXPECTED_EXCEPTION;
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException e,
                                                      WebRequest webRequest){
        return handleExceptionInternal(e,
                ErrorDto.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build(),
                new HttpHeaders(),
                        e.getCode().equals(ACCESS_TOKEN_EXPIRED) ? HttpStatus.UNAUTHORIZED : HttpStatus. FORBIDDEN,
                webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e,
                                                      WebRequest webRequest){
        e.printStackTrace();
        return handleExceptionInternal(e,
                ErrorDto.builder()
                        .code(UNEXPECTED_EXCEPTION)
                        .message(e.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }
}
