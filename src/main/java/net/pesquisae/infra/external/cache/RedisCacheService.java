package net.pesquisae.infra.external.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pesquisae.domain.usecases.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheService<T> implements CacheService<T> {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CACHE_PREFIX = "pesquisae:";
    private static final long CACHE_EXPIRATION_TIME = 3600;

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    public RedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void put(String key, T value) {
        try {
            String keyWithPrefix = CACHE_PREFIX + key;
            redisTemplate.opsForValue().set(
                    keyWithPrefix,
                    objectMapper.writeValueAsString(value),
                    CACHE_EXPIRATION_TIME,
                    TimeUnit.SECONDS);
        } catch (Exception exception) {
            logger.error("Erro ao tentar colocar resultado no cache {}", exception.getMessage());
        }
    }

    @Override
    public Optional<T> get(String key, TypeReference<T> typeRef) {
        String keyWithPrefix = CACHE_PREFIX + key;
        String value = redisTemplate.opsForValue().get(keyWithPrefix);
        if (value == null || value.isBlank()) return Optional.empty();

        try {
            T result = objectMapper.readValue(value, typeRef);
            return Optional.of(result);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(CACHE_PREFIX + key);
    }
}
