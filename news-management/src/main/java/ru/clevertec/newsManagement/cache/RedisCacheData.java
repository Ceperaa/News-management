package ru.clevertec.newsManagement.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Slf4j
public class RedisCacheData extends AbstractCacheData implements CacheData {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, Object o) {

        redisTemplate.opsForValue()
                .set(key, o);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        boolean isRemove = redisTemplate.delete(key);
        log.info("удаление из кеша " + key + " " + isRemove);
    }
}
