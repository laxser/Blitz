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
package com.laxser.blitz.advancedinterceptor;

import java.lang.reflect.Method;

/**
 * 
 * @author laxser  Date 2012-4-7 下午5:52:06
@contact [duqifan@gmail.com]
@ActionSelector.java

 * 
 */
public interface ActionSelector {

    /**
     * 作为候选拦截器，这个拦截器是否应拦截所指的控制器及其方法？
     * 
     * @param controllerClazz
     * @param actionMethod
     * @return
     */
    boolean isForAction(Class<?> controllerClazz, Method actionMethod);

}
