package com.nnlk.z1zontodoserver.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@ToString
public class UserLoginDto {
    @NotNull(message = "email은 필수 값 입니다.")
    private String email;
    @NotNull(message = "password는 필수 값 입니다.")
    private String password;

}
