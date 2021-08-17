package com.nnlk.z1zontodoserver.controller;

import com.nnlk.z1zontodoserver.dto.ResponseDto;
import com.nnlk.z1zontodoserver.dto.user.UserCreateDto;
import com.nnlk.z1zontodoserver.dto.user.UserLoginDto;
import com.nnlk.z1zontodoserver.jwt.JwtFilter;
import com.nnlk.z1zontodoserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> save(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        try{
            HttpHeaders httpHeaders = new HttpHeaders();
            if(errors.hasErrors()) {
                System.out.println(String.format("   ---> %s" ,errors.getFieldError().toString()));
                return new ResponseEntity<String>("wrong field",HttpStatus.BAD_REQUEST);
            }
            userService.register(userCreateDto);
        }
        catch(RuntimeException e){
            return new ResponseEntity<String>("runtime error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<String>("save success",httpHeaders,HttpStatus.ACCEPTED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors) {
        if(errors.hasErrors()){
            System.out.println(String.format("   ---> %s" ,errors.getFieldError().toString()));
            return new ResponseEntity<String>("wrong field",HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        try{
            String jwtToken = userService.login(userLoginDto);
            httpHeaders.setBearerAuth(jwtToken);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>("password wrong",httpHeaders,HttpStatus.BAD_REQUEST);
        }catch (DisabledException e) {
            return  new ResponseEntity<String>("token wrong",httpHeaders,HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("token wrong",httpHeaders,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("login Success",httpHeaders,HttpStatus.OK);
    }
}
