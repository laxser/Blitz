/**
 * $Id: ServiceCutter.java 27494 2010-08-11 06:19:37Z yan.liu@XIAONEI.OPI.COM $
 * Copyright 2009-2010 Oak Pacific Interactive. All right reserved.
 */
package com.laxser.blitz.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Cutting service while timeout. Put the annotation on class or method,
 * and the priority of method is higher than class if exist.
 * 
 * @author xylz(xylz@live.cn)
 * @since 2010-1-6
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceCutter {

    /**
     * enable service cutting
     */
    boolean enabled() default false;

    /**
     * the timeout for service cutting
     * 
     * @return time in {@link TimeUnit#MILLISECONDS}
     */
    int timeout() default 50;
    /**
     * the max concurrent for Controller class or method<br />
     * The default is -1 (unlimited)
     * @return the max count of concurrent or -1 (never cutting)
     * @since 2010-01-25
     */
    int maxConcurrent() default -1;
    /**
     * the default page (or controller) when reaching the max concurrent
     * @return HTTP CODE 403
     * @since 2010-01-25
     * @see {@link net.paoding.rose.web.instruction.Instruction}
     */
    public String instruction() default "status:403; througput";
}
