package org.esadev.mastermindhelper.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class TraceableAspect {

    @Around("@annotation(Traceable)")
    public Object aroundTraceableMethod(ProceedingJoinPoint point) throws Throwable {
        long start = System.nanoTime();

        UUID id = UUID.randomUUID();
        Object returnedValue = executeWithTrace(point, id);

        log.debug("AOP - #{} - End of method '{}' (duration {} ms). Returned value => {}", UUID.randomUUID(), point.toShortString(), (System.nanoTime() - start), returnedValue);
        return returnedValue;
    }

    private Object executeWithTrace(ProceedingJoinPoint pjp, UUID id) throws Throwable {

        Object[] args = pjp.getArgs();

        try {
            log.debug("AOP - #{} - Starting method '{}' with params => '{}", id, pjp.toShortString(), Arrays.toString(args));
            return pjp.proceed();
        } catch (Throwable throwable) {
            log.error("AOP - #{} - Error while executing method with params => '{} '{}'", id, Arrays.toString(args), pjp.toShortString(), throwable);
            throw throwable;
        }
    }
}
