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

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.ClassUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * 实现返回新生成主键的 {@link PreparedStatementCallback} 实现。
 * 
 * @author laxser  Date 2012-3-22 下午4:01:59
@contact [duqifan@gmail.com]
@PreparedStatementCallbackReturnId.java

 */
public class PreparedStatementCallbackReturnId implements PreparedStatementCallback {

    private final PreparedStatementSetter setter;

    private final Class<?> returnType;

    private final Class<?> idType;

    private final Class<?> wrappedIdType;

    private final SingleColumnRowMapper mapper;

    public PreparedStatementCallbackReturnId(PreparedStatementSetter setter, Class<?> returnType) {
        this.setter = setter;
        if (returnType.isPrimitive()) {
            returnType = ClassUtils.primitiveToWrapper(returnType);
        }
        this.returnType = returnType;
        Class<?> idType = returnType;
        if (returnType.isArray()) {
            idType = returnType.getComponentType();
        }
        this.idType = idType;
        if (idType.isPrimitive()) {
            idType = ClassUtils.primitiveToWrapper(idType);
        }
        this.wrappedIdType = idType;
        this.mapper = new SingleColumnRowMapper(idType);
        if (wrappedIdType != Integer.class && wrappedIdType != Long.class) {
            throw new IllegalArgumentException(
                    "Wrong return type(int/long type or its array type only): " + returnType);
        }
    }

    @Override
    public Object doInPreparedStatement(PreparedStatement ps) throws SQLException,
            DataAccessException {

        if (setter != null) {
            setter.setValues(ps);
        }

        int updated = ps.executeUpdate();
        if (updated == 0) {
            if (returnType.isArray()) {
                return Array.newInstance(wrappedIdType, 0);
            } else {
                return defaultValueOf(wrappedIdType);
            }
        }

        ResultSet keys = ps.getGeneratedKeys();
        if (keys != null) {
            try {
                Object ret = null;
                if (returnType.isArray()) {
                    keys.last();
                    int length = keys.getRow();
                    keys.beforeFirst();
                    ret = Array.newInstance(wrappedIdType, length);
                }

                for (int i = 0; keys.next(); i++) {
                    Object value = mapper.mapRow(keys, i);
                    if (value == null && idType.isPrimitive()) {
                        // 如果本不是primitive的，保持null的语义不变
                        value = defaultValueOf(wrappedIdType);
                    }
                    if (ret != null) {
                        Array.set(ret, i + 1, value);
                    } else {
                        ret = value;
                        break;
                    }
                }
                return ret;
            } finally {
                JdbcUtils.closeResultSet(keys);
            }
        } else {
            if (returnType.isArray()) {
                return Array.newInstance(wrappedIdType, 0);
            } else {
                return defaultValueOf(wrappedIdType);
            }
        }
    }

    private static Object defaultValueOf(Class<?> numberType) {
        if (numberType == Integer.class) {
            return new Integer(-1);
        } else if (numberType == Long.class) {
            return new Long(-1);
        }
        throw new Error("wrong number type: " + numberType);
    }
    
    public static void main(String[] args) throws SecurityException, NoSuchMethodException {
        System.out.println(
        PreparedStatementCallbackReturnId.class.getMethod("main", String[].class).getReturnType().getName());
    }
}
