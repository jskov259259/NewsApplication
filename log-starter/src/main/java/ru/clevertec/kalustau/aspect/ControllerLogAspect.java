package ru.clevertec.kalustau.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ControllerLogAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    @Pointcut("execution(@ru.clevertec.kalustau.annotation.ControllerLog * *(..))")
    private void annotatedMethods() {
    }

    @Pointcut("within(@ru.clevertec.kalustau.annotation.ControllerLog *)")
    private void annotatedClass() {
    }

    @Around("annotatedMethods() || annotatedClass()")
    public void writeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String startMessage = createFirstMessage(joinPoint);
        log.info(startMessage);

        Object methodResult = joinPoint.proceed();

        String finalMessage = createFinalMessage(methodResult);
        log.info(finalMessage);
    }

    private String createFirstMessage(ProceedingJoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("Client executed " + joinPoint.getSignature().getName().toUpperCase() + " method ");
        builder.append("with params " + Arrays.toString(joinPoint.getArgs()));
        builder.append(" in " + joinPoint.getSignature().getDeclaringTypeName() + " class ");
        return builder.toString();
    }

    private String createFinalMessage(Object methodResult) {
        StringBuilder builder = new StringBuilder();
        ResponseEntity responseEntity = (ResponseEntity) methodResult;
        builder.append("Server returned response: status " + responseEntity.getStatusCode());
        builder.append(", result " + responseEntity.getBody());
        return builder.toString();
    }

}
