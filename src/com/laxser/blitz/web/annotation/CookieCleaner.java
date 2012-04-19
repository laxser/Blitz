/**
 * 
 */
package com.laxser.blitz.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注了这个annotation就会迫使server 清空一下用户的cookie
 * @author laxser  Date 2012-4-8 下午5:29:42
@contact [duqifan@gmail.com]
@CookieCleaner.java

 * 
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CookieCleaner {

}
