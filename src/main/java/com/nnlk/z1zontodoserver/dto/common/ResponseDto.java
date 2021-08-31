package com.nnlk.z1zontodoserver.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    HttpStatus status;
    String messsage;
    T data;

}
