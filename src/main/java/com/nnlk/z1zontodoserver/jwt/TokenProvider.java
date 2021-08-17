package com.nnlk.z1zontodoserver.jwt;

import com.nnlk.z1zontodoserver.service.UserService;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.nnlk.z1zontodoserver.jwt.JwtFilter.AUTHORIZATION_HEADER;

@Component
public class TokenProvider{

    private static final String AUTHORITIES_KEY = "auth";

    private UserService userService;
    private String secret;
    private final long tokenValidityInMilliseconds;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }
    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    /*
    * createToken 메소드는 Authentication 객체에 포함되어 있는 권한 정보들을 담은 토큰을 생성
    * jwt.token-validity-in-seconds 값을 이용해 토큰의 만료 시간을 지정합니다.
    */
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidityInMilliseconds)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secret)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
    /*
    * Todo 로거 사용 협의 후 sout 교체
    * 토큰 검증 수행
    */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
            //logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
            //logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
            //logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
            //logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}