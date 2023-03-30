package ru.clevertec.newsManagement.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.clevertec.newsManagement.cache.aop.CacheAspect;

@Configuration
public class CacheConfig {

    @Value("${app.cache.max_size}")
    private int maxSize;

    @Value("${spring.data.redis.host}")
    private String REDIS_HOST;
    @Value("${spring.data.redis.port}")
    private Integer REDIS_PORT;

    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "algorithm", havingValue = "LFU")
    public CacheAspect cacheDataLFU() {
        return new CacheAspect(new LFUCacheData(maxSize));
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "algorithm", havingValue = "LRU")
    public CacheAspect cacheDataLRU() {
        return new CacheAspect(new LRUCacheData(maxSize));
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "algorithm", havingValue = "redis")
    public CacheAspect cacheDataRedis(RedisTemplate<String, Object> redisTemplate) {
        return new CacheAspect(new RedisCacheData(redisTemplate));
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "algorithm", havingValue = "redis")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "algorithm", havingValue = "redis")
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder() {

            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            }
        };
    }
}
