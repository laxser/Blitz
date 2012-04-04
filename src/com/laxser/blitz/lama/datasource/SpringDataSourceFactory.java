package com.laxser.blitz.lama.datasource;

import javax.sql.DataSource;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.laxser.blitz.lama.annotation.DAO;

/**
 * 获取用 Spring Framework 配置的 {@link javax.sql.DataSource} 数据源。
 * 
 * @author laxser  Date 2012-3-22 下午3:52:49
@contact [duqifan@gmail.com]
@SpringDataSourceFactory.java

 */
public class SpringDataSourceFactory implements DataSourceFactory, ApplicationContextAware {

    private Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public DataSource getDataSource(Class<?> daoClass) {
        DataSource dataSource = null;
        String catalog = daoClass.getAnnotation(DAO.class).catalog();
        if (catalog.length() > 0) {
            dataSource = getDataSourceByDirectory(daoClass, catalog);
        }
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = getDataSourceByDirectory(daoClass, daoClass.getName());
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = getDataSourceByKey(daoClass, "jade.dataSource");
        if (dataSource != null) {
            return dataSource;
        }
        return getDataSourceByKey(daoClass, "dataSource");
    }

    private DataSource getDataSourceByDirectory(Class<?> daoClass, String catalog) {
        String tempCatalog = catalog;
        DataSource dataSource;
        while (tempCatalog != null && tempCatalog.length() > 0) {
            dataSource = getDataSourceByKey(daoClass, "jade.dataSource." + tempCatalog);
            if (dataSource != null) {
                return dataSource;
            }
            int index = tempCatalog.lastIndexOf('.');
            if (index == -1) {
                tempCatalog = null;
            } else {
                tempCatalog = tempCatalog.substring(0, index);
            }
        }
        return null;
    }

    private DataSource getDataSourceByKey(Class<?> daoClass, String key) {
        if (applicationContext.containsBean(key)) {
            DataSource dataSource = (DataSource) applicationContext.getBean(key, DataSource.class);
            if (logger.isDebugEnabled()) {
                logger.debug("found dataSource: " + key + " for DAO " + daoClass.getName());
            }
            return dataSource;
        }
        return null;
    }
}
