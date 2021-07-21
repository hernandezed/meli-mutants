package com.meli.mutants.api.dtos.response;

import lombok.Value;

@Value
public class ErrorDto {
    String code;
    String message;
}
