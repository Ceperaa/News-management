package ru.clevertec.newsManagement.logging;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcats {

    @Pointcut("within(ru.clevertec.newsManagement.controllers.*)")
    public void isControllerLayer() {
    }

    @Pointcut("within(ru.clevertec.newsManagement.servises..*ServiceImpl)")
    public void isServiceLayer() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
    public void isController() {
    }
}
