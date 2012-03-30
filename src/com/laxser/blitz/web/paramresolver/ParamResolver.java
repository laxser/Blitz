/*
 * $Id$
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
package com.laxser.blitz.web.paramresolver;

import com.laxser.blitz.web.Invocation;

/**
 * @author laxser  Date 2012-3-23 下午4:52:07
@contact [duqifan@gmail.com]
@ParamResolver.java

 */
public interface ParamResolver {

    /**
     * 返回true表示是由本解析器负责解析这种类型的参数.
     * 
     * @param metaData
     * @return
     */
    public boolean supports(ParamMetaData metaData);

    /**
     * @param inv
     * @param metaData
     * @return
     * @throws Exception
     */
    public Object resolve(Invocation inv, ParamMetaData metaData) throws Exception;
}
