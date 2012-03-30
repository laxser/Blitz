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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

/**
 * 类代码从 {@link org.springframework.jdbc.core.ArgPreparedStatementSetter}
 * 复制，原始类是非 public 的。
 * 
 * @author laxser  Date 2012-3-22 下午3:58:20
@contact [duqifan@gmail.com]
@ArgPreparedStatementSetter.java

 */
public class ArgPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {

    private final Object[] args;

    /**
     * Create a new ArgPreparedStatementSetter for the given arguments.
     * 
     * @param args the arguments to set
     */
    public ArgPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                if (arg instanceof SqlParameterValue) {
                    SqlParameterValue paramValue = (SqlParameterValue) arg;
                    StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue, paramValue
                            .getValue());
                } else {
                    StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN,
                            arg);
                }
            }
        }
    }

    public void cleanupParameters() {
        StatementCreatorUtils.cleanupParameters(this.args);
    }
}
