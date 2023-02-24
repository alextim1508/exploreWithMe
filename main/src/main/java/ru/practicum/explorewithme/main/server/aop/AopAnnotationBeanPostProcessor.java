package ru.practicum.explorewithme.main.server.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.practicum.explorewithme.main.server.aop.annotation.ControllerLog;
import ru.practicum.explorewithme.main.server.exception.MethodArgumentMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AopAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        Arrays.stream(methods).filter(method -> Objects.nonNull(method.getAnnotation(ControllerLog.class)))
                .forEach(m -> {
                    if (!List.of(m.getParameterTypes()).contains(HttpServletRequest.class)) {

                        throw new MethodArgumentMismatchException(String.format(
                            "Method %s annotated @ControllerLog in class %s must contain HttpServletRequest parameter",
                            m.getName(),
                            m.getDeclaringClass().getName()));
                    }
                });
        return bean;
    }
}
