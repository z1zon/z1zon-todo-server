package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.dto.user.UserCreateDto;
import com.nnlk.z1zontodoserver.dto.user.UserLoginDto;
import com.nnlk.z1zontodoserver.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("signup")
    public ResponseEntity<String> save(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        try {
            if(log.isDebugEnabled()){
                log.debug("   ---> 회원가입 시작 {}",userCreateDto);
            }
            if (errors.hasErrors()) {
                log.error(String.format("   ---> %s", errors.getFieldError().toString()));
                return new ResponseEntity<String>("wrong field", HttpStatus.BAD_REQUEST);
            }
            authService.register(userCreateDto);
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
        if(log.isDebugEnabled()){
            log.debug("   ---> 로그인 인증 시작 {}",userLoginDto);
        }
        if (errors.hasErrors()) {
            log.error(String.format("   ---> %s", errors.getFieldError().toString()));
            return new ResponseEntity<String>("wrong field", HttpStatus.BAD_REQUEST);
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
}
