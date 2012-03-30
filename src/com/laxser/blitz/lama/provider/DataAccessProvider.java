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
package com.laxser.blitz.lama.provider;

/**
 * 定义: DataAccess 的供应者接口。
 * 
 * @author laxser  Date 2012-3-22 下午4:20:01
@contact [duqifan@gmail.com]
@DataAccessProvider.java
 */
public interface DataAccessProvider {

    /**
     * 创建一个: {@link DataAccess} 对象。
     * 
     * @param catalog -
     * 
     * @return DataAccess 对象
     */
    public DataAccess createDataAccess(Class<?> daoClass);
}
