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

    public static <E> Specification<E> byMultipleParams(Map<String, String> params) {
        if (params == null) return null;
        removePage(params);
        AtomicReference<Specification<E>> specification = new AtomicReference<>(Specification.where(null));
        params.entrySet()
                .stream()
                .<Specification<News>>map(param -> (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get(param.getKey()), param.getValue()))
                .map(spec -> specification.getAndSet(specification.get().and((Specification<E>) spec)))
                .collect(Collectors.toList());
        return specification.get();
    }

    private static Map<String, String> removePage(Map<String, String> params) {
        if (params.containsKey("page")){
            params.remove("page");
        }
        if (params.containsKey("size")){
            params.remove("size");
        }
        return params;
    }
}
