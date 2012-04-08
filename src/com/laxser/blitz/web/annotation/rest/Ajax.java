package com.laxser.blitz.web.annotation.rest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标注请求是Ajax的，因此不算一次完整的HTTP 请求
 * 我们只对里面的一小部分进行替换
 * 
 * @author laxser
 * @ contact qifan.du@renren-inc.com
 * date: 2012-4-7
 */
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Ajax {

}
