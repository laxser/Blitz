package com.laxser.blitz.interceptors;

import javax.servlet.http.HttpServletResponse;

import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.annotation.Interceptor;

/**
 * 
 * {@link NoCacheInterceptor} 拦截对Web控制器的调用，将response设置为不使用HTTP缓存
 * 
 * @author laxser  Date 2012-4-7 下午6:25:23
@contact [duqifan@gmail.com]
@NoCacheInterceptor.java

 * 
 */
@Interceptor(oncePerRequest = true)
public class NoCacheInterceptor extends ControllerInterceptorAdapter {

    @Override
    public Object before(Invocation inv) throws Exception {
        HttpServletResponse response = inv.getResponse();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return true;
    }
}
