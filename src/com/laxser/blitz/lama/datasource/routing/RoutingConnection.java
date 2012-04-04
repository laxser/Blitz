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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * {@link RoutingConnection} 由 {@link RoutingDataSource}
 * 创建，封装对散库系统中的数据库连接抽象。
 * 
 * @author laxser  Date 2012-3-22 下午3:54:33
@contact [duqifan@gmail.com]
@RoutingConnection.java

 * 
 */
public class RoutingConnection implements Connection {

    /**
     * 所属的 {@link RoutingDataSource}
     */
    private final RoutingDataSource routingDataSource;

    /** 存储设置给这个连接的clientInfo */
    private Properties clientInfo = new Properties();

    /** 真正的数据库连接－这里所谓的“真正的”只是相对本类而言 */
    private Connection realConnection;

    /**
     * 存储这个方法中的username参数
     * {@link RoutingDataSource#getConnection(String, String)}
     */
    private String username;

    /**
     * 存储这个方法中的password参数
     * {@link RoutingDataSource#getConnection(String, String)}
     */

    private String password;

    /**
     * 在实际数据库连接获取之前，存储{@link #setAutoCommit(boolean)参数}
     */
    private Boolean autoCommit;

    /**
     * 在实际数据库连接获取之前，存储{@link #setReadOnly(boolean)参数}
     */
    private Boolean readOnly;

    /**
     * 存储所设置的Catalog，本参数不传会给真正的数据库连接
     * 
     * @see #setCatalog(String)
     */
    private String catalog;

    /** 在实际数据库连接获取之前，存储{@link #setTypeMap(Map)参数} */
    private Map<String, Class<?>> typeMap = Collections.emptyMap();

    /**
     * @see RoutingDataSource#getConnection()
     * @param routingDataSource
     */
    public RoutingConnection(RoutingDataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }

    /**
     * @see RoutingDataSource#getConnection(String, String)
     * @param routingDataSource
     * @param username
     * @param password
     */
    public RoutingConnection(RoutingDataSource routingDataSource, String username, String password) {
        this.routingDataSource = routingDataSource;
        this.username = username;
        this.password = password;
    }

    protected Connection loadConnection(boolean create) throws SQLException {
        if (realConnection == null && create) {
            String catalog = clientInfo.getProperty(RoutingActors.CATALOG);
            String partition = clientInfo.getProperty(RoutingActors.PARTITION);
            String node = clientInfo.getProperty(RoutingActors.NODE);
            String sql = clientInfo.getProperty(RoutingActors.SQL);
            if (catalog == null) {
                catalog = this.catalog;
            }
            RoutingActors actors = new RoutingActors();
            actors.setCatalog(catalog);
            actors.setPartition(partition);
            actors.setNode(node);
            actors.setUsername(username);
            actors.setPassword(password);
            actors.setSQL(sql);
            actors.setDataSource(routingDataSource);
            realConnection = routingDataSource.getConnectionLocator().getConnection(actors);
            applySettings(realConnection);
        }
        return realConnection;
    }

    protected void applySettings(Connection real) throws SQLException {
        // client info
        for (Map.Entry<Object, Object> entry : clientInfo.entrySet()) {
            String name = entry.getKey().toString();
            if (!name.startsWith(RoutingActors.class.getName())) {
                real.setClientInfo(name, entry.getValue().toString());
            }
        }
        // 
        if (this.typeMap != null) {
            real.setTypeMap(this.typeMap);
            this.typeMap = null;
        }
        if (autoCommit != null) {
            real.setAutoCommit(autoCommit);
            this.autoCommit = null;
        }
        if (readOnly != null) {
            real.setReadOnly(readOnly);
            this.readOnly = null;
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return loadConnection(true).createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return loadConnection(true).prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return loadConnection(true).prepareCall(sql);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return loadConnection(true).createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return loadConnection(true).prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return loadConnection(true).prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return loadConnection(true).createStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return loadConnection(true).prepareStatement(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return loadConnection(true).prepareCall(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return loadConnection(true).prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return loadConnection(true).prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return loadConnection(true).prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return loadConnection(true).createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return loadConnection(true).createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return loadConnection(true).createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return loadConnection(true).createSQLXML();
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return loadConnection(true).nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        if (this.realConnection != null) {
            this.realConnection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.getAutoCommit();
        }
        if (autoCommit == null) {
            return true;
        }
        return autoCommit;
    }

    @Override
    public void commit() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.close();
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        if (this.realConnection != null) {
            return this.realConnection.isClosed();
        }
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return loadConnection(true).getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        if (this.realConnection != null) {
            this.realConnection.setReadOnly(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.isReadOnly();
        }
        if (readOnly == null) {
            return false;
        }
        return readOnly;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        // set before real connection
        if (this.realConnection != null) {
            throw new IllegalStateException();
        }
        this.catalog = catalog;
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.catalog;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.realConnection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.realConnection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        if (this.realConnection != null) {
            return this.realConnection.getWarnings();
        }
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.clearWarnings();
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        if (this.realConnection == null) {
            return typeMap;
        }
        return this.realConnection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        if (this.realConnection != null) {
            this.realConnection.setTypeMap(map);
        }
        this.typeMap = map;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.realConnection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.realConnection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return loadConnection(false).setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return loadConnection(false).setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        loadConnection(false).rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        loadConnection(false).releaseSavepoint(savepoint);
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return loadConnection(false).isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        if (this.realConnection != null) {
            this.realConnection.setClientInfo(name, value);
        } else {
            clientInfo.setProperty(name, value);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        if (this.realConnection != null) {
            this.realConnection.setClientInfo(properties);
        } else {
            this.clientInfo.putAll(properties);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        if (this.realConnection != null) {
            return realConnection.getClientInfo(name);
        } else {
            return clientInfo.getProperty(name);
        }
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        if (this.realConnection != null) {
            return realConnection.getClientInfo();
        } else {
            return clientInfo;
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
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
