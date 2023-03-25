package ru.clevertec.newsManagement.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsManagement.model.News;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CustomerSpecifications {

    // TODO: убрать removePaging  или page из контроллера
    public static <E> Specification<E> byMultipleParams(Map<String, String> params, Class<?> entityClass) {
        if (params == null) return null;
        removePaging(params);
        params = getFields(params, entityClass);
        AtomicReference<Specification<E>> specification = new AtomicReference<>(Specification.where(null));
        params.entrySet()
                .stream()
                .<Specification<News>>map(param -> (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get(param.getKey()), param.getValue()))
                .map(spec -> specification.getAndSet(specification.get().and((Specification<E>) spec)))
                .collect(Collectors.toList());
        return specification.get();
    }

    private static void removePaging(Map<String, String> params) {
        String page = "page";
        String size = "size";
        if (params.containsKey(page) || params.containsKey(size)) {
            params.remove(page);
            params.remove(size);
        }
    }

    private static <E> Map<String, String> getFields(Map<String, String> params, Class<E> tClass) {
        Map<String, String> map = new HashMap<>();
        Arrays
                .stream(tClass.getDeclaredFields())
                .map(Field::getName)
                .forEach(name -> params.entrySet()
                        .stream()
                        .filter(key -> key.getKey().contains(name))
                        .forEach(key -> map.put(key.getKey(), key.getValue())));
        return map;
    }
}
