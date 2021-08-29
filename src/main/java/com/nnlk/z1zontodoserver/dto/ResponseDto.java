package com.nnlk.z1zontodoserver.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    int status;
    T data;
}
