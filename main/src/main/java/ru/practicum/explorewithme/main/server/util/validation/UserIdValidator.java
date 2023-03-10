package ru.practicum.explorewithme.main.server.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@Component
@Slf4j
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class UserIdValidator implements ConstraintValidator<UserIdEqualsJwtSubConstraint, Object[]> {

    private int userIdParamIndex;
    private int jwtParamIndex;

    public void initialize(UserIdEqualsJwtSubConstraint annotation) {
        userIdParamIndex = annotation.userIdParamIndex();
        jwtParamIndex = annotation.jwtParamIndex();
    }

    @Override
    public boolean isValid(Object[] params, ConstraintValidatorContext constraintValidatorContext) {
        Long userIdFromUri = (Long) params[userIdParamIndex];
        log.debug("userIdFromUri {}", userIdFromUri);

        Jwt jwt = (Jwt)params[jwtParamIndex];
        String sub = jwt.getClaim("sub");
        log.debug("sub {}", sub);

        String[] split = sub.split(":");
        Long idFromJwt = Long.valueOf(split[split.length - 1]);
        log.debug("idFromJwt {}", idFromJwt);

        log.debug("res {}", idFromJwt.equals(userIdFromUri));

        return idFromJwt.equals(userIdFromUri);
    }
}
