package com.laxser.blitz.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * cookie管理类
 * HTTPCookieManager 用来继承这个基类使用并且完善这个功能，建议使用HttpCookieManger
 * @author laxser  Date 2012-4-8 下午4:15:20
@contact [duqifan@gmail.com]
@BasicCookieManager.java

 *
 */


public class BasicCookieManager {
	private static BasicCookieManager instance;
	
	
	
	//FIXME  :  添加Domain 的策略
	private  String domainMain;

	public static BasicCookieManager getInstance() {
		if (instance == null)
			synchronized (BasicCookieManager.class) {
				if (instance == null)
					instance = new BasicCookieManager();
			}

		return instance;
	}
	public String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(key))
				return cookies[i].getValue();
			
		}
		return null;
	}

	public void saveCookie(HttpServletResponse response, String key,
			String value) {
		this.saveCookie(response, key, value, -1,"/") ;
	}
	public void saveCookie(HttpServletResponse response, String key,
			String value,String path) {
		this.saveCookie(response, key, value, -1,path) ;
	}
	public void saveCookie(HttpServletResponse response, String key,
			String value,int second,String path) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath(path);
		cookie.setMaxAge(second);
		cookie.setDomain("."+this.domainMain);
		response.addCookie(cookie);
	}
	public void saveCookie(HttpServletResponse response, String key,
			String value,int second,String path, String domain) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath(path);
		cookie.setMaxAge(second);
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}	
	public void clearCookie(HttpServletResponse response, String key,
			int second,String path){
		if(key.equals("xng")){
			try{
				throw new Exception() ;
			}catch(Exception e){
				e.printStackTrace(System.out) ;
			}
		}
		Cookie cookie = new Cookie(key,null);
		cookie.setPath(path);
		cookie.setMaxAge(second);
		cookie.setDomain("."+domainMain);
		response.addCookie(cookie) ;
	}
	public void clearCookie(HttpServletResponse response, String key,
			int second,String path, String domain){
		if(key.equals("xng")){
			try{
				throw new Exception() ;
			}catch(Exception e){
				e.printStackTrace(System.out) ;
			}
		}
		Cookie cookie = new Cookie(key,null);
		cookie.setPath(path);
		cookie.setMaxAge(second);
		cookie.setDomain(domain);
		response.addCookie(cookie) ;
	}	

}
