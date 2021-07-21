package com.meli.mutants.api.controllers;

import com.meli.mutants.api.dtos.request.DnaSampleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MutantController {

    @PostMapping("/mutant/")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaSampleDto dnaSampleDto) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
