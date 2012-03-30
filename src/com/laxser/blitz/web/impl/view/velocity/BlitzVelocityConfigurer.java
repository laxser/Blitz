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
package com.laxser.blitz.web.impl.view.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

/**
 * 
 * @author laxser  Date 2012-3-28 上午9:17:53
@contact [duqifan@gmail.com]
@BlitzVelocityConfigurer.java

 * 
 */
public class BlitzVelocityConfigurer extends VelocityConfigurer {

    /** the name of the resource loader for Blitz's bind macros */
    private static final String BLITZ_MACRO_RESOURCE_LOADER_NAME = "blitzMacro";

    /** the key for the class of Blitz's bind macro resource loader */
    private static final String BLITZ_MACRO_RESOURCE_LOADER_CLASS = "blitzMacro.resource.loader.class";

    /** the name of Blitz's default bind macro library */
    private static final String BLITZ_MACRO_LIBRARY = "com/laxser/blitz/web/impl/view/velocity/blitz.vm";

    @Override
    protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
        super.postProcessVelocityEngine(velocityEngine);
        velocityEngine.setProperty(BLITZ_MACRO_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class
                .getName());
        velocityEngine.addProperty(VelocityEngine.RESOURCE_LOADER, BLITZ_MACRO_RESOURCE_LOADER_NAME);
        velocityEngine.addProperty(VelocityEngine.VM_LIBRARY, BLITZ_MACRO_LIBRARY);
        if (logger.isInfoEnabled()) {
            logger.info("ClasspathResourceLoader with name '" + BLITZ_MACRO_RESOURCE_LOADER_NAME
                    + "' added to configured VelocityEngine");
        }
    }
}
