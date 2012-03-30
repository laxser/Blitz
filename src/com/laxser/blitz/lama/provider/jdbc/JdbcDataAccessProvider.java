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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import com.laxser.blitz.lama.datasource.DataSourceFactory;
import com.laxser.blitz.lama.datasource.SpringDataSourceFactory;
import com.laxser.blitz.lama.provider.AbstractDataAccessProvider;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.SQLInterpreter;

/**
 * 
 *@author laxser  Date 2012-3-22 下午3:59:06
@contact [duqifan@gmail.com]
@JdbcDataAccessProvider.java

 */
public class JdbcDataAccessProvider extends AbstractDataAccessProvider implements
        ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected DataSourceFactory createDataSourceFactory() {
        Map<?, ?> beansOfType = applicationContext.getBeansOfType(DataSourceFactory.class);
        if (beansOfType.size() > 1) {
            throw new NoSuchBeanDefinitionException(DataSourceFactory.class,
                    "expected single bean but found " + beansOfType.size());
        } else if (beansOfType.size() == 1) {
            return (DataSourceFactory) beansOfType.values().iterator().next();
        }
        SpringDataSourceFactory dataSourceFactory = new SpringDataSourceFactory();
        dataSourceFactory.setApplicationContext(applicationContext);
        return dataSourceFactory;
    }

    @Override
    protected final DataAccess createDataAccess(DataSource dataSource) {
        JdbcDataAccess dataAccess = createEmptyJdbcTemplateDataAccess(dataSource);
        dataAccess.setInterpreters(findSQLInterpreters());
        JdbcWrapper[] jdbcs = findJdbcWrappers();
        for (JdbcWrapper jdbcWrapper : jdbcs) {
            jdbcWrapper.setDataSource(dataSource);
        }
        dataAccess.setJdbcWrappers(jdbcs);
        return dataAccess;
    }

    /**
     * findPlugin<br>
     * 
     * @return
     * 
     * @author tai.wang@opi-corp.com May 26, 2010 - 4:30:09 PM
     */
    protected JdbcWrapper[] findJdbcWrappers() {
        // JdbcWrapper必须把score写为prototype
        @SuppressWarnings("unchecked")
        Collection<JdbcWrapper> jdbcWrappers = this.applicationContext.getBeansOfType(
                JdbcWrapper.class).values();
        JdbcWrapper[] arrayJdbcWrappers = jdbcWrappers.toArray(new JdbcWrapper[0]);
        for (JdbcWrapper jdbcWrapper : arrayJdbcWrappers) {
            Assert.isNull(jdbcWrapper.getJdbc());// jdbc should be null here
        }
        Arrays.sort(arrayJdbcWrappers, new Comparator<JdbcWrapper>() {

            @Override
            public int compare(JdbcWrapper thees, JdbcWrapper that) {
                Order thessOrder = thees.getClass().getAnnotation(Order.class);
                Order thatOrder = that.getClass().getAnnotation(Order.class);
                int thessValue = thessOrder == null ? 0 : thessOrder.value();
                int thatValue = thatOrder == null ? 0 : thatOrder.value();
                return thessValue - thatValue;
            }

        });
        return arrayJdbcWrappers;
    }

    protected JdbcDataAccess createEmptyJdbcTemplateDataAccess(DataSource dataSource) {
        return new JdbcDataAccess(new JdbcImpl(dataSource), dataSource);
    }

    protected SQLInterpreter[] findSQLInterpreters() {
        @SuppressWarnings("unchecked")
        Collection<SQLInterpreter> interpreters = this.applicationContext.getBeansOfType(
                SQLInterpreter.class).values();
        SQLInterpreter[] arrayInterpreters = interpreters.toArray(new SQLInterpreter[0]);
        Arrays.sort(arrayInterpreters, new Comparator<SQLInterpreter>() {

            @Override
            public int compare(SQLInterpreter thees, SQLInterpreter that) {
                Order thessOrder = thees.getClass().getAnnotation(Order.class);
                Order thatOrder = that.getClass().getAnnotation(Order.class);
                int thessValue = thessOrder == null ? 0 : thessOrder.value();
                int thatValue = thatOrder == null ? 0 : thatOrder.value();
                return thessValue - thatValue;
            }

        });
        return arrayInterpreters;
    }
}
