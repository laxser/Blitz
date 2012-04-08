package com.laxser.blitz.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 合并各个window的数据模型到Portal中
 * 
 * @author laxser  Date 2012-4-8 下午5:30:35
@contact [duqifan@gmail.com]
@MergeWindowModel.java

 * @see {@link MergeWindowModelInterceptor}
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MergeWindowModel {
	int PERFORMANCE_PRIV = 20000;
}
