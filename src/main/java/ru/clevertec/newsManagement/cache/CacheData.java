package ru.clevertec.newsManagement.cache;

public interface CacheData<K,V> {

    V put(K key, V o);

    V get(K key);

    void delete(K key);
}
