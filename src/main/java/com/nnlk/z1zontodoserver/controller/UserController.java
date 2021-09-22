package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.InvalidKeyException;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/user/update/{userId}")
    public ResponseDto update(@AuthenticationPrincipal User user,
                              @Valid @RequestBody UserUpsertRequestDto userUpsertRequestDto,
                              @PathVariable Long userId) throws InvalidKeyException {
        userService.update(user, userUpsertRequestDto, userId);
        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long userId) throws InvalidKeyException {
        userService.delete(user, userId);

        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }

}
