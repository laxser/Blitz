package com.laxser.blitz.lama.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供 ConcurrentHashMap 缓存池的 {@link CacheProvider} 实现。
 * 
 * @author laxser  Date 2012-3-22 下午3:40:00
@contact [duqifan@gmail.com]
@MockCacheProvider.java

 */
public class MockCacheProvider implements CacheProvider {

    private ConcurrentHashMap<String, MockCache> caches = new ConcurrentHashMap<String, MockCache>();

    private int defaultMaxSize = 100; // 默认值

    public int getDefaultMaxSize() {
        return defaultMaxSize;
    }

    public void setDefaultMaxSize(int defaultMaxSize) {
        this.defaultMaxSize = defaultMaxSize;
    }

    @Override
    public Cache getCacheByPool(String poolName) {

        MockCache cache = caches.get(poolName);
        if (cache == null) {
            cache = new MockCache(poolName);

            MockCache cacheExist = caches.putIfAbsent(poolName, cache);
            if (cacheExist != null) {
                cache = cacheExist;
            }
        }

        return cache;
    }
}
