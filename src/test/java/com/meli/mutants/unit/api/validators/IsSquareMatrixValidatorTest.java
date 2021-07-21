package com.meli.mutants.unit.api.validators;

import com.meli.mutants.api.validators.IsSquareMatrixValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsSquareMatrixValidatorTest {

    IsSquareMatrixValidator validator = new IsSquareMatrixValidator();

    @Test
    void validate_withFirstRowDifferentLength_returnFalse() {
        String[] value = new String[]{
                "ACT",
                "CTGA",
                "TGAC",
                "GACT"
        };
        assertThat(validator.isValid(value, null))
                .isFalse();
    }

    @Test
    void validate_withDifferentRowAndColumnLength_returnFalse() {
        String[] value = new String[]{
                "ACTG",
                "CTGA",
                "TGAC"
        };
        assertThat(validator.isValid(value, null))
                .isFalse();
    }

    @Test
    void validate_withSquareMatrix_returnTrue() {
        String[] value = new String[]{
                "ACTG",
                "CTGA",
                "TGAC",
                "GACT"
        };
        assertThat(validator.isValid(value, null))
                .isTrue();
    }

    @Test
    void validate_withNull_returnTrue() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}
