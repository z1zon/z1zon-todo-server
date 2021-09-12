package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import com.nnlk.z1zontodoserver.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @PostMapping("task")
    private ResponseDto create(@ApiIgnore @AuthenticationPrincipal User user,
                               @RequestBody @Valid TaskCreateDto taskCreateDto) {
        taskService.create(user, taskCreateDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("task create success")
                .build();
    }

    @GetMapping("/tasks")
    private ResponseDto findAll(@AuthenticationPrincipal User user) {
        List<Task> tasks = taskService.findAll(user);

        return ResponseDto.builder()
                .messsage("tasks lookup success")
                .status(HttpStatus.OK)
                .data(tasks)
                .build();
    }

    @PostMapping("/task/update/{taskId}")
    public ResponseDto update(@AuthenticationPrincipal User user,
                              @Valid @RequestBody TaskCreateDto taskCreateDto,
                              @PathVariable Long taskId
    ) {

//        taskService.updateTask(user, taskId, taskCreateDto);

        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long taskId) {
        taskService.deleteTask(user, taskId);

        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }


}