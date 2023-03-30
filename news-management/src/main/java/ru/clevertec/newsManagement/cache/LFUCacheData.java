package ru.clevertec.newsManagement.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class LFUCacheData extends AbstractCacheData implements CacheData {

    private final HashMap<String, CacheObject> cache;
    private final PriorityQueue<CacheObject> priorityQueue;
    private final int maxSize;

    public LFUCacheData(int maxSize) {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparing(CacheObject::getClickSize));
        this.maxSize = maxSize;
        this.cache = new HashMap<>(maxSize);
    }

    public void put(String id, Object value) {
        String key = getKey(id, value.getClass().getSimpleName());
        deleteUnnecessary();
        putCacheObject(key, buildObject(key, value));
        log.info("Добавление кеша " + value.toString() + " is: " + cache.containsKey(key));
        cache.forEach((k, v) -> log.debug(k + ": " + v.getObj().toString()));
    }

    public Object get(String key) {
        if (cache.containsKey(key)) {
            return addClick(cache.get(key)).getObj();
        }
        log.info("кеша c ключем " + key + " не существует");
        return null;
    }

    private void deleteUnnecessary() {
        if (isMaxSize(cache, maxSize)){
            CacheObject cacheObject = priorityQueue.poll();
            CacheObject remove = cache.remove(cacheObject != null ? cacheObject.getKey() : null);
            log.info("Вытеснение кеша: " + remove.toString());
        }
    }

    public void delete(String key) {
        if (cache.containsKey(key)) {
            priorityQueue.remove(cache.get(key));
            cache.remove(key);
            log.info("Удаление кеша c id " + key + " is: " + !cache.containsKey(key));
        }
    }

    private CacheObject buildObject(String key, Object obj) {
        CacheObject objectCacheObject = new CacheObject();
        objectCacheObject.setObj(obj);
        objectCacheObject.setKey(key);
        log.info("Создание сущности кеша: " + objectCacheObject.toString());
        return objectCacheObject;
    }

    private CacheObject addClick(CacheObject cacheObject) {
        cacheObject.addClickSize();
        putCacheObject(cacheObject.getKey(), cacheObject);
        log.info("Добавление клика в сущность с id: " + cacheObject.getKey() + ", click size: " + cacheObject.getClickSize());
        return cacheObject;
    }


    private void putCacheObject(String key, CacheObject cacheObject) {
        if (cache.containsKey(key)) {
            priorityQueue.remove(cacheObject);
        }
        priorityQueue.add(cacheObject);
        cache.put(key, cacheObject);
    }

}
