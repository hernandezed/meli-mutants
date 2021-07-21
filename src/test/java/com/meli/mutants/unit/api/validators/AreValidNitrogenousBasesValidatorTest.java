package com.meli.mutants.unit.api.validators;

import com.meli.mutants.api.validators.AreValidNitrogenousBasesValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AreValidNitrogenousBasesValidatorTest {

    AreValidNitrogenousBasesValidator validator = new AreValidNitrogenousBasesValidator();

    @Test
    void validate_withOnlyTCGA_allStringOfSameSize_lengthGreaterThanFour_returnTrue() {
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
    void validate_withOtherLetterThantTCGA_returnFalse() {
        String[] value = new String[]{
                "ACTG",
                "CTGA",
                "TRAC",
                "GACT"
        };
        assertThat(validator.isValid(value, null))
                .isFalse();
    }

    @Test
    void validate_withNull_returnTrue() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
