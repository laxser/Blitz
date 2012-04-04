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
import java.sql.SQLException;

/**
 * {@link ConnectionLocator} 规定了 {@link RoutingDataSource} 和外部的真实数据库数据源的接口。
 * <p>
 * 
 * 不同的组织或不同的业务可以实现自己特定的 {@link ConnectionLocator}，完成特有的散库策略。
 * 
 * @author laxser  Date 2012-3-22 下午3:53:03
@contact [duqifan@gmail.com]
@ConnectionLocator.java

 * 
 */
public interface ConnectionLocator {

    /**
     * 按照 {@link RoutingActors} 所提供的信息返回一个对应的正确的数据库链接
     * 
     * @param dataSource
     * @param actors
     * @return
     * @throws SQLException
     */
    Connection getConnection(RoutingActors actors) throws SQLException;

}
