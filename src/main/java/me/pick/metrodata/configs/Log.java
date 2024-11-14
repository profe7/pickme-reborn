package me.pick.metrodata.configs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Log {

    @Around("execution(* me.pick.metrodata.services.*.*(..)) || execution(* me.pick.metrodata.controllers.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().toShortString();
        System.out.println(methodName + " executed in " + duration + "ms");

        return result;
    }
}
