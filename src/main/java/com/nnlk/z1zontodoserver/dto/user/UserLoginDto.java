package com.nnlk.z1zontodoserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserLoginDto {
    @NotNull(message = "name은 필수 값 입니다.")
    private String name;
    @NotNull(message = "password는 필수 값 입니다.")
    private String password;
}
