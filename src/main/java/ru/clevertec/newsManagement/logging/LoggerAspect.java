package ru.clevertec.newsManagement.logging;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggerAspect {

    private final LogMessageFilter logMessageFilter;

    @Before(value = "Pointcats.isControllerLayer()")
    public void debugLevelControllerRequest(JoinPoint joinPoint) {
        LogMessage logMessage = logMessageFilter.getLogMessage();
        if (logMessage != null) {
            Signature signature = joinPoint.getSignature();
            logMessage.setJavaMethod(signature.getDeclaringTypeName() + "# " + signature.getName());
            log.debug("Request - " + requestMessageController(joinPoint, logMessage));
        }
    }

    @AfterReturning(value = "Pointcats.isControllerLayer()", returning = "result")
    public void debugLevelControllerAnswer(JoinPoint joinPoint, Object result) {
        LogMessage logMessage = logMessageFilter.getLogMessage();
        if (result != null && logMessage != null) {
            ResponseEntity proceed = (ResponseEntity) result;
            logMessage.setHttpStatus(proceed.getStatusCode().toString());
            logMessage.setResponse(Optional.ofNullable(proceed.getBody().toString()).orElseGet(() -> ""));
        } else {
            logMessage.setHttpStatus(HttpStatus.NO_CONTENT.toString());
        }
        log.debug("Answer - " + answerMessageController(logMessage));
    }

    @Around(value = "Pointcats.isServiceLayer()")
    public Object debugLevelService(ProceedingJoinPoint joinPoint) throws Throwable {
        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        Object proceed = joinPoint.proceed();
        try {
            if (proceed != null) {
                log.debug(simpleName + " - " + serviceMessage(joinPoint, proceed));
                return proceed;
            }
        } catch (Exception throwable) {
            log.error(serviceMessage(throwable));
            throw throwable;
        }
        return proceed;
    }

    private String requestMessageController(JoinPoint joinPoint, LogMessage logMessage) {
        return String.format("\n%s %s\nparams %s\nMapped to %s",
                logMessage.getHttpMethod(),
                logMessage.getPath(),
                Arrays.toString(joinPoint.getArgs()),
                logMessage.getJavaMethod());
    }

    private String answerMessageController(LogMessage logMessage) {
        return String.format("\nWriting %s \n%s",
                logMessage.getResponse(),
                logMessage.getHttpStatus());
    }

    private String serviceMessage(ProceedingJoinPoint joinPoint, Object proceed) {
        return String.format("\nmethod %s\narguments %s\nresult %s",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                proceed.toString());
    }

    private String serviceMessage(Exception throwable) {
        return String.format("%s - message: %s",
                throwable.getClass().getSimpleName(),
                throwable.getMessage());
    }
}
