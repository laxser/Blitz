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
package com.laxser.blitz.lama.core.mapper;

import java.util.ArrayList;
import java.util.Collection;

import com.laxser.blitz.lama.provider.Modifier;


/**
 * 将SQL结果集的一行映射为List
 * 
 * @author laxser  Date 2012-3-22 下午3:40:44
@contact [duqifan@gmail.com]
@ListRowMapper.java

 */
public class ListRowMapper extends AbstractCollectionRowMapper {

    public ListRowMapper(Modifier modifier) {
        super(modifier);
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    protected Collection createCollection(int columnSize) {
        return new ArrayList(columnSize);
    }
}
