package com.laxser.blitz.lama.provider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 * 假的 {@link DataAccessProvider} 实现。
 * 
 *@author laxser  Date 2012-4-11 上午11:29:57
@contact [duqifan@gmail.com]
@DataAccessProviderMock.java

 */
public class DataAccessProviderMock implements DataAccessProvider {

    private static final Log logger = LogFactory.getLog(DataAccessProvider.class);

    @Override
    public DataAccess createDataAccess(Class<?> daoClass) {

        if (logger.isWarnEnabled()) {
            logger.warn("lama is not configured, return mock instance");
        }

        return (DataAccess) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                new Class[] { DataAccess.class }, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {

                        if (method.getDeclaringClass() == DataAccess.class) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("lama is not configured");
                            }
                            throw new IllegalStateException("lama is not configured");
                        }

                        return method.invoke(proxy, args);
                    }
                });
    }

}
