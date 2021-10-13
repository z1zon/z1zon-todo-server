package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubTaskUpsertDto;
import com.nnlk.z1zontodoserver.service.SubTaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
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

    @PostMapping("/task/update/{subtaskId}")
    public ResponseDto update(@ApiIgnore @AuthenticationPrincipal User user,
                              @RequestBody @Valid SubTaskUpsertDto subTaskUpsertDto,
                              @PathVariable Long subtaskId
    ) {
        subTaskService.update(user, subtaskId, subTaskUpsertDto);

        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/subtask/{subtaskId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long subtaskId) {
        subTaskService.delete(user, subtaskId);
        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }




}
