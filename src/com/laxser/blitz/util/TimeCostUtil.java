package com.laxser.blitz.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * logging something for debuging or analyzing in the future
 * 
 * @author laxser  Date 2012-4-12 上午9:58:38
@contact [duqifan@gmail.com]
@TimeCostUtil.java
 */
public class TimeCostUtil {

    /**
     * The logger linking to '<code>com.dodoyo.utils.HomeData</code>' will
     * be changed after an appender linking to the logger
     */
    public static final Log logger = LogFactory.getLog(TimeCostUtil.class);

    /**
     * starting a logger progress
     * 
     * @return the currrent time in milliseconds or <code>0</code> if the
     *         logger level is upper to '<code>DEBUG</code>'.
     * @see System#currentTimeMillis()
     */
    public static final long logBegin() {
        return logBegin(logger);
    }

    /**
     * starting a logger using given <code>Log</code>
     * 
     * @param log the given log
     * @return the current tiem in milliseconds
     * @see #logBegin()
     */
    public static final long logBegin(Log log) {
        return !log.isDebugEnabled() ? 0 : System.currentTimeMillis();
    }

    /**
     * ending a logger progress<br>
     * 
     * if <code>begin &gt;0 && logger.isDebugEnabled()</code>, we will log
     * something in the format:<br>
     * 
     * <pre>
     * 'Main'|clazz|method|cost|userid
     * </pre>
     * 
     * the cost is the costing time between <i>current time</i> and the
     * <i>begin</i>
     * 
     * <pre>
     * Actually, the log4j can catch the class name and method name, but it costs
     * more time and runs slower.
     * </pre>
     * 
     * @param log the given logger
     * @param begin the starting time
     * @param clazz calling class
     * @param method calling method (or key and so on)
     * @param userid the user id if exist
     * @return the ending time (the current time in milliseconds) or
     *         <code>0</code> if the <i>begin &lt; 0</i>
     * 
     */
    public static final long logEnd(Log log, long begin, String clazz, String method, int userid) {
        if (begin > 0 && log.isDebugEnabled()) {
            long end = System.currentTimeMillis();
            final String f = "%s|Main|%s|%s|%s|%s";
            log.debug(String.format(f, Thread.currentThread().getName(),clazz,method,(end-begin),userid));
            return end;
        }
        return 0;
    }

    /**
     * ending a logger progress<br>
     * 
     * @param begin the starting time
     * @param clazz calling class
     * @param method calling method (or key and so on)
     * @see #logEnd(long, String, String, int)
     */
    public static final long logEnd(long begin, String clazz, String method) {
        return logEnd(begin, clazz, method, -1);
    }

    /**
     * ending a logger progress<br>
     * if <code>begin &gt;0 && logger.isDebugEnabled()</code>, we will log
     * something in the format:<br>
     * 
     * <pre>
     * 'Main'|clazz|method|cost|userid
     * </pre>
     * 
     * the cost is the costing time between <i>current time</i> and the
     * <i>begin</i>
     * 
     * @param begin the starting time
     * @param clazz calling class
     * @param method calling method (or key and so on)
     * @param userid the user id if exist
     * @return the ending time (the current time in milliseconds) or
     *         <code>0</code> if the <i>begin &lt; 0</i>
     */
    public static final long logEnd(long begin, String clazz, String method, int userid) {
        return logEnd(logger, begin, clazz, method, userid);
    }

    /**
     * log an exception with given logger
     * 
     * @param log the logger
     * @param t the exception object
     * @see {@link #logError(Log, Throwable, String)}
     */
    public static final void logError(Log log, Throwable t) {
        logError(log, t, "");
    }

    /**
     * log an exception with given logger and message<br>
     * if the logger is debugenabled, the logger will print the stack.
     * 
     * @param log the logger
     * @param t the exception object
     * @param message the message
     * @see {@link Log#isDebugEnabled()}
     */
    public static final void logError(Log log, Throwable t, String message) {
        if (log != null) {
            if (t == null) {
                log.error(message);
                return;
            }
            if (log.isDebugEnabled()) {
                log.error(message + t.getMessage(), t);
            } else {
                log.error(message + t.getMessage());
            }
        }
    }

    /**
     * log an exception
     * 
     * @param t the exception
     * @see #logError(Log, Throwable)
     */
    public static final void logError(Throwable t) {
        logError(logger, t);
    }

}
