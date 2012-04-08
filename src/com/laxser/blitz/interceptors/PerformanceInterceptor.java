package com.laxser.blitz.interceptors;

import java.lang.annotation.Annotation;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.util.TimeCostUtil;
import com.laxser.blitz.web.ControllerInterceptor;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;

/**
 * a {@link ControllerInterceptor} which logging the costing time
 * 
 *@author laxser  Date 2012-4-8 下午5:32:20
@contact [duqifan@gmail.com]
@PerformanceInterceptor.java

 * @see {@link ControllerInterceptor}
 * @see {@link TimeCostUtil}
 * 
 */
public class PerformanceInterceptor extends ControllerInterceptorAdapter {

    private static final String PERFORMANCE_INTERCEPTOR_TIME_BEGIN = "PerformanceInterceptor_TimeBegin";
    private static final Log logger = LogFactory.getLog(PerformanceInterceptor.class);

    public PerformanceInterceptor() {
        setPriority(Performance.PERFORMANCE_PRIV);
    }

    @Override
    protected Class<? extends Annotation> getRequiredAnnotationClass() {
        return Performance.class;
    }

    @Override
    public Object before(Invocation inv) throws Exception {
        inv.setAttribute(PERFORMANCE_INTERCEPTOR_TIME_BEGIN, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(Invocation inv, Throwable ex) throws Exception {
        if(ex==null)return;
        logTimeCost(inv, "ERROR");
    }
    private void logTimeCost(Invocation inv,Object instruction) {
        Long begin = (Long) inv.getAttribute(PERFORMANCE_INTERCEPTOR_TIME_BEGIN);
        if (begin != null && begin > 0) {
            String c = inv.getControllerClass().getSimpleName();
            String m = inv.getMethod().getName();
            long end = System.currentTimeMillis();
            long cost=(end-begin);
            if(cost>200) {//大于200毫秒强制记录
                logger.error(String.format("controller-cost:(%s ms) %s#%s --> %s",cost,c,m,""+instruction));
            }
            TimeCostUtil.logEnd(begin, c, m);
            TimeCostUtil.logger.debug(c + "->" + m + "=" + String.valueOf(instruction));
            if (logger.isDebugEnabled()) {
                Object[] ps = inv.getMethodParameters();
                StringBuilder buf = new StringBuilder(c + "->" + m + "="
                        + String.valueOf(instruction));
                if (ps != null && ps.length > 0) {
                    for (int i = 0; i < ps.length; i++) {
                        buf.append("\n\t[").append(i).append("] ").append(toString(ps[i]));
                    }
                } else {
                    buf.append("\tempty args");
                }
                logger.debug(buf.toString());
            }
            inv.removeAttribute(PERFORMANCE_INTERCEPTOR_TIME_BEGIN);
        }
    }
    @Override
    public Object after(Invocation inv, Object instruction) throws Exception {
        logTimeCost(inv, instruction);
        return instruction;
    }

    private String toString(Object obj) {
        return obj == null ? "Null" : ToStringBuilder.reflectionToString(obj);
    }
}
