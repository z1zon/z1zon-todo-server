package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.jwt.JwtFilter;
import com.nnlk.z1zontodoserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/")
    private String test() {
        return "test success";
    }

    @GetMapping("/test")
    private String authTest() {

        return "auth success";
    }
}
