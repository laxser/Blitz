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

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * 数据库访问层的扩展接口。
 * 
 * @author laxser  Date 2012-3-22 下午4:05:54
@contact [duqifan@gmail.com]
@DataAccess.java

 */
public interface DataAccess {

    /**
     * 执行 SELECT 语句。
     * 
     * @param LamaSQL - 执行的语句
     * @param modifier - 语句修饰
     * @param parameters - 参数
     * @param rowMapper - 对象映射方式
     * 
     * @return 返回的对象列表
     */
    public List<?> select(String lamaSQL, Modifier modifier, Map<String, Object> parameters,
            RowMapper rowMapper);

    /**
     * 执行 UPDATE / DELETE 语句。
     * 
     * @param lamaSQL - 执行的语句
     * @param modifier - 语句修饰
     * @param parameters - 参数
     * 
     * @return 更新的记录数目
     */
    public int update(String lamaSQL, Modifier modifier, Map<String, Object> parameters);

    /**
     * 
     * @param lamaSQL
     * @param modifier
     * @param parametersList
     * @return
     */
    public int[] batchUpdate(String lamaSQL, Modifier modifier,
            List<Map<String, Object>> parametersList);

    /**
     * 执行 INSERT 语句，并返回插入对象的 ID.
     * 
     * @param lamaSQL - 执行的语句
     * @param modifier - 语句修饰
     * @param parameters - 参数
     * 
     * @return 插入对象的 ID
     */
    public Object insertReturnId(String lamaSQL, Modifier modifier, Map<String, Object> parameters);
}
