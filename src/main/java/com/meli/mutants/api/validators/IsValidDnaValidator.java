package com.meli.mutants.api.validators;

import com.meli.mutants.api.validators.annotations.IsValidDna;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class IsValidDnaValidator implements ConstraintValidator<IsValidDna, String[]> {
    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        int rows = value.length;
        return Arrays.stream(value).allMatch(v -> v.matches("^[ACGT]+$") && v.length() == rows);
    }
}



