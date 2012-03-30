package com.laxser.blitz.lama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jade支持DAO方法返回Map形式的，默认情况下Jade选取第一列作为Map的key。
 * <p>
 * 我们推荐您在写返回map的SQL时，把key放到第一列，但是如果真不想这样做，你可以通过本注解，即{@link KeyColumnOfMap}
 * 进行指定。
 * 
*@author laxser  Date 2012-3-22 下午3:29:46
@contact [duqifan@gmail.com]
@KeyColumnOfMap.java

*
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KeyColumnOfMap {

    /**
     * 指出要被当成map key的字段名称
     * 
     * @return
     */
    String value();
}
