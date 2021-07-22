package com.meli.mutants.api.controllers;

import com.meli.mutants.api.dtos.request.DnaSampleDto;
import com.meli.mutants.api.dtos.response.StatisticsDto;
import com.meli.mutants.business.usecases.EvaluateDnaUseCase;
import com.meli.mutants.business.usecases.GetStatisticsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final EvaluateDnaUseCase evaluateDnaUseCase;
    private final GetStatisticsUseCase getStatisticsUseCase;

    @PostMapping("/mutant/")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaSampleDto dnaSampleDto) {
        if (evaluateDnaUseCase.execute(dnaSampleDto.toDnaSampleBO())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    public StatisticsDto getStats() {
        return StatisticsDto.from(getStatisticsUseCase.execute());
    }

}
