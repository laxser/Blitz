package com.laxser.blitz.interceptors;

import java.lang.annotation.Annotation;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.annotation.Interceptor;
import com.laxser.blitz.web.annotation.ResourceProtected;

/**
 * 
 * {@link ResourceProtectionInterceptor} 拦截对Web控制器的调用，
 * 对POST请求进行referer验证，防止外站提交。
 * 
 * @author laxser  Date 2012-4-8 下午5:28:53
@contact [duqifan@gmail.com]
@ResourceProtectionInterceptor.java

 * 
 */
@Interceptor(oncePerRequest = true)
public class ResourceProtectionInterceptor extends ControllerInterceptorAdapter {

    private Log logger = LogFactory.getLog(getClass());

    
    //FIXME: 通过配置统一的配置路径来做校验
    private String domainMain;
    private String domainStatic;
    private String urlHome;
    @Override
    protected Class<? extends Annotation> getRequiredAnnotationClass() {
        return ResourceProtected.class;
    }

    @Override
    public Object before(Invocation inv) throws Exception {
        HttpServletRequest request = inv.getRequest();
        String referer = request.getHeader("Referer");
        if (referer != null && referer.trim().length() > 0) {
            URL url = null;
            try {
                referer = referer.replaceAll("#", "");
                url = new URL(referer);
                String hosturl = url.getHost().toLowerCase();
                if (hosturl.endsWith(domainMain.toString())
                        || hosturl.endsWith(domainStatic.toString())
                       ) {
                    // passed filtering
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("invalid post from referer: " + referer + ";         inv.uri="
                                + inv.getRequestPath().getUri());
                    }
                    return "r:" + urlHome + "/Home.do";
                }
            } catch (Exception e) {
                // e.g: MalformedURLException
                // refer格式有问题,这种情况理论上应该不会发生，但是后来证实在wap平台上确实发生了，所以作忽略处理
            }
        } else {
            // 没有referer的情况，也需要处理，暂时还没有处理，
            // 发生了一种情况：学校资料页，先保存到本地，再用这个页提交，虽然没有referer但是可以提交成功，这是个漏洞
        }
        return true;
    }
}
