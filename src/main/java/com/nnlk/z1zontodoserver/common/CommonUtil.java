package com.nnlk.z1zontodoserver.common;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CommonUtil {

    public Cookie setJwtCookie(String jwt){
        Cookie cookie = new Cookie("Bearer",jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24);
        return cookie;
    }
}
