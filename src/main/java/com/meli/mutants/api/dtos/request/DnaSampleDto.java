package com.meli.mutants.api.dtos.request;

import com.meli.mutants.api.validators.annotations.AreValidNitrogenousBases;
import com.meli.mutants.api.validators.annotations.IsSquareMatrix;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DnaSampleDto {
    @NotNull(message = "{mutants.dna.not.null}")
    @AreValidNitrogenousBases
    @IsSquareMatrix
    String[] dna;
}
