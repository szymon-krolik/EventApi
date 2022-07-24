package com.example.praca.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Szymon Królik
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    public String siema(Principal principal) {
        return principal.getName();
    }
}
