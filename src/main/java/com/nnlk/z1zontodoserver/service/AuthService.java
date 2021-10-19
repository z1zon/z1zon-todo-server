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
import java.util.Optional;


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
    @Value("${oauth.github.github_api_uri}")
    private String githubApiUri;

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
    @Transactional
    public String getJwtByGithubCode(String code) throws JsonProcessingException {

        Map<String, String> tokenResponseMap = getAccessToken(code);
        UserUpsertRequestDto userUpsertRequestDto = getUserInformation(tokenResponseMap.get("access_token"));
        upsertGithubUser(userUpsertRequestDto);
        return tokenProvider.createToken(userUpsertRequestDto.getEmail());
    }

    private void upsertGithubUser(UserUpsertRequestDto userUpsertRequestDto) {
        userRepository
                .findByNameAndProvider(userUpsertRequestDto.getName(), userUpsertRequestDto.getProvider())
                .ifPresentOrElse(user -> user.update(userUpsertRequestDto), () -> userRepository.save(userUpsertRequestDto.toEntity()));
    }

    private UserUpsertRequestDto getUserInformation(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        MultiValueMap<String, String> requestBodys = new LinkedMultiValueMap<>();

        requestHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.add("Authorization", "token " + accessToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(requestBodys, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                githubApiUri,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        GithubUserResponseDto githubUserResponseDto =
                objectMapper.readValue(responseEntity.getBody(), GithubUserResponseDto.class);

        log.debug("   ---> Oauth 유저 정보 {}", githubUserResponseDto);

        String userName = githubUserResponseDto.getLogin();

        return UserUpsertRequestDto.builder()
                .name(userName)
                .email(Optional.ofNullable(githubUserResponseDto.getEmail()).orElse(userName + "@github.com"))
                .provider("github")
                .password(" ")
                .role("public")
                .build();
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
