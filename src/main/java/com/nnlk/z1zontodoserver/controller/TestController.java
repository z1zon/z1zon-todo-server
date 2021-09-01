package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/")
    private String test(@AuthenticationPrincipal User user) {
        System.out.println(user.getEmail());
        System.out.println(user);
        return "test success";
    }

    @GetMapping("/test")
    private String authTest() {

        return "auth success";
    }
}
