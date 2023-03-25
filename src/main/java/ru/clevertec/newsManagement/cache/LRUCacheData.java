package ru.clevertec.newsManagement.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class LRUCacheData<K, V> implements CacheData<K, V> {

    private final LinkedHashMap<K, V> cache;
    private final int maxSize;

    public LRUCacheData(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize);
    }

    public V put(K key, V o) {
        removeEldest();
        cache.put(key, o);
        log.info("добавление кеша " + o.toString() + " is: " + cache.containsKey(key));
        cache.forEach((s, v) -> log.debug(s + ": " + v.toString()));
        return o;
    }

    public V get(K key) {
        if (cache.containsKey(key)){
            return cache.get(key);
        }
        log.info("Кеша c ключем " + key + " не существует");
        return null;
    }

    public void delete(K key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            log.info("удаление кеша c id " + key + " is: " + !cache.containsKey(key));
        }
    }

    private boolean isEldestEntry() {
        return cache.size() >= maxSize;
    }

    private void removeEldest() {
        if ((isEldestEntry())) {
            final V remove = cache.remove(cache.entrySet().iterator().next().getKey());
            log.info("вытеснение кеша: " + remove.toString());
        }
    }
}
