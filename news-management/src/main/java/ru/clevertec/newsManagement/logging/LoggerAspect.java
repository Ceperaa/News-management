package ru.clevertec.newsManagement.logging;

import jakarta.annotation.PostConstruct;
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

/**
 * Логирование контроллеров и сервисов
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggerAspect {

    private final LogMessageFilter logMessageFilter;
    private LogMessage logMessage;

    @PostConstruct
    public void init() {
        logMessage = (logMessage == null) ? new LogMessage() :
                logMessageFilter.getLogMessages();
    }

    /**
     * сообщение c подробностями о начале запроса
     *
     * @param joinPoint данные о точке соединения
     */
    @Before(value = "Pointcats.isControllerLayer()")
    public void debugLevelControllerRequest(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        logMessage.setJavaMethod(String.format("%s# %s", signature.getDeclaringTypeName(), signature.getName()));
        log.debug("Request - " + requestMessageController(joinPoint, logMessage));
    }

    /**
     * ответ c подробностями о результазе запроса
     *
     * @param joinPoint данные о точке соединения
     * @param result    результат запроса
     */
    @AfterReturning(value = "Pointcats.isControllerLayer()", returning = "result")
    public void debugLevelControllerAnswer(JoinPoint joinPoint, Object result) {
        if (result != null && result.getClass().isAssignableFrom(ResponseEntity.class)) {
            ResponseEntity proceed = (ResponseEntity) result;
            logMessage.setHttpStatus(proceed.getStatusCode().toString());
            logMessage.setResponse(Optional.ofNullable(proceed.getBody().toString()).orElseGet(() -> ""));
        } else {
            logMessage.setHttpStatus(HttpStatus.NO_CONTENT.toString());
        }
        log.debug("Answer - " + answerMessageController(logMessage));
    }

    /**
     * соощение о приходящих параметрах и результате работы
     *
     * @param joinPoint данные о точке соединения
     * @return результат работы метода
     * @throws Throwable
     */
    @Around(value = "Pointcats.isServiceLayer()")
    public Object debugLevelService(ProceedingJoinPoint joinPoint) throws Throwable {
        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        Object proceed = joinPoint.proceed();
        try {
            if (proceed != null) {
                log.debug(String.format("%s - %s", simpleName, serviceMessage(joinPoint, proceed)));
                return proceed;
            }
        } catch (Exception throwable) {
            log.error(serviceMessage(throwable));
            throw throwable;
        }
        return proceed;
    }

    private String requestMessageController(JoinPoint joinPoint, LogMessage logMessages) {
        return String.format("\n%s %s\nparams %s\nMapped to %s",
                logMessages.getHttpMethod(),
                logMessages.getPath(),
                Arrays.toString(joinPoint.getArgs()),
                logMessages.getJavaMethod());
    }

    private String answerMessageController(LogMessage logMessages) {
        return String.format("\nWriting %s \n%s",
                logMessages.getResponse(),
                logMessages.getHttpStatus());
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
