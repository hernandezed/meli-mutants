package com.meli.mutants.api.aspects;

import com.meli.mutants.api.dtos.response.ApiErrorsResponseDto;
import com.meli.mutants.api.dtos.response.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class MeliMutantsControllerAdvice extends ResponseEntityExceptionHandler {

    Map<String, String> codes = new HashMap<>();

    public MeliMutantsControllerAdvice() {
        codes.put("IsSquareMatrix", "mutants.dna.no.square");
        codes.put("AreValidNitrogenousBases", "mutants.dna.invalid.nitrogen.base");
        codes.put("NotNull", "mutants.dna.not.null");
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return ResponseEntity.badRequest().body(new ApiErrorsResponseDto(badRequest.name(), "Invalid request", ex.getBindingResult().getAllErrors().stream().map(err -> new ErrorDto(codes.get(err.getCode()), err.getDefaultMessage())).collect(Collectors.toList())));
    }
}
