package com.laxser.blitz.lama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用：{#link UseMaster} 标注需要强制查询 master 数据库的 DAO 接口方法。
 * 
 * @author laxser  Date 2012-3-22 下午3:38:19
@contact [duqifan@gmail.com]
@UseMaster.java

 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseMaster {

    /**
     * 是否需要强制查询 master 数据库。
     * 
     * @return 强制查询 master 数据库
     */
    boolean value() default true;
}
