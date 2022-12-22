package rgo.cloud.docs.boot.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import rgo.cloud.common.api.rest.Request;

@Aspect
@Component
@Slf4j
public class ValidateAspect {
    private ValidateAspect() {
    }

    @Pointcut("execution(* rgo.cloud.docs.boot.api.decorator.*..*(..))")
    public void decorator() {
    }

    @Pointcut("args(rgo.cloud.common.api.rest.Request)")
    public void request() {
    }

    @Pointcut("@target(rgo.cloud.docs.internal.api.annotation.Validate)")
    public void validateAnnotation() {
    }

    @Before("decorator() && request() && validateAnnotation()")
    public void validateRequest(JoinPoint jp) {
        Request rq = ((Request) jp.getArgs()[0]);
        log.info("{} received: {}", rq.getClass().getSimpleName(), rq);
        rq.validate();
    }
}
