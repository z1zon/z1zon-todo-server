package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.task.TaskUpsertRequestDto;
import com.nnlk.z1zontodoserver.dto.task.TaskResponseDto;
import com.nnlk.z1zontodoserver.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/task")
    private ResponseDto create(@ApiIgnore @AuthenticationPrincipal User user,
                               @RequestBody @Valid TaskUpsertRequestDto taskUpsertRequestDto) {
        taskService.create(user, taskUpsertRequestDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("task create success")
                .build();
    }

    @GetMapping("/tasks")
    private ResponseDto findAll(@ApiIgnore @AuthenticationPrincipal User user) {
        List<TaskResponseDto> tasks = taskService.findAll(user);

        return ResponseDto.builder()
                .messsage("tasks lookup success")
                .status(HttpStatus.OK)
                .data(tasks)
                .build();
    }

    @PostMapping("/task/update/{taskId}")
    public ResponseDto update(@ApiIgnore @AuthenticationPrincipal User user,
                              @RequestBody @Valid TaskUpsertRequestDto taskUpsertRequestDto,
                              @PathVariable Long taskId
    ) {
        taskService.update(user, taskId, taskUpsertRequestDto);

        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long taskId) {
        taskService.delete(user, taskId);
        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }
}
