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

import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.web.impl.module.ControllerRef;
import com.laxser.blitz.web.impl.module.Module;

/**
 *@author laxser  Date 2012-3-23 下午4:40:11
@contact [duqifan@gmail.com]
@ControllerEngine.java
 */
public class ControllerEngine implements Engine {

    private static Log logger = LogFactory.getLog(ControllerEngine.class);

    private final Module module;

    private final Object controller;

    private final boolean proxiedController;

    private final Class<?> controllerClass;

    private final String viewPrefix;

    public ControllerEngine(Module module, ControllerRef controllerRef) {
        this.module = module;
        this.controller = controllerRef.getControllerObject();
        this.controllerClass = controllerRef.getControllerClass();
        this.viewPrefix = controllerRef.getControllerName() + "-";
        this.proxiedController = Proxy.isProxyClass(this.controller.getClass());
        if (proxiedController && logger.isDebugEnabled()) {
            logger.debug("it's a proxied controller: " + controllerClass.getName());
        }
    }

    public Module getModule() {
        return module;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public Object getController() {
        return controller;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public boolean isProxiedController() {
        return proxiedController;
    }

    @Override
    public int isAccepted(HttpServletRequest blitz) {
        return 1;
    }

    @Override
    public Object execute(Blitz blitz) throws Throwable {
        return blitz.doNext();
    }

    public void destroy() {
    }

    @Override
    public String toString() {
        return getControllerClass().getName();
    }
}
