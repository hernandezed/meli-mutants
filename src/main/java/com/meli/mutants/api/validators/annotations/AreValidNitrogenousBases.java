package com.meli.mutants.api.validators.annotations;

import com.meli.mutants.api.validators.AreValidNitrogenousBasesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = AreValidNitrogenousBasesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AreValidNitrogenousBases {
    String message() default "{mutants.dna.invalid.nitrogen.base}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
