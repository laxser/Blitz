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

import java.sql.Connection;

/**
 * {@link RoutingActors}登记和散库有关的各种因素，以提供给 {@link ConnectionLocator}
 * 参考这些因素给出实际的数据库连接
 * 
 * @author laxser  Date 2012-3-22 下午3:54:41
@contact [duqifan@gmail.com]
@RoutingActors.java

 */
public class RoutingActors {

    /**
     * catalog参数的名字
     * 
     * @see Connection#setClientInfo(String, String)
     */
    public static final String CATALOG = RoutingActors.class.getName() + "#catalog";

    /**
     * partition参数的名字
     * 
     * @see Connection#setClientInfo(String, String)
     */
    public static final String PARTITION = RoutingActors.class.getName() + "#partition";

    /**
     * master参数的名字
     * 
     * @see Connection#setClientInfo(String, String)
     */
    public static final String NODE = RoutingActors.class.getName() + "#master";

    /**
     * sql参数的名字
     * 
     * @see Connection#setClientInfo(String, String)
     */
    public static final String SQL = RoutingActors.class.getName() + "#sql";

    // --------------

    /** */
    String catalog;

    /** */
    String partition;

    /***/
    String node;

    String sql;

    String username;

    String password;

    RoutingDataSource routingDataSource;

    public void setDataSource(RoutingDataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }

    public RoutingDataSource getDataSource() {
        return this.routingDataSource;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getPartition() {
        return partition;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getSQL() {
        return sql;
    }

    public void setSQL(String sql) {
        this.sql = sql;
    }

}
