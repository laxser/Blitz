package com.laxser.blitz.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.laxser.blitz.util.TimeCostUtil;

/**
 * 性能监控的拦截器，将每个方法的执行时间记录下来，此类依赖{@link TimeCostUtil}
 * @author xylz(xylz@live.cn)
 * @since 20091106
 * @see {@link TimeCostUtil}
 *
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Performance {
	int PERFORMANCE_PRIV = 20000;
}
