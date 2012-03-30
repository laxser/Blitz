package com.laxser.blitz.lama.provider.cache;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.laxser.blitz.lama.cache.CacheProvider;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.DataAccessProvider;

/**
 * 提供支持缓存的 {@link DataAccessProvider} 包装器实现。
 * 
 * @author laxser  Date 2012-3-22 下午3:58:12
@contact [duqifan@gmail.com]
@CacheDataAccessProvider.java

 */
public class CacheDataAccessProvider implements DataAccessProvider, ApplicationContextAware {

    protected final DataAccessProvider dataAccessProvider;

    protected final CacheProvider cacheProvider;

    public CacheDataAccessProvider(DataAccessProvider dataAccessProvider,
            CacheProvider cacheProvider) {
        this.dataAccessProvider = dataAccessProvider;
        this.cacheProvider = cacheProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (dataAccessProvider instanceof ApplicationContextAware) {
            // 向下转播  ApplicationContext 对象
            ((ApplicationContextAware) dataAccessProvider)
                    .setApplicationContext(applicationContext);
        }
    }

    @Override
    public DataAccess createDataAccess(Class<?> daoClass) {

        // 含缓存逻辑的  DataAccess
        return new CacheDataAccess( // NL
                dataAccessProvider.createDataAccess(daoClass), // NL
                cacheProvider);
    }

}
