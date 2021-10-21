package az.bank.msauth.controller;

import az.bank.msauth.health.CustomHealthIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HealthController {
    private final CustomHealthIndicator customHealthIndicator;
    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck(){
        //TODO remove it
        customHealthIndicator.health();
    }
}
