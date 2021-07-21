package com.meli.mutants.api.controllers;

import com.meli.mutants.api.dtos.request.DnaSampleDto;
import com.meli.mutants.business.usecases.EvaluateDnaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final EvaluateDnaUseCase evaluateDnaUseCase;

    @PostMapping("/mutant/")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaSampleDto dnaSampleDto) {
        if (evaluateDnaUseCase.execute(dnaSampleDto.toDnaSampleBO())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

}
