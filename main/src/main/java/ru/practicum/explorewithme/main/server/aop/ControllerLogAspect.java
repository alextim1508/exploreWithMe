package ru.practicum.explorewithme.main.server.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.server.aop.annotation.ControllerLog;
import ru.practicum.explorewithme.stat.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerLogAspect {

    private final StatClient statClient;

    @Around("@annotation(controllerLog)")
    public Object controllerLogAdvice(ProceedingJoinPoint point, ControllerLog controllerLog) throws Throwable {

        Object[] args = point.getArgs();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        HttpServletRequest request = (HttpServletRequest) Arrays.stream(args)
                .filter(arg -> arg instanceof HttpServletRequest)
                .findFirst()
                .orElse(null);

        if (request != null) {
            log.info("Request {} with arguments {}", request.getRequestURI(), Arrays.toString(args));

            try {
                statClient.hit(request);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

        } else {
            log.error("@ControllerLog required HttpServletRequest parameter in method {} of class {}",
                    methodSignature.getMethod().getName(),
                    methodSignature.getMethod().getDeclaringClass().getName()
            );
        }

        return point.proceed(args);
    }
}
