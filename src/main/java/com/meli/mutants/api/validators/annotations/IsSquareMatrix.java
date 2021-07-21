package com.meli.mutants.api.validators.annotations;

import com.meli.mutants.api.validators.IsSquareMatrixValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = IsSquareMatrixValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsSquareMatrix {
    String message() default "{mutants.dna.no.square}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
