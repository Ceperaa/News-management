package ru.clevertec.newsManagement.cache;

public interface CacheData {

    void put(String key, Object o);

    Object get(String key);

    void delete(String key);
}
