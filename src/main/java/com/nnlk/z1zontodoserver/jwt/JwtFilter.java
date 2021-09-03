package com.nnlk.z1zontodoserver.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
* Todo GenericFilterBean의 경우 매 서블릿마다 실행되는 것처럼 보인다.
* 교체 가능하다고 판단된다면 추후에 OncePerRequestFilter로 교체하자.
*/
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    /*
    * doFilter 메소드는 jwt 토큰의 인증 정보를 현재 실행중인 스레드 ( Security Context ) 에 저장합니다.
    */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
        // 유효한 토큰인지 확인합니다.
        if (token != null && tokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = tokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
