package ru.clevertec.newsManagement.cache.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcats {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isServiceLayer() {
    }

    @Pointcut("isServiceLayer() && @annotation(ru.clevertec.newsManagement.cache.aop.annotations.PutCache)")
    public void isPutCacheLayer() {
    }

    @Pointcut("isServiceLayer() && @annotation(ru.clevertec.newsManagement.cache.aop.annotations.GetCache)")
    public void isGetCacheLayer() {
    }

    @Pointcut("isServiceLayer() && @annotation(ru.clevertec.newsManagement.cache.aop.annotations.RemoveCache)")
    public void isRemoveCacheLayer() {
    }
}
