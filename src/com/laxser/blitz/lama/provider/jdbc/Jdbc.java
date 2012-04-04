/*
 * Copyright 2009-2010 the original author or authors.
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
package com.laxser.blitz.lama.provider.jdbc;

import java.util.List;

import javax.sql.DataSource;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.laxser.blitz.lama.provider.Modifier;

/**
 * 
 *@author laxser  Date 2012-3-22 下午3:58:51
@contact [duqifan@gmail.com]
@Jdbc.java

 * 
 */
public interface Jdbc {

    /**
     * 
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource);

    /**
     * 根据给定的标准sql以及所带的参数去查询数据库并返回一个结果集
     * 
     * @param sql
     * @param args
     * @param rowMapper
     * @return
     */
    public List<?> query(Modifier modifier, String sql, Object[] args, RowMapper rowMapper);

    /**
     * 根据给定的标准sql以及所带的参数更新数据库(包含插入数据)
     * 
     * @param sql
     * @param args
     * @return
     */
    public int update(Modifier modifier, String sql, Object[] args);

    /**
     * 
     * @param modifier
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    public int[] batchUpdate(Modifier modifier, String sql, List<Object[]> args)
            throws DataAccessException;

    /**
     * 根据给定的标准sql以及所带的参数执行插入数据到数据库，并返回自增列值
     * 
     * @param sql
     * @param parameters
     * @return
     */
    public Object insertAndReturnId(Modifier modifier, String sql, Object[] parameters);

}
