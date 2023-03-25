package ru.clevertec.newsManagement.cache.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import ru.clevertec.newsManagement.cache.CacheData;

import java.lang.reflect.Field;
import java.util.Optional;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class CacheAspect<K, V> {

    private final CacheData<K, V> cacheData;

    @AfterReturning(value = "Pointcats.isPutCacheLayer())",
            returning = "entityDto")
    public void putCache(JoinPoint joinPoint, Object entityDto) throws NoSuchFieldException, IllegalAccessException {
        cacheData.put((K) getFieldId(entityDto), (V) entityDto);
    }

    @After(value = "Pointcats.isRemoveCacheLayer() && args(id)")
    public void removeCache(JoinPoint joinPoint, Object id) {
        cacheData.delete((K) id);
    }

    @Around(value = "Pointcats.isGetCacheLayer() && args(id)")
    public Object getCache(ProceedingJoinPoint pjp, Object id) throws Throwable {
        Object entityDto = cacheData.get((K) id);
        if (entityDto == null) {
            Object proceed = pjp.proceed();
            cacheData.put((K) id, (V) proceed);
            return proceed;
        }
        return entityDto;
    }

    private K getFieldId(Object entityDto) throws NoSuchFieldException, IllegalAccessException {
        Class<?> entityDtoClass = entityDto.getClass();
        Field field = entityDtoClass.getDeclaredField("id");
        field.setAccessible(true);
        return (K) field.get(entityDto);
    }
}
