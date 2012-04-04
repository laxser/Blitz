package com.laxser.blitz.lama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用：{#link CacheDelete} 标注需要在执行后清除缓存的 DAO 接口方法。
 * 
 *@author laxser  Date 2012-3-22 下午3:30:14
@contact [duqifan@gmail.com]
@CacheDelete.java
*
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheDelete {

    /**
     * 标注需要清除的缓存 Pool
     * 
     * @return 缓存 Pool
     */
    String pool() default "default";

    /**
     * 标注需要清除的缓存 Key.
     * 
     * @return 缓存 Key
     */
    String[] key();
}
