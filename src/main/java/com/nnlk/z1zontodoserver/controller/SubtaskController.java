package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubtaskResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubtaskUpsertDto;
import com.nnlk.z1zontodoserver.service.SubTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SubtaskController {

    private final SubTaskService subTaskService;

    @PostMapping("subtask")
    private ResponseDto create(@ApiIgnore @AuthenticationPrincipal User user,
                               @RequestBody @Valid SubtaskUpsertDto subTaskUpsertDto) {

        subTaskService.create(user, subTaskUpsertDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("subtask create success")
                .build();
    }

    @GetMapping("/subtasks/{taskId}")
    private ResponseDto findAll(@ApiIgnore @AuthenticationPrincipal User user,
                                @PathVariable Long taskId) {
        List<SubtaskResponseDto> tasks = subTaskService.findAll(user, taskId);

        return ResponseDto.builder()
                .messsage("subtask lookup success")
                .status(HttpStatus.OK)
                .data(tasks)
                .build();
    }

    @PostMapping("/subtask/update/{subtaskId}")
    public ResponseDto update(@ApiIgnore @AuthenticationPrincipal User user,
                              @RequestBody @Valid SubtaskUpsertDto subTaskUpsertDto,
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
