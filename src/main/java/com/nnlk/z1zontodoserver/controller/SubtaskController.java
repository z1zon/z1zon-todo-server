package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubTaskUpsertDto;
import com.nnlk.z1zontodoserver.service.SubTaskService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class SubtaskController {

    private final SubTaskService subTaskService;

    @PostMapping("subtask")
    private ResponseDto create(@ApiIgnore @AuthenticationPrincipal User user,
                               @RequestBody @Valid SubTaskUpsertDto subTaskUpsertDto) {

        subTaskService.create(user, subTaskUpsertDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("subtask create success")
                .build();
    }

}
