/**
 * $Id: BlockService.java 14200 2010-01-07 03:31:13Z yan.liu@XIAONEI.OPI.COM $
 * Copyright 2009-2010 Oak Pacific Interactive. All right reserved.
 */
package com.laxser.blitz.util;

import java.util.concurrent.Future;

/**
 * Block cutter service. This service will cut the access for any operation
 * if the operation is timeout.<br />
 * Cutting service example:
 * 
 * <pre>
 * <code>
 *  BlockService.setEnabledAndTimeout(true,20);
 * </code>
 * </pre>
 * 
 * That's over. Any operation from {@link SnsAdapterFactory} will be cutted
 * if the operation is timeout in 20 MILLISECONDS and the caller will get a
 * {@link ServiceTimeoutException}.<br />
 * <p>
 * Be careful! You should set the enable and timeout in your own
 * {@link Thread}. The service will not be cutted if you do nothing or do
 * anything not in the calling thread.
 * </p>
 * The feature (cutting service) is not enable by default.<br />
 * Close the feature by calling the method {@link #setEnabled(boolean)}
 * with 'false' when you want any time and any place. If the feature is
 * closed (not opened), the operation is timeout only by remote service and
 * never timeout if remote service returns never.
 * 
 * 
 * @see ThreadLocal
 * @see Future
 */
public class BlockService {

    private static final boolean DEFAULT_ENABLED = false;

    public static class Cutter {

        private volatile boolean enable = DEFAULT_ENABLED;

        private volatile int timeout = 50;//ms

        public Cutter(boolean enable, int timeout) {
            this.enable = enable;
            this.timeout = timeout;
        }

        public Cutter() {
        }

        public String toString() {
            return "Cutter [enable=" + enable + ", timeout=" + timeout + "]";
        }
    }

    private static ThreadLocal<Cutter> service = new ThreadLocal<Cutter>();

    /**
     * check out the enable state for block service
     */
    public static boolean isEnabled() {
        Cutter cut = service.get();
        return cut != null ? cut.enable : DEFAULT_ENABLED;
    }

    /**
     * waiting for max time (ms)
     * 
     * @return the max wating time or -1 (never timeout)
     */
    public static int getTimeout() {
        Cutter cut = service.get();
        return cut != null ? cut.timeout : -1;
    }

    /**
     * set enable for service cutting
     * 
     * @param b enable state
     */
    public static void setEnabled(boolean b) {
        Cutter cut = service.get();
        if (cut == null) {
            cut = new Cutter();
            service.set(cut);
        }
        cut.enable = b;
    }

    /**
     * set timeout for service cutting <br/>
     * This does not set the enable for service cutting.
     * 
     * @param timeout time for timeout in MILLISECONDS.
     * @see #setEnabled(boolean)
     * @see #setEnabledAndTimeout(boolean, int)
     */
    public static void setTimeout(int timeout) {
        if (timeout < 0) return;
        Cutter cut = service.get();
        if (cut == null) {
            cut = new Cutter();
            service.set(cut);
        }
        cut.timeout = timeout;
    }

    /**
     * reset the last {@link Cutter}
     * @param cutter the last {@link Cutter}
     */
    public static void resetCutter(Cutter cutter) {
        if(cutter==null)return;
        setEnabledAndTimeout(cutter.enable, cutter.timeout);
    }
    
    /**
     * set timeout and enable for service cutting
     * 
     * @param b the enable
     * @param timeout time for timeout in MILLISECONDS.
     * @return the old {@link Cutter}
     */
    public static Cutter setEnabledAndTimeout(boolean b, int timeout) {
        if (timeout < 0) timeout = -1;
        Cutter cut = service.get();
        if (cut == null) {
            cut = new Cutter();
            service.set(cut);
        }
        Cutter old = new Cutter(cut.enable,cut.timeout);
        cut.enable = b;
        cut.timeout = timeout;
        return old;
    }

    /**
     * get the default enable status
     * 
     * @return the defaultEnabled The default enable status is 'false'.
     */
    public static boolean isDefaultEnabled() {
        return DEFAULT_ENABLED;
    }
}
