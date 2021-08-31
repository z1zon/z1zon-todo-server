package com.nnlk.z1zontodoserver.controller;


import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import com.nnlk.z1zontodoserver.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/task")
    private ResponseDto create(@AuthenticationPrincipal User user, TaskCreateDto taskCreateDto, Errors errors) {

        if (errors.hasErrors()) {
            // 공통으로 처리할 수 있는 모듈로 빼낸다.
        }

        Long userId = user.getId();
        taskService.createTask(userId, taskCreateDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("task create success.")
                .build();

    }

}
