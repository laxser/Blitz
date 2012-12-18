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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.laxser.blitz.lama.annotation.SQLType;
import com.laxser.blitz.lama.core.SQLThreadLocal;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Modifier;
import com.laxser.blitz.lama.provider.SQLInterpreter;
import com.laxser.blitz.lama.provider.SQLInterpreterResult;

/**
 * 
 * @author laxser  Date 2012-3-22 下午3:58:58
@contact [duqifan@gmail.com]
@JdbcDataAccess.java

 */
public class JdbcDataAccess implements DataAccess {

    private SQLInterpreter[] interpreters;

    private final Jdbc sysjdbc;

    private Jdbc jdbc;

    private final DataSource dataSource;

    protected JdbcDataAccess(Jdbc jdbc, DataSource dataSource) {
        this.sysjdbc = jdbc;
        this.jdbc = jdbc;
        this.dataSource = dataSource;
    }

    public void setInterpreters(SQLInterpreter[] interpreters) {
        Assert.isTrue(interpreters.length > 0);
        this.interpreters = interpreters;
    }

    public void setJdbcWrappers(JdbcWrapper[] jdbcWrappers) {
        // start from 1
        for (int i = 1; i < jdbcWrappers.length; i++) {
            JdbcWrapper pre = jdbcWrappers[i - 1];
            pre.setJdbc(jdbcWrappers[i]);
        }
        if (jdbcWrappers.length > 0) {
            jdbcWrappers[jdbcWrappers.length - 1].setJdbc(sysjdbc);
            this.jdbc = jdbcWrappers[0];
        }
    }

    // ------------------------------------------------

    @Override
    public List<?> select(String lamaSQL, Modifier modifier, Map<String, Object> parametersAsMap,
            RowMapper rowMapper) {
        SQLInterpreterResult result = interpret(lamaSQL, modifier, parametersAsMap);
        return jdbc.query(modifier, result.getSQL(), result.getParameters(), rowMapper);
    }

    @Override
    public int update(String lamaSQL, Modifier modifier, Map<String, Object> parametersAsMap) {
        SQLInterpreterResult result = interpret(lamaSQL, modifier, parametersAsMap);
        return jdbc.update(modifier, result.getSQL(), result.getParameters());
    }

    @Override
    public Object insertReturnId(String lamaSQL, Modifier modifier,
            Map<String, Object> parametersAsMap) {
        SQLInterpreterResult result = interpret(lamaSQL, modifier, parametersAsMap);
        return jdbc.insertAndReturnId(modifier, result.getSQL(), result.getParameters());
    }

    @Override
    public int[] batchUpdate(String sql, Modifier modifier, List<Map<String, Object>> parametersList) {
        // 以com.xiaonei.in.dao为试点测试真正的批量插入、更新，不支持返回可能的自增主键
        // 2010-10-20
        if (
        		modifier.getDefinition().getDAOClazz().getName().startsWith("com.laxser.blitz.lama.mock.dao")) {
            return batchUpdate2(sql, modifier, parametersList);
        } else {
            return batchUpdate1(sql, modifier, parametersList);
        }
    }

    private int[] batchUpdate1(String sql, Modifier modifier,
            List<Map<String, Object>> parametersList) {
        int[] updated = new int[parametersList.size()];
        for (int i = 0; i < updated.length; i++) {
            Map<String, Object> parameters = parametersList.get(i);
            SQLThreadLocal.set(SQLType.WRITE, sql, modifier, parameters);
            updated[i] = update(sql, modifier, parameters);
            SQLThreadLocal.remove();
        }
        return updated;
    }

    private int[] batchUpdate2(String sql, Modifier modifier,
            List<Map<String, Object>> parametersList) {
        if (parametersList.size() == 0) {
            return new int[0];
        }
        // sql --> args[]
        HashMap<String, List<Object[]>> batches = new HashMap<String, List<Object[]>>();
        // sql --> named args
        HashMap<String, List<Map<String, Object>>> batches2 = new HashMap<String, List<Map<String, Object>>>();
        // sql --> [2,3,6,9] positions of parametersList
        Map<String, List<Integer>> positions = new HashMap<String, List<Integer>>();

        for (int i = 0; i < parametersList.size(); i++) {
            SQLInterpreterResult ir = interpret(sql, modifier, parametersList.get(i));
            List<Object[]> args = batches.get(ir.getSQL());
            List<Integer> position = positions.get(ir.getSQL());
            List<Map<String, Object>> maplist = batches2.get(ir.getSQL());
            if (args == null) {
                args = new LinkedList<Object[]>();
                batches.put(ir.getSQL(), args);
                position = new LinkedList<Integer>();
                positions.put(ir.getSQL(), position);
                maplist = new LinkedList<Map<String, Object>>();
                batches2.put(ir.getSQL(), maplist);
            }
            position.add(i);
            args.add(ir.getParameters());
            maplist.add(parametersList.get(i));
        }
        if (batches.size() == 1) {
            SQLThreadLocal.set(SQLType.WRITE, sql, modifier, parametersList);
            int[] updated = jdbc.batchUpdate(modifier, batches.keySet().iterator().next(), batches
                    .values().iterator().next());
            SQLThreadLocal.remove();
            return updated;
        }
        int[] batchUpdated = new int[parametersList.size()];
        for (Map.Entry<String, List<Object[]>> batch : batches.entrySet()) {
            String batchSQL = batch.getKey();
            List<Object[]> values = batch.getValue();
            List<Map<String, Object>> map = batches2.get(batchSQL);
            SQLThreadLocal.set(SQLType.WRITE, sql, modifier, map);
            int[] updated = jdbc.batchUpdate(modifier, batchSQL, values);
            SQLThreadLocal.remove();
            List<Integer> position = positions.get(batchSQL);
            int i = 0;
            for (Integer p : position) {
                batchUpdated[p] = updated[i++];
            }
        }
        return batchUpdated;

    }

    protected SQLInterpreterResult interpret(String lamaSQL, Modifier modifier,
            Map<String, Object> parametersAsMap) {
        SQLInterpreterResult result = null;
        // 
        for (SQLInterpreter interpreter : interpreters) {
            String sql = (result == null) ? lamaSQL : result.getSQL();
            Object[] parameters = (result == null) ? null : result.getParameters();
            SQLInterpreterResult t = interpreter.interpret(dataSource, sql, modifier,
                    parametersAsMap, parameters);
            if (t != null) {
                result = t;
            }
        }
        Assert.notNull(result);
        //
        return result;
    }

}
