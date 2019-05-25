package com.gzpy.demo.config;

import java.net.UnknownHostException;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.format.support.DefaultFormattingConversionService;

/** 在缓存的自动把配置中，import会选择哪一个缓存，如果我们导入了redis场景启动器，会默认选择RedisCacheConfiguration
 * static {
		Map<CacheType, Class<?>> mappings = new HashMap<CacheType, Class<?>>();
		mappings.put(CacheType.GENERIC, GenericCacheConfiguration.class);
		mappings.put(CacheType.EHCACHE, EhCacheCacheConfiguration.class);
		mappings.put(CacheType.HAZELCAST, HazelcastCacheConfiguration.class);
		mappings.put(CacheType.INFINISPAN, InfinispanCacheConfiguration.class);
		mappings.put(CacheType.JCACHE, JCacheCacheConfiguration.class);
		mappings.put(CacheType.COUCHBASE, CouchbaseCacheConfiguration.class);
		mappings.put(CacheType.REDIS, RedisCacheConfiguration.class);
		mappings.put(CacheType.CAFFEINE, CaffeineCacheConfiguration.class);
		addGuavaMapping(mappings);
		mappings.put(CacheType.SIMPLE, SimpleCacheConfiguration.class);
		mappings.put(CacheType.NONE, NoOpCacheConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}
	1、redisCacheManager 注入容器是在 RedisCacheConfiguration中。
	在新版本的jar中，use_prefix属性是在RedisCacheConfiguration指定，而其值都是 CacheProperties取
	2、对于@cacheable等注解，都是从cacheManager中取缓存来操作的，而在1.8版本中，redisCacheManager与
	2.15版本中的redisCacheManager发生了改变，用RedisCacheWriter取代了redisOperations（也就是redisTemplate）
	一些相关的配置，比如keySerializer等，都存放在了RedisCacheConfiguration，之前是redisTemplate）中，
	所以我们想要更改配置，修改redisTemplate是无效的。
	
	
 * */
@Configuration
public class RedisConfig {
	//在新版本无效
//	@Bean(name = "redisTemplate")
//	public RedisTemplate<Object, Object> redisTemplate(
//			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//		RedisTemplate<Object, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory);
//		Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//		template.setDefaultSerializer(jsonSerializer);
//		template.setKeySerializer(jsonSerializer);
//		template.setValueSerializer(jsonSerializer);
//		return template;
//	}
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
			ResourceLoader resourceLoader) {
		RedisCacheManagerBuilder builder = RedisCacheManager
				.builder(redisConnectionFactory);
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		RedisCacheConfiguration configuraton = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class)));
		builder.cacheDefaults(configuraton);
		return builder.build();
	}
	
}
