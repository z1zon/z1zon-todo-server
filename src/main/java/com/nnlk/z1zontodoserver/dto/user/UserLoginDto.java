package com.nnlk.z1zontodoserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserLoginDto {
    @NotNull(message = "email은 필수 값 입니다.")
    private String email;
    @NotNull(message = "password는 필수 값 입니다.")
    private String password;
}
