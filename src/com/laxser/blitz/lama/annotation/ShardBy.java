package com.laxser.blitz.lama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对于sql中没有含有散表参数的或散表参数名称和配置的散表名称不一致的，通过将 ShardBy 配置在这个参数上，表示使用该参数进行散表.
 * <p>
 * &#064;SQL(&quot;....where name like :1&quot;)<br>
 * public List<Xxx> find(String likeValue, &#064;ShardBy String pageId);
 * 
 * <pre>
 * </pre>
 * 
 * @author laxser  Date 2012-3-22 下午3:31:45
@contact [duqifan@gmail.com]
@ShardBy.java
 */
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardBy {
}
