package com.nnlk.z1zontodoserver.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@ToString
public class UserUpdateRequestDto {

    @NotNull(message = "name은 필수 값 입니다.")
    private String name;
    @NotNull(message = "password는 필수 값 입니다.")
    private String password;

    private String role;

    @Pattern(regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+",message = "이메일 입력 형식에 맞지 않습니다.")
    private String email;

}
