package com.microservice.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/hospitalServiceFallBack")
    public String orderServiceFallback() {
        return "Hospital Service is down!";
    }

    @GetMapping("/patientServiceFallBack")
    public String paymentServiceFallback() {
        return "Payment Service is down!";
    }

    @GetMapping("/authServiceFallBack")
    public String authServiceFallback() {
        return "Auth Service is down!";
    }

}
