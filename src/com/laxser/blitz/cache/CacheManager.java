/**
 * 
 */
package com.laxser.blitz.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;



/**
 * 对于每一个访问来说，有自己的Cache。对于每一个请求来说，可以假设用户在这个时间内访问同一个数据是不变的，比方说自己的名字，姓别等等
 * 
 *@author laxser  Date 2012-4-8 下午5:27:28
@contact [duqifan@gmail.com]
@CacheManager.java

 *
 */
public class CacheManager implements Map<Object,Object>{

    /**
     * 
     * @see java.util.Map#clear()
     */
    public void clear() {
        cache.clear();
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    /**
     * @param value
     * @return
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    /**
     * @return
     * @see java.util.Map#entrySet()
     */
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        return cache.entrySet();
    }

    /**
     * @param o
     * @return
     * @see java.util.Map#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return cache.equals(o);
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(Object key) {
        return cache.get(key);
    }

    /**
     * @return
     * @see java.util.Map#hashCode()
     */
    @Override
    public int hashCode() {
        return cache.hashCode();
    }

    /**
     * @return
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * @return
     * @see java.util.Map#keySet()
     */
    public Set<Object> keySet() {
        return cache.keySet();
    }

    /**
     * @param key
     * @param value
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value) {
        return cache.put(key, value);
    }

    /**
     * @param m
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends Object, ? extends Object> m) {
        cache.putAll(m);
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key) {
        return cache.remove(key);
    }

    /**
     * @return
     * @see java.util.Map#size()
     */
    public int size() {
        return cache.size();
    }

    /**
     * @return
     * @see java.util.Map#values()
     */
    public Collection<Object> values() {
        return cache.values();
    }

    
    
    public static final String KEY = CacheManager.class.getName();
    
    private ConcurrentMap<Object,Object> cache = new ConcurrentHashMap<Object,Object>();
    
    
    /**
     * @return the holder
     */
    public HttpServletRequest getHolder() {
        return holder;
    }



    private HttpServletRequest holder = null;

    /**
     * @param holder
     */
    public CacheManager(HttpServletRequest holder) {
        super();
        this.holder = holder;
    }
}
