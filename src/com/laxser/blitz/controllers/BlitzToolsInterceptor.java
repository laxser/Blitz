/*
 * Copyright 2007-2010 the original author or authors.
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
package com.laxser.blitz.controllers;


import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.BlitzVersion;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;

/**
 * 
 * @author laxser  Date 2012-3-23 下午3:49:35
@contact [duqifan@gmail.com]
@BlitzToolsInterceptor.java

 * 
 */
public class BlitzToolsInterceptor extends ControllerInterceptorAdapter {

    @Override
    public Object before(Invocation inv) throws Exception {
        Class<?> controllerClass = inv.getControllerClass();
        if (!LogFactory.getLog(controllerClass).isDebugEnabled()) {
            String msg = String.format("warning: set logger.%s to debug level first. "
                    + "<br> Blitz-Version: %s", controllerClass.getName(), BlitzVersion.getVersion());
            return Utils.wrap(msg);
        }
        return true;
    }

}
