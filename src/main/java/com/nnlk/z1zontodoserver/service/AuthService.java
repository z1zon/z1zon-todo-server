package com.nnlk.z1zontodoserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.user.request.UserLoginDto;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.dto.user.response.GithubUserResponseDto;
import com.nnlk.z1zontodoserver.jwt.TokenProvider;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    @Value("${oauth.github.client_id}")
    private String clientId;
    @Value("${oauth.github.client_secret}")
    private String clientSecret;
    @Value("${oauth.github.access_token_uri}")
    private String accessTokenUri;
    @Value("${oauth.github.redirect_uri}")
    private String redirectUri;
//
//    @PostConstruct
//    public void init(@Value("${oauth.github.client_id}") String clientId,
//                     @Value("${oauth.github.client_secret}") String clientSecret,
//                     @Value("${oauth.github.access_token_uri}") String accessTokenUri,
//                     @Value("${oauth.github.redirect_uri}") String redirectUri) {
//        this.clientId = clientId;
//        this.clientSecret = clientSecret;
//        this.accessTokenUri = accessTokenUri;
//        this.redirectUri = redirectUri;
//    }

    @Transactional
    public void register(UserUpsertRequestDto userUpsertRequestDto) {
        validateDuplicateUser(userUpsertRequestDto.getEmail());

        User user = userUpsertRequestDto.toEntity();
        user.encryptPwd(getSHA256Pwd(userUpsertRequestDto.getPassword()));
        log.debug("   ---> user {}", user);
        userRepository.save(user);
    }

    public String login(UserLoginDto userLoginDto) {
        String email = userLoginDto.getEmail();
        String encPwd = getSHA256Pwd(userLoginDto.getPassword());
        User user = (User) loadUserByUsername(email);
        if (!encPwd.equals(user.getPassword())) {
            throw new IllegalArgumentException();
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, encPwd);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(email);

        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            if (log.isErrorEnabled()) {
                log.error("   ---> 유저 이름을 찾을 수 없습니다.{}", user);
            }
            throw new UsernameNotFoundException("유저 이름을 찾을 수 없습니다.");
        }
        return user;
    }
    /*
    * 1. code를 통해 access_token 획득
    * 2. access_token통해 user 정보 획득
    * 3. 저장후 jwt secret을 이용해 토큰 발급 및 유저정보 저장
    * */
    public void githubLogin(String code) throws JsonProcessingException{

        Map<String, String> tokenResponseMap = getAccessToken(code);
        GithubUserResponseDto githubUserResponseDto = getUserInformation(tokenResponseMap.get("access_token"));

    }

    private GithubUserResponseDto getUserInformation(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        MultiValueMap<String, String> requestBodys = new LinkedMultiValueMap<>();

        requestHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.add("Authorization", "token " + accessToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(requestBodys, requestHeaders);
        //HTTP 요청하기 -POST 방식으로 - 그리고 response변수의 응답받음.
        String uri = "https://api.github.com/user";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        GithubUserResponseDto githubUserResponseDto =
                objectMapper.readValue(responseEntity.getBody(), GithubUserResponseDto.class);
        log.debug("   ---> User 정보 파싱 전 {}", responseEntity.getBody());
        log.debug("   ---> User 정보");
        log.debug("   ---> User info {}", githubUserResponseDto);
        return githubUserResponseDto;
    }

    private Map<String, String> getAccessToken(String code) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        MultiValueMap<String, String> requestBodys = new LinkedMultiValueMap<>();

        requestHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        requestBodys.add("client_id", clientId);
        requestBodys.add("redirect_uri", redirectUri);
        requestBodys.add("code", code);
        requestBodys.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(requestBodys, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                accessTokenUri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        Map<String, String> responseMap = Splitter
                .on("&")
                .withKeyValueSeparator("=")
                .split(responseEntity.getBody());

        log.debug("   ---> oAuth 토큰 발급 완료");
        log.debug("   ---> access token: {}", responseMap.get("access_token"));
        log.debug("   ---> token type: {}", responseMap.get("scope"));
        log.debug("   ---> scope: {}", responseMap.get("token_type"));

        return responseMap;
    }

    private void validateDuplicateUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (log.isErrorEnabled()) {
                log.error("   ---> 이미 사용중인 이메일 입니다.{}", user);
            }
            throw new DuplicateKeyException("이미 사용중인 이메일 입니다.");
        }
    }

    public String getSHA256Pwd(String rawPwd) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(rawPwd.getBytes());
        String hashedPwd = String.format("%064x", new BigInteger(1, md.digest()));
        return hashedPwd;
    }

}
