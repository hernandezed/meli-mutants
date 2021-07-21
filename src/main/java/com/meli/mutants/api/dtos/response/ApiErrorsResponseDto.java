package com.meli.mutants.api.dtos.response;

import lombok.Value;

import java.util.List;

@Value
public class ApiErrorsResponseDto {
    String code;
    String message;
    List<ErrorDto> errors;
}
