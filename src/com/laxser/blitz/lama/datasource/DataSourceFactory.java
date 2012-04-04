package com.laxser.blitz.lama.datasource;

import javax.sql.DataSource;

/**
 * 定义创建 {@link javax.sql.DataSource} 的工厂。
 * 
 *@author laxser  Date 2012-3-22 下午3:54:21
@contact [duqifan@gmail.com]
@DataSourceFactory.java

 */
public interface DataSourceFactory {

    /**
     * 
     * @param dataSourceName - 数据源名称
     * 
     * @return {@link javax.sql.DataSource} 实例
     */
    DataSource getDataSource(Class<?> daoClass);
}
