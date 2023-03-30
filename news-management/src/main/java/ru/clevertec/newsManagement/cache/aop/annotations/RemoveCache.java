package ru.clevertec.newsManagement.cache.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Удаляет кэш сущности
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RemoveCache {

    /**
     * @return имя поля внешнего ключа
     */
    String sourceFieldEntity() default "";

    /**
     * @return имя поля в главной сущности куда сохранить зависимость
     */
    String targetFieldEntity() default "";

    /**
     * @return тип главной сущности
     */
    Class<?> typeEntity() default Object.class;

    String nameKey();
}
