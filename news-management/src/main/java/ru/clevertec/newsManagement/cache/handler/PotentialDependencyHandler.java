package ru.clevertec.newsManagement.cache.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import ru.clevertec.newsManagement.cache.CacheData;
import ru.clevertec.newsManagement.cache.aop.annotations.PutCache;
import ru.clevertec.newsManagement.cache.aop.annotations.RemoveCache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.clevertec.newsManagement.cache.handler.Operation.ADD;
import static ru.clevertec.newsManagement.cache.handler.Operation.REMOVE;

@RequiredArgsConstructor
public class PotentialDependencyHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CacheData cacheData;

    /**
     * build DataObject  для дальнейшего добавления зависимости
     *
     * @param joinPoint       данные о точке соединения
     * @param dependentEntity зависимой сущности
     */
    public void observerAdd(JoinPoint joinPoint, Object dependentEntity) {
        PutCache putCache = getMethod(joinPoint).getAnnotation(PutCache.class);
        observer(DataObject.builder()
                .dependentEntity(dependentEntity)
                .fieldName(putCache.sourceFieldEntity())
                .typeEntityName(putCache.typeEntity().getSimpleName())
                .targetFieldEntity(putCache.targetFieldEntity())
                .typeEntity(putCache.typeEntity())
                .operation(ADD)
                .build());
    }

    /**
     * build DataObject  для дальнейшего удаления зависимости
     *
     * @param joinPoint данные о точке соединения
     * @param id        зависимой сущности
     */
    public void observerRemove(JoinPoint joinPoint, String id) {
        RemoveCache removeCache = getMethod(joinPoint).getAnnotation(RemoveCache.class);
        String simpleName = removeCache.nameKey();
        String key = getKey(id, simpleName);
        Object dependentEntity = cacheData.get(key);
        observer(DataObject.builder()
                .dependentEntity(dependentEntity)
                .fieldName(removeCache.sourceFieldEntity())
                .typeEntityName(removeCache.typeEntity().getSimpleName())
                .targetFieldEntity(removeCache.targetFieldEntity())
                .typeEntity(removeCache.typeEntity())
                .operation(REMOVE)
                .build());
    }

    /**
     * подготовка главной сущности для ее обновления
     *
     * @param dataObject данные точки соединения
     */
    private void observer(DataObject dataObject) {
        if (dataObject.getDependentEntity() != null) {
            Map fieldMap = objectMapper.convertValue(dataObject.getDependentEntity(), Map.class);
            String keyTargetEntity = getKey(String.valueOf(fieldMap.get(dataObject.getFieldName())),
                    dataObject.getTypeEntity().getSimpleName());
            Object mainEntity = cacheData.get(keyTargetEntity);

            if (mainEntity != null && dataObject.dependentEntity != null) {
                Object targetEntity = Optional.ofNullable(mainEntity)
                        .map((targetEntityBefore) -> objectMapper.convertValue(targetEntityBefore, Map.class))
                        .map((targetEntityMap) -> operationDependentEntity(targetEntityMap, dataObject))
                        .orElseThrow(() -> new RuntimeException("Не удалось обработать объект: " + dataObject.getTypeEntityName()));

                cacheData.put(keyTargetEntity, targetEntity);
            }
        }
    }

    /**
     * обновление главной сущноси(удаление или добавление зависимости)
     *
     * @param targetEntityMap главная сущность
     * @param dataObject      данные точки соединения
     * @return обновленный объект
     */
    private Object operationDependentEntity(Map<String, Object> targetEntityMap, DataObject dataObject) {
        List<Object> fieldList = (List<Object>) targetEntityMap.get(dataObject.getTargetFieldEntity());
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        switch (dataObject.getOperation()) {
            case ADD:
                fieldList.add(dataObject.getDependentEntity());
                break;
            case REMOVE:
                fieldList = deleteDependentEntity(fieldList,
                        objectMapper.convertValue(dataObject.getDependentEntity(), Map.class));
                break;
        }
        targetEntityMap.put(dataObject.getTargetFieldEntity(), fieldList);
        return objectMapper.convertValue(targetEntityMap, dataObject.typeEntity);
    }

    /**
     * Удаление элемента из зависимости главной сущности
     *
     * @param fieldList список зависимых элементов
     * @param map       map - сущность для удаления
     * @return
     */
    private List<Object> deleteDependentEntity(List<Object> fieldList, Map map) {
        return fieldList.stream()
                .map((entity) -> objectMapper.convertValue(entity, Map.class))
                .filter((entityMap) -> !entityMap.get("id").equals(map.get("id")))
                .collect(Collectors.toList());
    }

    /**
     * получение Метода точки соединения
     *
     * @param joinPoint данные о точке соединения
     * @return Method
     */

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    /**
     * @param id          суности
     * @param elementName имя сущности
     * @return id_name
     */
    private String getKey(String id, String elementName) {
        return String.format("%s_%s", id, elementName);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class DataObject {

        Object dependentEntity;
        String fieldName;
        String typeEntityName;
        String targetFieldEntity;
        Class<?> typeEntity;
        Operation operation;
    }
}