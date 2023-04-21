package com.microservice.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/demo-controller")
public class DemoController {

    @GetMapping("")
    public ResponseEntity<String> sayHello() {
        /*UserEntity user = AuthUtils.getCurrentUser();
        System.out.println("======================" + user);*/
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
