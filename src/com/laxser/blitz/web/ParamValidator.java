/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.web;


import org.springframework.validation.Errors;

import com.laxser.blitz.web.paramresolver.ParamMetaData;

/**
 * 请求参数的验证器
 *@author laxser  Date 2012-3-22 下午4:53:25
@contact [duqifan@gmail.com]
@ParamValidator.java

 * 
 */
public interface ParamValidator {

    /**
     * 返回true表示是由本解析器负责解析这种类型的参数.
     * 
     * @param metaData
     * @return
     */
    public boolean supports(ParamMetaData metaData);

    /**
     * 如果返回的instruction不是null、boolean或空串==>杯具：流程到此为止！
     * 返回null或true,false,空串没有本质区别
     */
    Object validate(ParamMetaData metaData, Invocation inv, Object target, Errors errors);
}
