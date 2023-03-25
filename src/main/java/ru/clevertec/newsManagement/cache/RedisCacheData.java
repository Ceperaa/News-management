package ru.clevertec.newsManagement.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Slf4j
public class RedisCacheData<K, V> implements CacheData<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    @Override
    public V put(K key, V o) {
        String s = String.valueOf(key);
        redisTemplate.opsForValue().set((K) s, o);

        redisTemplate.keys((K) "*")
                .stream()
                .map((k) -> redisTemplate.opsForValue().get(k))
                .forEach((v) -> log.info(key + ": " + v.toString()));
        return o;
    }

    @Override
    public V get(K key) {
        String s = String.valueOf(key);
        return redisTemplate.opsForValue().get(s);
    }

    @Override
    public void delete(K key) {
        String s = String.valueOf(key);
        boolean isRemove = redisTemplate.delete((K) s);
        log.info("удаление из кеша " + key + " " + isRemove);
    }
}
