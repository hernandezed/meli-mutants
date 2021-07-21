package com.meli.mutants.api.validators;

import com.meli.mutants.api.validators.annotations.IsSquareMatrix;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;


public class IsSquareMatrixValidator implements ConstraintValidator<IsSquareMatrix, String[]> {
    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        return Optional.ofNullable(dna)
                .map(value -> Arrays.stream(value).allMatch(v -> v.length() == value.length))
                .orElse(true);
    }
}
