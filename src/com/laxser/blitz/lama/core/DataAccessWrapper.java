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
package com.laxser.blitz.lama.core;

import java.util.List;
import java.util.Map;


import org.springframework.jdbc.core.RowMapper;

import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Modifier;

/**
 * 
 * @author laxser  Date 2012-3-22 下午3:42:06
@contact [duqifan@gmail.com]
@DataAccessWrapper.java

 * 
 */

public class DataAccessWrapper implements DataAccess {

    protected DataAccess targetDataAccess;

    public DataAccessWrapper() {
    }

    public DataAccessWrapper(DataAccess dataAccess) {
        this.targetDataAccess = dataAccess;
    }

    public void setDataAccess(DataAccess dataAccess) {
        this.targetDataAccess = dataAccess;
    }

    @Override
    public List<?> select(String sql, Modifier modifier, Map<String, Object> parameters,
            RowMapper rowMapper) {
        return targetDataAccess.select(sql, modifier, parameters, rowMapper);
    }

    @Override
    public int update(String sql, Modifier modifier, Map<String, Object> parameters) {
        return targetDataAccess.update(sql, modifier, parameters);
    }

    @Override
    public Object insertReturnId(String sql, Modifier modifier, Map<String, Object> parameters) {
        return targetDataAccess.insertReturnId(sql, modifier, parameters);
    }

    @Override
    public int[] batchUpdate(String sql, Modifier modifier, List<Map<String, Object>> parametersList) {
        return targetDataAccess.batchUpdate(sql, modifier, parametersList);
    }

}
