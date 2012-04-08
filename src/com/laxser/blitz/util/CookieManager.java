package com.laxser.blitz.util;

import javax.servlet.http.HttpServletResponse;
/**
 * CookieManager <br>
 * the subclass of {@link com.xiaonei.platform.core.opt.base.CookieManager}
 * , add a feature of add http only cookie
 * 
 * 
 * @see com.xiaonei.platform.core.opt.base.CookieManager
 * 
 * @author tai.wang@opi-corp.com Nov 5, 2010 - 7:19:50 PM
 */
public class CookieManager extends com.laxser.blitz.interceptors.CookieManager {

    private static final CookieManager instance = new CookieManager();

    public static final CookieManager getInstance() {
        return instance;
    }

    //    || name.equalsIgnoreCase("Comment") // rfc2019
    //    || name.equalsIgnoreCase("Discard") // 2019++
    //    || name.equalsIgnoreCase("Domain")
    //    || name.equalsIgnoreCase("Expires") // (old cookies)
    //    || name.equalsIgnoreCase("Max-Age") // rfc2019
    //    || name.equalsIgnoreCase("Path")
    //    || name.equalsIgnoreCase("Secure")
    //    || name.equalsIgnoreCase("Version")
    //    || name.startsWith("$")
    public void saveCookieHttpOnly(HttpServletResponse response, String key, String value,
            int second, String path, String domain) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setPath(path);
//        cookie.setMaxAge(second);
//        cookie.setDomain(domain);
//        //        response.addCookie(cookie);
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
//        sb.append("path=").append(cookie.getPath()).append("; ");
//        if(cookie.getMaxAge() != -1){
//            sb.append("max-age=").append(cookie.getMaxAge()).append("; ");
//        }
//        sb.append("domain=").append(cookie.getDomain()).append("; ");
//        sb.append("httponly");
//        response.setHeader("Set-Cookie", sb.toString());
    	
    	super.saveCookie(response, key, value, second, path, domain+"; httponly");
    }
}
