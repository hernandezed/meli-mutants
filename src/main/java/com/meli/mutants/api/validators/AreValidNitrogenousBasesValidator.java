package com.meli.mutants.api.validators;

import com.meli.mutants.api.validators.annotations.AreValidNitrogenousBases;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;

public class AreValidNitrogenousBasesValidator implements ConstraintValidator<AreValidNitrogenousBases, String[]> {
    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        return Optional.ofNullable(dna)
                .map(value -> Arrays.stream(dna).allMatch(v -> v.matches("^[ACGT]+$")))
                .orElse(true);
    }
}



