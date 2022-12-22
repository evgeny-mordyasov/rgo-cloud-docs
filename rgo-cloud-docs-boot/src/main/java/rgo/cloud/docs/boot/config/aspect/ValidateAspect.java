package rgo.cloud.docs.boot.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import rgo.cloud.common.api.rest.Request;

@Aspect
@Component
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
        ((Request) jp.getArgs()[0]).validate();
    }
}
