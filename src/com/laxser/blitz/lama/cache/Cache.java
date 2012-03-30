package com.laxser.blitz.lama.cache;

/**
 * 定义 Cache 实例，一个 Cache 实例代表一个缓存实例，支持其中的 get, set 等缓存操作。
 * 
 * @author laxser  Date 2012-3-22 下午3:39:28
@contact [duqifan@gmail.com]
@Cache.java
 */
public interface Cache {

    /**
     * 从 Cache 缓存池取出对象。
     * 
     * @param key - 缓存关键字
     * 
     * @return 之前缓存的对象。
     */
    Object get(String key);

    /**
     * 将缓存的对象放入 Cache 缓存池。
     * 
     * @param key - 缓存关键字
     * @param value - 缓存的对象
     * @param expiry - 缓存过期时间
     * 
     * @return 之前缓存的对象。
     */
    boolean set(String key, Object value, int expiry);

    /**
     * 从 Cache 缓存池删除对象。
     * 
     * @param key - 缓存关键字
     */
    boolean delete(String key);
}
