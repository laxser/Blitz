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
package com.laxser.blitz.web.impl.module;

import com.laxser.blitz.web.ControllerErrorHandler;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.ParentErrorHandler;
import com.laxser.blitz.web.impl.thread.InvocationBean;


/**
 * 
 * @author laxser  Date 2012-4-5 下午3:24:19
@contact [duqifan@gmail.com]
@ParentErrorHandlerImpl.java

 * 
 */
public class ParentErrorHandlerImpl implements ParentErrorHandler {

    @Override
    public Object onError(Invocation inv, Throwable ex) throws Throwable {
        InvocationBean invb = (InvocationBean) inv;
        Module module = invb.getModule();
        while ((module = module.getParent()) != null) {
            ControllerErrorHandler handler;
            if ((handler = module.getErrorHandler()) != null) {
                return handler.onError(invb, ex);
            }
        }
        throw ex;
    }

}
