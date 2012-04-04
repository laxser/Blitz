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


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.portal.Pipe;
import com.laxser.blitz.web.portal.Portal;
import com.laxser.blitz.web.portal.PortalFactory;

/**
 * Portal 工厂的 构造代理
 *@author laxser  Date 2012-3-23 下午4:55:51
@contact [duqifan@gmail.com]
@PortalFactoryDelegate.java
 
 */
public class PortalFactoryDelegate implements PortalFactory, ApplicationContextAware {

    private PortalFactory portalFactory;

    private String portalFactoryName;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setPortalFactoryName(String portalFactoryName) {
        this.portalFactoryName = portalFactoryName;
    }

    @Override
    public Portal createPortal(Invocation inv) {
        return getPortalFactory().createPortal(inv);
    }

    @Override
    public Pipe createPipe(Invocation inv, boolean create) {
        return getPortalFactory().createPipe(inv, create);
    }

    protected PortalFactory getPortalFactory() {
        if (portalFactory == null) {
            portalFactory = (PortalFactory) applicationContext.getBean(portalFactoryName);
        }
        return portalFactory;
    }
}
