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

import com.laxser.blitz.web.Dispatcher;

/**
 * 
 * @author laxser  Date 2012-4-7 下午5:52:19
@contact [duqifan@gmail.com]
@DispatcherSelector.java

 * 
 */
public interface DispatcherSelector {

    /**
     * 当所分配请求是所给的类型时(普通请求、FORWARD请求、INCLUDE请求)，是否执行此拦截器？
     * 
     * @return
     */
    boolean isForDispatcher(Dispatcher dispatcher);

}
