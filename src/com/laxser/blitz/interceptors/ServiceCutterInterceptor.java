/**
 * $Id: ServiceCutterInterceptor.java 15847 2010-01-25 07:19:15Z yan.liu@XIAONEI.OPI.COM $
 * Copyright 2009-2010 Oak Pacific Interactive. All right reserved.
 */
package com.laxser.blitz.interceptors;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.util.BlockService;
import com.laxser.blitz.util.BlockService.Cutter;
import com.laxser.blitz.util.ConfigCenter;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;

/**
 * service cutting implementation.
 * 
 * @author xylz(xylz@live.cn)
 * @since 2010-1-6
 */
public class ServiceCutterInterceptor extends ControllerInterceptorAdapter {

    final protected static Log log = LogFactory.getLog(ServiceCutter.class);

    /** 并发计数，KEY为Method,Value为计数 */
    static final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>(
            100);

    final static String BLOCK_SERVICE_CUTTER = "BlockServiceCutter_";

    final String KEY_SERVICECUTTER = ServiceCutter.class.getName();

    @Override
    protected Class<? extends Annotation> getRequiredAnnotationClass() {
        return ServiceCutter.class;
    }

    private String getControllerMethodName(Invocation inv) {
        return inv.getControllerClass().getName()+"#"+inv.getMethod().getName();
    }
    
    @Override
    public Object before(Invocation inv) throws Exception {
        ServiceCutter cutter = inv.getMethod().getAnnotation(ServiceCutter.class);
        if (cutter == null) {
            cutter = inv.getControllerClass().getAnnotation(ServiceCutter.class);
        }
        if (cutter != null) {
            final String methodName = getControllerMethodName(inv);
            inv.setAttribute(BLOCK_SERVICE_CUTTER, BlockService.setEnabledAndTimeout(cutter
                    .enabled(), cutter.timeout()));
            int maxCount = ConfigCenter.getInteger(methodName,cutter.maxConcurrent());
            //===============================================================
            //                   如果开启了并发切断的功能
            //===============================================================
            if (maxCount > 0) {
                AtomicInteger count = map.get(methodName);
                AtomicInteger maxHistoryCount = map.get(methodName + "__max");
                if (count == null) {//初始化
                    count = new AtomicInteger(0);
                    maxHistoryCount = new AtomicInteger(0);
                    map.putIfAbsent(methodName, count);
                    map.putIfAbsent(methodName + "__max", maxHistoryCount);
                }
                if (count.get() >= maxCount) {//达到最大并发
                    log.error(methodName+" --> controller reaches at the max concurrent " + maxCount);
                    return cutter.instruction();
                }
                int currentCount = count.incrementAndGet();
                if (maxHistoryCount.get() < currentCount) {//记录历史最大并发
                    maxHistoryCount.set(currentCount);
                }
                inv.setAttribute(KEY_SERVICECUTTER, count);
            }

            if (log.isDebugEnabled()) {
                log.debug(String.format("%s:ServiceCutter|%s#%s|%s:%s",//
                        Thread.currentThread().getName()//
                        , inv.getControllerClass().getSimpleName()//
                        , methodName//
                        , cutter.enabled(), cutter.timeout()));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Object after(Invocation inv, Object instruction) throws Exception {
        resetEnableAndTimeout(inv);
        return instruction;
    }

    protected void resetEnableAndTimeout(Invocation inv) {

        Cutter cutter = (Cutter) inv.getAttribute(BLOCK_SERVICE_CUTTER);
        if (cutter != null) {
            BlockService.resetCutter(cutter);
            inv.removeAttribute(BLOCK_SERVICE_CUTTER);

        }
        Object o = inv.getAttribute(KEY_SERVICECUTTER);
        if (o != null) {
            AtomicInteger concurrentCount = map.get(getControllerMethodName(inv));
            if (concurrentCount != null) {
                concurrentCount.decrementAndGet();
            }
            inv.removeAttribute(KEY_SERVICECUTTER);
        }
    }

    @Override
    public void afterCompletion(Invocation inv, Throwable ex) throws Exception {
        resetEnableAndTimeout(inv);
    }

    /**
     * get the status for Controller concurrent
     */
    public static final String getStatus() {
        return getStatus("\n");
    }

    public static final String getStatus(String ind) {
        StringBuilder buf = new StringBuilder(2048);
        for (Map.Entry<String, AtomicInteger> e : map.entrySet()) {
            buf.append(e.getKey()).append("=").append(e.getValue()).append(ind);
        }
        return buf.toString();
    }
}
