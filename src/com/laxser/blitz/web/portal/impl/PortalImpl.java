/*
 * Copyright 2007-2012 the original author or authors.
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

import java.util.concurrent.ExecutorService;

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.portal.Portal;
import com.laxser.blitz.web.portal.WindowListener;


/**
 * {@link Portal} 的实现类，Portal 框架的核心类。
 * 
 *@author laxser  Date 2012-3-23 下午4:56:28
@contact [duqifan@gmail.com]
@PortalImpl.java
 
 */
public class PortalImpl extends GenericWindowContainer implements Portal, WindowListener {

    public PortalImpl(Invocation inv, ExecutorService executorService, WindowListener portalListener) {
        super(inv, executorService, portalListener);
    }

    //-------------实现toString()---------------

    @Override
    public String toString() {
        return "portal ['" + getInvocation().getRequestPath().getUri() + "']";
    }

	

}
