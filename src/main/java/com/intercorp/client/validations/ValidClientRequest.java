package com.intercorp.client.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClientValidator.class)
public @interface ValidClientRequest {
    String message() default "Invalid birth date";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
