package com.laxser.blitz.lama.cache;

/**
 * 定义 CacheProvider 接口从缓存池名称获取实例。
 * 
 * @author laxser  Date 2012-3-22 下午3:39:39
@contact [duqifan@gmail.com]
@CacheProvider.java
 */
public interface CacheProvider {

    /**
     * 从缓存池的名称获取实例。
     * 
     * @param poolName - 缓存池的名称
     * 
     * @return 缓存池实例
     */
    Cache getCacheByPool(String poolName);


}
