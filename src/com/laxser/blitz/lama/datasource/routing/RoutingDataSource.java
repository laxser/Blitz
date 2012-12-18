/*
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.lama.datasource.routing;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * {@link RoutingDataSource} 是一个为支持“散库”功能而准备的 {@link DataSource} 实现。
 * <p>
 * 
 * <p>
 * 所谓“散库”是指根据不同的条件，一个给定的SQL语句应该由MySQL-A执行，另一条SQL语句应该由MySQL-B执行。或者相同的一条SQL语句
 * ，此时应该由MySQL-A执行，彼时应该由MySQL-B执行<br>
 * 散库的情况可能有:<br>
 * <ul>
 * <li>
 * master-slave模式：所有read操作可以送到任意一个slave，但写操作只能送到master</li>
 * <li>
 * sharding模式：对id为1、11、111的SQL操作送到结点MySQL-A，对id为2、12、112的SQL操作送到MySQL-B</li>
 * <li>
 * 复合模式：对id为1、11、111的SQL写操作送到MySQL-A-MASTER结点下，读操作送到MySQL-A-SLAVE结点下；对id为2、
 * 12、112的SQL写操作送到MySQL-B-MASTER结点下，读操作送到MySQL-B-SLAVE结点下；</li>
 * <li>自定义模式：您可以通过扩展自定义任意的模式及其策略</li>
 * </ul>
 * <p>
 * 
 * {@link RoutingDataSource} 包含了一个 {@link ConnectionLocator}，通过传递相关的决策因素
 * {@link RoutingActors}， 委托它提供实际的数据库连接。因此，从某种角度，我们也可以将
 * {@link RoutingDataSource} 理解为 {@link ConnectionLocator} 服务的适配器，它将
 * {@link ConnectionLocator} 适配成为一个 {@link DataSource}对象<br>
 * 同时您可以给 {@link RoutingDataSource} 设置一些相关的属性，在需要的时侯可以由
 * {@link ConnectionLocator}进行读取，从而做相关的决策。当然，具体地，这些取决于
 * {@link ConnectionLocator} 的实现。
 * <p>
 * 
 * 
 * 
 * @author laxser  Date 2012-3-22 下午3:54:28
@contact [duqifan@gmail.com]
@RoutingDataSource.java

 * 
 */
public class RoutingDataSource implements DataSource {
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 数据库连接的实际提供者
     */
    private ConnectionLocator locator;

    /**
     * 配置到本数据源的属性，必要时侯 {@link ConnectionLocator} 可以参考这些属性提供真正的数据库连接
     */
    private Properties propertis = new Properties();

    /**
     * @see #setLogWriter(PrintWriter)
     * @see #getLogWriter()
     */
    private PrintWriter logWriter;

    //-----------------------

    /**
     * 构造一个{@link RoutingDataSource}对象
     * <p>
     * 您还必须设置相关的一些对象才能开始使用，比如 {@link ConnectionLocator}
     */
    public RoutingDataSource() {
    }

    /**
     * @param locator
     */
    public RoutingDataSource(ConnectionLocator locator) {
        this.setConnectionLocator(locator);
    }

    //-----------------------

    /**
     * 新增给定的属性，必要时侯 {@link ConnectionLocator} 可以参考这些属性提供真正的数据库连接
     * 
     * @param propertis
     */
    public void setPropertis(Properties propertis) {
        this.propertis.putAll(propertis);
    }

    /**
     * 返回所有属性
     * 
     * @return
     */
    public Properties getPropertis() {
        return propertis;
    }

    /**
     * 设置给定属性，必要时侯 {@link ConnectionLocator} 可以参考这些属性提供真正的数据库连接
     * 
     * @param name
     * @param value
     */
    public void setProperty(String name, String value) {
        this.propertis.setProperty(name, value);
    }

    /**
     * 返回给定名称的属性
     * 
     * @param name
     * @return
     */
    public String getProperty(String name) {
        return this.propertis.getProperty(name);
    }

    /**
     * 返回给定名称的属性
     * 
     * @param name
     * @param defValue
     * @return
     */
    public String getProperty(String name, String defValue) {
        return this.propertis.getProperty(name, defValue);
    }

    /**
     * 设置实际数据库连接提供器
     * 
     * @param locator
     */
    public void setConnectionLocator(ConnectionLocator locator) {
        this.locator = locator;
    }

    /**
     * 返回设置的实际数据库连接提供器
     * 
     * @return
     */
    public ConnectionLocator getConnectionLocator() {
        return locator;
    }

    /**
     * 创建一个具有散库功能的数据库连接
     */
    @Override
    public RoutingConnection getConnection() throws SQLException {
        return new RoutingConnection(this);
    }

    /**
     * 创建一个具有散库功能的数据库连接
     * <p>
     * 所提供的用户名和密码是否有效，由 {@link ConnectionLocator} 的实现决定
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new RoutingConnection(this, username, password);
    }

    //-----------------

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    //-----------------

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

}
