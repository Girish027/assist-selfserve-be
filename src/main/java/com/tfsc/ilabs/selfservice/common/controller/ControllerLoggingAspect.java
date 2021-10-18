package com.tfsc.ilabs.selfservice.common.controller;

import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ravi.b on 31-05-2019.
 */
@Aspect
@Component
public class ControllerLoggingAspect {
    private static final Logger logger =
            LoggerFactory.getLogger(ControllerLoggingAspect.class);

    private long elapsedTime;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
        // not implemented
    }

    @Pointcut("execution(public * *(..))")
    protected void allMethod() {
        // not implemented
    }

    @Around("controller()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object output = pjp.proceed();
        elapsedTime = System.currentTimeMillis() - start;

        return output;
    }

    @Before("controller() && allMethod()")
    public void logBefore(JoinPoint joinPoint) {
        String joinPointArgs = Arrays.toString(joinPoint.getArgs());
        String joinPointStr = toString(joinPoint);
        logger.info("received rest call at {} with arguments {}", joinPointStr, joinPointArgs);
    }

    @AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        int returnValueLength = returnValue.length();
        String joinPointStr = toString(joinPoint);
        logger.info("rest call at {} returning response {}, Method execution time {} milliseconds", joinPointStr,
                returnValue.substring(0, Math.min(returnValueLength, 50)), elapsedTime);
    }

    @AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String joinPointStr = toString(joinPoint);
        String formattedStr = String.format("Exception while processing rest call at %S. Exception", joinPointStr);
        logger.error(formattedStr, exception);
    }

    private String toString(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
    }

    private String getValue(Object result) {
        String returnValue = "";
        try {
            if (null != result) {
                if (result instanceof List) {
                    List res = (List) result;
                    returnValue = BaseUtil.convertListToString(res);
                } else if (result instanceof Set) {
                    Set res = (Set) result;
                    returnValue = BaseUtil.convertSetToString(res);
                } else if (result instanceof Map) {
                    Map res = (Map) result;
                    returnValue = BaseUtil.convertMapToString(res);
                } else {
                    returnValue = result.toString();
                }
            }
        } catch (Exception ex) {
            logger.error("Error when converting Object result to String", ex);
        }
        return returnValue;
    }
}
