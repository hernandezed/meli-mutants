package com.meli.mutants.api.validators.annotations;

import com.meli.mutants.api.validators.IsValidDnaValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Constraint(validatedBy = IsValidDnaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsValidDna {
}
