package ru.clevertec.kalustau.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect describing logging for all application
 * @author Dzmitry Kalustau
 */
@Aspect
@Component
public class ApplicationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void annotatedRepositoryClass() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void annotatedServiceClass() {}

    /**
     * Annotation-level advice that takes control of logging
     * @param joinPoint The join point that represents the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("annotatedRepositoryClass() || annotatedServiceClass()")
    public void writeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String message = createMessage(joinPoint);
        log.info(message);
        joinPoint.proceed();
    }

    /**
     * Advice which is called after throw exception in the service layer
     * @param exception that are thrown in the service layer
     */
    @AfterThrowing(value = "annotatedServiceClass()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        log.error(exception.getMessage());
    }

    private String createMessage(ProceedingJoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("In " + joinPoint.getSignature().getName().toUpperCase() + " method ");
        builder.append("with params " + Arrays.toString(joinPoint.getArgs()));
        builder.append(" in " + joinPoint.getSignature().getDeclaringTypeName() + " class ");
        return builder.toString();
    }
}
