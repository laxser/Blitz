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
package com.laxser.blitz.web.impl.thread;

import java.lang.reflect.Method;

import com.laxser.blitz.web.BeforeInterceptors;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;


/**
 * @author laxser  Date 2012-3-23 下午4:46:06
@contact [duqifan@gmail.com]
@BeforeInterceptorsInterceptor.java

 */
public class BeforeInterceptorsInterceptor extends ControllerInterceptorAdapter {

    public BeforeInterceptorsInterceptor() {
        setPriority(Integer.MAX_VALUE);
    }

    @Override
    protected boolean isForAction(Method actionMethod, Class<?> controllerClazz) {
    	for (Class<?> clazz :actionMethod.getParameterTypes()) {
            if (BeforeInterceptors.class.isAssignableFrom(clazz)) {
                return true;
            }
        }
    	return false;
    }

    @Override
    public Object before(Invocation inv) throws Exception {
        for (Object object : inv.getMethodParameters()) {
            if (object instanceof BeforeInterceptors) {
                ((BeforeInterceptors) object).doBeforeInterceptors(inv);
            }
        }
        return true;
    }
}
