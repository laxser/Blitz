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

import java.lang.reflect.Proxy;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.DataAccessProvider;
import com.laxser.blitz.lama.provider.Definition;

/**
 * 提供 DAO 对象的 Spring-framework {@link FactoryBean} 工厂。
 * 
 * @author laxser  Date 2012-3-22 下午3:43:13
@contact [duqifan@gmail.com]
@LamaDaoFactoryBean.java

 * 
 */
public class LamaDaoFactoryBean<T> implements FactoryBean, InitializingBean {

    private T dao;

    private Class<T> daoClass;

    // 保存dataAccessProvider而非dataAccess是为了尽量延迟获取DataAccess实例
    private DataAccessProvider dataAccessProvider;

    public void setDaoClass(Class<T> daoClass) {
        this.daoClass = daoClass;
    }

    public void setDataAccessProvider(DataAccessProvider dataAccessProvider) {
        this.dataAccessProvider = dataAccessProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dataAccessProvider);
        Assert.isTrue(daoClass.isInterface(), "not a interface class: " + daoClass.getName());
    }

    @Override
    public T getObject() {
        if (dao == null) {
            synchronized (this) {
                if (dao == null) {
                    dao = createDAO(daoClass);
                }
            }
        }
        Assert.notNull(dao);
        return dao;
    }

    @SuppressWarnings("unchecked")
    protected T createDAO(Class<T> daoClass) {
        Definition definition = new Definition(daoClass);
        DataAccess dataAccess = dataAccessProvider.createDataAccess(daoClass);
        LamaDaoInvocationHandler handler = new LamaDaoInvocationHandler(dataAccess, definition);
        return (T) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                new Class[] { daoClass }, handler);
    }

    @Override
    public Class<T> getObjectType() {
        return daoClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
