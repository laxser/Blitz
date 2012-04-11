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


import org.springframework.beans.factory.annotation.Autowired;

import com.laxser.blitz.lama.cache.CacheProvider;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.DataAccessProvider;
import com.laxser.blitz.lama.provider.cache.CacheDataAccess;

/**
 * Lama提供的{@link com.laxser.blitz.lama.provider.DataAccessProvider}实现。
 * 
 * <p>
 * 在Spring的XML配置中必须存在id为jada.dataAccessProvider的本对象实例。
 * 真正创建DataAccess的是其属性targetAccessProvider，需要在XML文件中注入。
 * 
 * 内置可注入的{@link com.laxser.blitz.lama.cache.CacheProvider}，可以提供Cache功能。
 * </p>
 * 
 *@author laxser  Date 2012-3-22 下午3:43:35
@contact [duqifan@gmail.com]
@LamaDataAccessProvider.java

 * 
 */
public class LamaDataAccessProvider implements DataAccessProvider {

	private DataAccessProvider targetAccessProvider;

	@Autowired(required = false)
	private CacheProvider cacheProvider;

	public void setTargetAccessProvider(DataAccessProvider targetAccessProvider) {
		this.targetAccessProvider = targetAccessProvider;
	}

	public void setCacheProvider(CacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}

	@Override
	public DataAccess createDataAccess(Class<?> daoClass) {
		DataAccess dataAccess = targetAccessProvider.createDataAccess(daoClass);
		dataAccess = new SQLThreadLocalWrapper(dataAccess);
		if (cacheProvider != null) {
			dataAccess = new CacheDataAccess(dataAccess, cacheProvider);
		}
		return dataAccess;
	}

}
