package org.esadev.mastermindhelper.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
@Log4j2
public class TraceableAspect {

    private static final AtomicLong count = new AtomicLong(0);

    @Around("@annotation(Traceable)")
    public Object aroundTraceableMethod(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();

        long id = count.incrementAndGet();
        Object returnedValue = executeWithTrace(point, id);

        log.debug("AOP - #{} - End of method '{}' (duration {} ms). Returned value => {}", id, point.toShortString(), (System.currentTimeMillis() - start), returnedValue);
        return returnedValue;
    }

    private Object executeWithTrace(ProceedingJoinPoint pjp, long id) throws Throwable {

        Object[] args = pjp.getArgs();

        try {
            log.trace("AOP - #{} - Starting method '{}' with params => '{}", id, pjp.toShortString(), Arrays.toString(args));
            return pjp.proceed();
        } catch (Throwable throwable) {
            log.error("AOP - #{} - Error while executing method with params => '{} '{}' : {} ", id, Arrays.toString(args), pjp.toShortString(), throwable);
            throw throwable;
        }
    }
}
