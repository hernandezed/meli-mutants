package com.meli.mutants.api.dtos;

import com.meli.mutants.api.validators.annotations.IsValidDna;
import lombok.Value;

@Value
public class DnaSampleDto {
    @IsValidDna
    String[][] dna;
}
