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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.lama.annotation.DAO;
import com.laxser.blitz.lama.annotation.SQL;
import com.laxser.blitz.lama.annotation.SQLParam;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Definition;
import com.laxser.blitz.lama.provider.Modifier;

/**
 * 
 * @author laxser  Date 2012-3-22 下午3:43:19
@contact [duqifan@gmail.com]
@LamaDaoInvocationHandler.java

 * 
 */
public class LamaDaoInvocationHandler implements InvocationHandler {

    private static final Log logger = LogFactory.getLog(LamaDaoInvocationHandler.class);

    private static LamaOperationFactory jdbcOperationFactory = new LamaOperationFactoryImpl();

    private HashMap<Method, LamaOperation> jdbcOperations = new HashMap<Method, LamaOperation>();

    private final Definition definition;

    private final DataAccess dataAccess;

    public LamaDaoInvocationHandler(DataAccess dataAccess, Definition definition) {
        this.definition = definition;
        this.dataAccess = dataAccess;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (logger.isDebugEnabled()) {
            logger
                    .debug("invoking  " + definition.getDAOClazz().getName() + "#"
                            + method.getName());
        }

        if (Object.class == method.getDeclaringClass()) {
            String methodName = method.getName();
            if (methodName.equals("toString")) {
                return LamaDaoInvocationHandler.this.toString();
            }
            if (methodName.equals("hashCode")) {
                return definition.getDAOClazz().hashCode() * 13 + this.hashCode();
            }
            if (methodName.equals("equals")) {
                return args[0] == proxy;
            }
            if (methodName.equals("clone")) {
                throw new CloneNotSupportedException("clone is not supported for lama dao.");
            }
            throw new UnsupportedOperationException(definition.getDAOClazz().getName() + "#"
                    + method.getName());
        }

        LamaOperation operation = jdbcOperations.get(method);
        if (operation == null) {
            synchronized (jdbcOperations) {
                operation = jdbcOperations.get(method);
                if (operation == null) {
                    Modifier modifier = new Modifier(definition, method);
                    operation = jdbcOperationFactory.getOperation(dataAccess, modifier);
                    jdbcOperations.put(method, operation);
                }
            }
        }
        //
        // 将参数放入  Map
        Map<String, Object> parameters;
        if (args == null || args.length == 0) {
            parameters = new HashMap<String, Object>(4);
        } else {
            parameters = new HashMap<String, Object>(args.length * 2 + 4);
            SQLParam[] sqlParams = operation.getModifier().getParameterAnnotations(SQLParam.class);
            for (int i = 0; i < args.length; i++) {
                parameters.put(":" + (i + 1), args[i]);
                SQLParam sqlParam = sqlParams[i];
                if (sqlParam != null) {
                    parameters.put(sqlParam.value(), args[i]);
                }
            }
        }
        //
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("invoking ").append(definition.getDAOClazz().getName()).append("#").append(
                    method.getName()).append("\n");
            sb.append("\toperation: ").append(operation.getClass().getSimpleName()).append("\n");
            sb.append("\tsql: ").append(operation.getModifier().getAnnotation(SQL.class).value())
                    .append("\n");
            sb.append("\tparams: ");
            ArrayList<String> keys = new ArrayList<String>(parameters.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                sb.append(key).append("='").append(parameters.get(key)).append("'  ");
            }
            logger.debug(sb.toString());
        }

        return operation.execute(parameters);
    }

    @Override
    public String toString() {
        DAO dao = definition.getDAOClazz().getAnnotation(DAO.class);
        String toString = definition.getDAOClazz().getName()//
                + "[catalog=" + dao.catalog() + "]";
        return toString;
    }

}
