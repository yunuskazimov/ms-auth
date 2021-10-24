package az.bank.msauth.controller;

import az.bank.msauth.health.CustomHealthIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    private final CustomHealthIndicator customHealthIndicator;

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck(){
        customHealthIndicator.health();
    }

    @GetMapping("/readiness")
    public void readiness(){
        log.info("Readiness probe");
    }

    @GetMapping("/liveness")
    public void liveness(){
        log.info("Liveness probe");
    }
}
