package com.nnlk.z1zontodoserver.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

    private Integer code;

    private String exception;

    private String sendMessages;

    private String message;

}
