package ru.clevertec.newsManagement.cache.aop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import ru.clevertec.newsManagement.cache.CacheData;
import ru.clevertec.newsManagement.cache.handler.PotentialDependencyHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class CacheAspect {

    private final CacheData cacheData;
    private PotentialDependencyHandler handler;

    @PostConstruct()
    public void into(){
       handler = new PotentialDependencyHandler(cacheData);
    }

    @AfterReturning(value = "PointcatsCache.isPutCacheLayer())",
            returning = "entityDto")
    public void putCache(JoinPoint joinPoint, Object entityDto) throws NoSuchFieldException, IllegalAccessException {
        final String fieldId = getFieldId(entityDto);
        String key1 = getKey(fieldId, entityDto.getClass().getSimpleName());
        cacheData.put(key1, entityDto);
        handler.observerAdd(joinPoint,entityDto);
    }

    @After(value = "PointcatsCache.isRemoveCacheLayer()")
    public void removeCache(JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        handler.observerRemove(joinPoint, String.valueOf(arg));
        cacheData.delete(String.valueOf(arg));
    }

    @Around(value = "PointcatsCache.isGetCacheLayer() && args(id)&&target(service)")
    public Object getCache(ProceedingJoinPoint pjp, Object id, Object service) throws Throwable {
        String res = getKey(String.valueOf(id), returningTypeName(pjp));
        Object entityDto = cacheData.get(res);
        if (entityDto == null) {
            Object proceed = pjp.proceed();
            cacheData.put(String.valueOf(id), proceed);
            return proceed;
        }
        return entityDto;
    }

    private String returningTypeName(JoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        return method.getReturnType().getSimpleName();
    }

    private String getFieldId(Object entityDto) throws NoSuchFieldException, IllegalAccessException {
        Class<?> entityDtoClass = entityDto.getClass();
        Field field = entityDtoClass.getDeclaredField("id");
        field.setAccessible(true);
        return field.get(entityDto).toString();
    }

    private String getKey(String id, String elementName) {
        return String.format("%s_%s", id, elementName);
    }
}
