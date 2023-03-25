package ru.clevertec.newsManagement.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class LFUCacheData<K, V> implements CacheData<K, V> {

    private final HashMap<K, CacheObject<K, V>> cache;
    private final PriorityQueue<CacheObject<K, V>> priorityQueue;
    private final int maxSize;

    public LFUCacheData(int maxSize) {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparing(CacheObject::getClickSize));
        this.maxSize = maxSize;
        this.cache = new HashMap<>(maxSize);
    }

    public V put(K key, V value) {
        deleteUnnecessary();
        putCacheObject((K) key, buildObject((K) key, (V) value));
        log.info("Добавление кеша " + value.toString() + " is: " + cache.containsKey(key));
        cache.forEach((k, v) -> log.debug(k + ": " + v.getObj().toString()));
        return (V) value;
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            return addClick(cache.get(key)).getObj();
        }
        log.info("кеша c ключем " + key + " не существует");
        return null;
    }

    private void deleteUnnecessary() {
        if ((isUnnecessary())) {
            CacheObject<K, V> cacheObject = priorityQueue.poll();
            CacheObject<K, V> remove = cache.remove(cacheObject != null ? cacheObject.getKey() : null);
            log.info("Вытеснение кеша: " + remove.toString());
        }
    }

    public void delete(K key) {
        if (cache.containsKey(key)) {
            priorityQueue.remove(cache.get(key));
            cache.remove(key);
            log.info("Удаление кеша c id " + key + " is: " + !cache.containsKey(key));
        }
    }

    private CacheObject<K, V> buildObject(K key, V obj) {
        CacheObject<K, V> objectCacheObject = new CacheObject<>();
        objectCacheObject.setObj(obj);
        objectCacheObject.setKey(key);
        log.info("Создание сущности кеша: " + objectCacheObject.toString());
        return objectCacheObject;
    }

    private CacheObject<K, V> addClick(CacheObject<K, V> cacheObject) {
        cacheObject.addClickSize();
        putCacheObject(cacheObject.getKey(), cacheObject);
        log.info("Добавление клика в сущность с id: " + cacheObject.getKey() + ", click size: " + cacheObject.getClickSize());
        return cacheObject;
    }

    private boolean isUnnecessary() {
        return cache.size() >= maxSize;
    }

    private void putCacheObject(K key, CacheObject<K, V> cacheObject) {
        if (cache.containsKey(key)) {
            priorityQueue.remove(cacheObject);
        }
        priorityQueue.add(cacheObject);
        cache.put(key, cacheObject);
    }

}
