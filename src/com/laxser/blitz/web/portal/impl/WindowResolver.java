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
package com.laxser.blitz.web.portal.impl;

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.paramresolver.ParamMetaData;
import com.laxser.blitz.web.paramresolver.ParamResolver;
import com.laxser.blitz.web.portal.PortalUtils;
import com.laxser.blitz.web.portal.Window;


/**
 * 解析声明在窗口控制器中的Window参数
 * 
 *@author laxser  Date 2012-3-23 下午4:59:26
@contact [duqifan@gmail.com]
@WindowResolver.java
 
 */
public class WindowResolver implements ParamResolver {

    @Override
    public boolean supports(ParamMetaData paramMetaData) {
        return Window.class == paramMetaData.getParamType();
    }

    @Override
    public Object resolve(Invocation inv, ParamMetaData paramMetaData) throws Exception {
        return PortalUtils.getWindow(inv);
    }
}
