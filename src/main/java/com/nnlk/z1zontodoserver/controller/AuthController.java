package com.nnlk.z1zontodoserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nnlk.z1zontodoserver.dto.user.request.UserLoginDto;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthService authService;

    /*
     * TODO Exception Refactoring
     * */
    @PostMapping("/signup")
    public ResponseEntity<String> save(@RequestBody @Valid UserUpsertRequestDto userUpsertRequestDto, Errors errors) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("   ---> 회원가입 시작 {}", userUpsertRequestDto);
            }
            authService.register(userUpsertRequestDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>("runtime error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("save success", HttpStatus.CREATED);
    }

    /*
     * TODO Exception Refactoring
     * */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors) {
        if (log.isDebugEnabled()) {
            log.debug("   ---> 로그인 인증 시작 {}", userLoginDto);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            String jwtToken = authService.login(userLoginDto);
            httpHeaders.setBearerAuth(jwtToken);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>("password wrong", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {
            return new ResponseEntity<String>("token wrong", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("token wrong", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("login Success", httpHeaders, HttpStatus.OK);
    }

    /** 클라이언트 깃허브 인증 선택시 콜백,
     * request: code
     * 인증 uri: https://github.com/login/oauth/authorize?client_id=cfcabcb37f8af4177c2a
     */
    @GetMapping("/github/callback")
    public RedirectView githubCallback(String code) throws JsonProcessingException {

        log.debug("   ---> code = {}", code);

        String jwtToken = authService.getJwtByGithubCode(code);

        RedirectView redirectView = new RedirectView();
        redirectView.setHosts("127.0.0.1");
        redirectView.setUrl("/?bear="+jwtToken);
        return redirectView;
    }
}
