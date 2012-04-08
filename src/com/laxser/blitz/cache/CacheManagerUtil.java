package com.laxser.blitz.cache;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the access to {@link CacheManager}<br />
 * Using {@link CacheManagerResolver} if you want to using this in Spring or
 * Rose.
 * 
 * @author xylz(xylz@live.cn)
 * @since 20091102
 */
public class CacheManagerUtil {
	private static Object lock = new Object();

	/**
	 * get the CacheManager from current {@link HttpServletRequest}<br />
	 * <b>This method is thread-safety.</b>
	 * 
	 * @param request
	 *            the current {@link HttpServletRequest}
	 * @return the {@link CacheManager}
	 */
	public static CacheManager get(HttpServletRequest request) {
		CacheManager cm = (CacheManager) request.getAttribute(CacheManager.KEY);
		if (cm == null) {
			synchronized (lock) {// because it is fast, so lock this whole class
				cm = (CacheManager) request.getAttribute(CacheManager.KEY);
				if (cm == null) {
					cm = new CacheManager(request);
					request.setAttribute(CacheManager.KEY, cm);
				}
			}
		}
		return cm;
	}
}
