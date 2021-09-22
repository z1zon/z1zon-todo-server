package com.nnlk.z1zontodoserver.controller;

import com.google.common.base.Splitter;
import com.nnlk.z1zontodoserver.dto.user.request.UserLoginDto;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

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

    /* 클라이언트 최초 code 받아오는 url
    https://github.com/login/oauth/authorize?client_id=cfcabcb37f8af4177c2a
    * */
    @GetMapping("/github/callback")
    public String githubCallback(String code) {
        log.debug("   ---> code = " + code); // access token will..
        //1. code로 토큰 받고...
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        //HTTPBODY 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "cfcabcb37f8af4177c2a");
        params.add("redirect_uri", "http://localhost:8080/api/v1/auth/github/callback");
        params.add("code", code);
        params.add("client_secret", "b6141ebf6b63663c769f063922e51a8d2369fb6c");
        //HTTPHEADER와 HTTPBODY를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> githubTokenRequest =
                new HttpEntity<>(params, headers);
        //HTTP 요청하기 -POST 방식으로 - 그리고 response변수의 응답받음.
        String url = "https://github.com/login/oauth/access_token";
        ResponseEntity<String> tokenResponse = restTemplate.exchange(
                url,
                HttpMethod.POST,
                githubTokenRequest,
                String.class
        );

        // dataSample: access_token=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&scope=&token_type=bearer
        Map<String, String> responseQueryParameters = Splitter
                .on("&")
                .withKeyValueSeparator("=")
                .split(tokenResponse.getBody());
        log.debug("   ---> oAuth 토큰 발급 완료");
        log.debug("   ---> access token: " + responseQueryParameters.get("access_token"));
        log.debug("   ---> token type: " + responseQueryParameters.get("scope"));
        log.debug("   ---> scope: " + responseQueryParameters.get("token_type"));
//        System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
//        RestTemplate rt2 = new RestTemplate();
//        //HTTPHEADER 오브젝트 생성
//        HttpHeaders headers2 = new HttpHeaders();
//        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
//        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        //
        //2. 토큰으로 받은 아이디 비밀번호
        //curl -H "Authorization: token OAUTH-TOKEN" https://api.github.com/user

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "token " + responseQueryParameters.get("access_token"));
        //HTTPBODY 오브젝트 생성
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();

        //HTTPHEADER와 HTTPBODY를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> githubTokenRequest2 =
                new HttpEntity<>(params2, headers2);
        //HTTP 요청하기 -POST 방식으로 - 그리고 response변수의 응답받음.
        String url2 = "https://api.github.com/user";
        ResponseEntity<String> tokenResponse2 = restTemplate.exchange(
                url2,
                HttpMethod.GET,
                githubTokenRequest2,
                String.class
        );
        log.debug("   ---> 최종" + tokenResponse2.getBody());
        return "redirect:/";
    }
}
