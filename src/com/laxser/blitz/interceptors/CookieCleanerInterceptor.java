package com.laxser.blitz.interceptors;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;

import com.laxser.blitz.util.BasicCookieManager;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.annotation.Interceptor;

/**
 * 清Cookie用的Interceptor,如果你需要使用，只用在对应的Controller上加Annotation
 * 
 * @see CookieCleaner
 * @author laxser  Date 2012-4-8 下午5:30:11
@contact [duqifan@gmail.com]
@CookieCleanerInterceptor.java
*/
@Interceptor(oncePerRequest = true)
public class CookieCleanerInterceptor extends ControllerInterceptorAdapter implements
        InitializingBean {

    protected Log logger = LogFactory.getLog(this.getClass());

    @Override
    public int getPriority() {
        // 确保在PortalWait之后运行after
        return 1;
    }

    @Override
    protected Class<? extends Annotation> getRequiredAnnotationClass() {
        return CookieCleaner.class;
    }

    @Override
    public Object after(Invocation inv, Object instruction) throws Exception {

        Cookie[] cks = inv.getRequest().getCookies();
        if (cks != null) {
            for (Cookie cookie : cks) {
                String cookieName = cookie.getName();
                String cookiePath = cookie.getPath();
                cookiePath = StringUtils.isEmpty(cookiePath) ? "/" : cookiePath;
                if (logger.isDebugEnabled()) {
                    logger.debug("Try to match Cookie name:" + cookieName + ", path:" + cookiePath);
                }
                String cookieSetPath = cookieSet.get(cookieName);
                if (null != cookieSetPath && cookieSetPath.equals(cookiePath)) {// 名字正确，无法做到域名的判断,客户端cookie机制导制
                    if (logger.isDebugEnabled()) {
                        logger.debug("Cookie in White list,skip:" + cookieName);
                    }
                    continue;
                }
                if (isUsed) {
                    BasicCookieManager.getInstance().clearCookie(inv.getResponse(), cookieName, 0,
                            cookiePath);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Clear Cookie:" + cookieName);
                    }
                }
            }
        }
        return instruction;
    }

    public static final Map<String, String> cookieSet = new ConcurrentHashMap<String, String>();

    public static volatile boolean isUsed = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
        
        new Runnable() {

            @Override
            public void run() {

                Properties p = new Properties();
                try {
                    p.load(this.getClass().getClassLoader().getResourceAsStream(
                            "cookiewhitelist.properties"));
                } catch (Exception e) {
                    // 取失败就不要过滤,可能是因为没有配置文件
                    return;
                }
                if (p.isEmpty()) return;// 取失败就不要过滤,可能是因为没有配置文件

                Set<Entry<Object, Object>> entrySet = p.entrySet();
                for (Entry<Object, Object> entry : entrySet) {
                    // is use tag
                    if ("USE".equals(entry.getKey())) {
                        if ("true".equals(entry.getValue())) {
                            isUsed = true;
                            continue;
                        } else {
                            isUsed = false;
                            break;
                        }
                    }

                    //set cookieSet
                    String key = (String) entry.getKey();
                    if (StringUtils.isEmpty(key)) {
                        continue;
                    }
                    JSONObject value;
                    try {
                        value = new JSONObject((String) entry.getValue());
                    } catch (ParseException e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(e.getMessage(), e);
                        }
                        value = new JSONObject();
                    }
                    String pathValue = value.getString("path");
                    String cookieSetValue = StringUtils.isEmpty(pathValue) ? "/" : pathValue;
                    cookieSet.put(key, cookieSetValue);
                }

            }
        }, 1l, 10l, TimeUnit.MINUTES);
    }
}
