package ru.practicum.explorewithme.main.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.explorewithme.main.server.aop.AopAnnotationBeanPostProcessor;

@Configuration
public class AopAnnotationBeanPostProcessorConfig {

    @Bean
    public AopAnnotationBeanPostProcessor aopAnnotationBeanPostProcessor() {
        return new AopAnnotationBeanPostProcessor();
    }
}
