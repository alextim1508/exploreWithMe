package ru.practicum.explorewithme.main.server.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Constraint(validatedBy = UserIdValidator.class)
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
public @interface UserIdEqualsJwtSubConstraint {

    String message() default "UserId from URI must be equals id from sub claim jwt token";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int userIdParamIndex();
    int jwtParamIndex();
}
