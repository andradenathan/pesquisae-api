package net.pesquisae.domain.usecases.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Optional;

public interface CacheService<T> {
    void put(String key, T value);
    Optional<T> get(String key, TypeReference<T> typeRef);
    void evict(String key);
}
