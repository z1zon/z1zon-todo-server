package com.nnlk.z1zontodoserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(500)
                .sendMessages("INTERNER_SERVER_ERROR")
                .message(ex.getMessage())
                .build();


        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotExistObjectException.class)
    protected ResponseEntity<Object> handleNotExistObjectException(Exception ex, WebRequest webRequest) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(400)
                .sendMessages("BAD_REQUEST")
                .message(ex.getMessage())
                .build();

        log.info(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(400)
                .exception(ex.getFieldError().getField() + "Exception")
                .sendMessages("BAD_REQUEST")
                .message(ex.getFieldError().getDefaultMessage())
                .build();

        log.info(ex.getFieldError().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}