package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.dto.ResponseDto;
import com.nnlk.z1zontodoserver.dto.user.UserCreateDto;
import com.nnlk.z1zontodoserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/user")
    public ResponseDto<Integer> save(@RequestBody @Valid UserCreateDto userCreateDto) {
        try{
            userService.registerUser(userCreateDto);
        }
        catch(RuntimeException e){
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR.value(),2);
        }

        return new ResponseDto<Integer>(HttpStatus.ACCEPTED.value(),1);
    }
}
