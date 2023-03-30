package ru.clevertec.newsManagement.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

@Slf4j
public class LRUCacheData extends AbstractCacheData implements CacheData {

    private final LinkedHashMap<String, Object> cache;
    private final int maxSize;

    public LRUCacheData(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize);
    }

    @Override
    public void put(String id, Object o) {
        String key = getKey(id, o.getClass().getSimpleName());
        removeEldest();
        cache.put(key, o);
        log.info("добавление кеша " + o.toString() + " is: " + cache.containsKey(key));
        cache.forEach((s, v) -> log.debug(s + ": " + v.toString()));
    }

    public Object get(String key) {
        if (cache.containsKey(key)){
            return cache.get(key);
        }
        log.info("Кеша c ключем " + key + " не существует");
        return null;
    }

    public void delete(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            log.info("удаление кеша c id " + key + " is: " + !cache.containsKey(key));
        }
    }

    private void removeEldest() {
        if ((isMaxSize(cache,maxSize))) {
            final Object remove = cache.remove(cache.entrySet().iterator().next().getKey());
            log.info("вытеснение кеша: " + remove.toString());
        }
    }
}
