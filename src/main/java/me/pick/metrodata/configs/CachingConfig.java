package me.pick.metrodata.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CachingConfig {
	@Bean
	public CacheManager cacheManager () {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager ();
		cacheManager.setCaffeine (caffeineCacheBuilder ());
		return cacheManager;
	}

	private Caffeine<Object, Object> caffeineCacheBuilder () {
		return Caffeine.newBuilder ().initialCapacity (100).maximumSize (500).expireAfterWrite (Duration.ofMinutes (10));
	}

}
