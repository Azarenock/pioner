package com.pioner.bankapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Bean
  public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
    return new GenericJackson2JsonRedisSerializer(objectMapper);
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration(GenericJackson2JsonRedisSerializer serializer) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(30))
        .disableCachingNullValues()
        .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(SerializationPair.fromSerializer(serializer));
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
      RedisCacheConfiguration cacheConfiguration) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(cacheConfiguration)
        .withInitialCacheConfigurations(Map.of(
            "userSearch", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(cacheConfiguration.getValueSerializationPair()),
            "accountBalance", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(cacheConfiguration.getValueSerializationPair())
        ))
        .transactionAware()
        .build();
  }
}