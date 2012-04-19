/*
* Copyright 2007-2009 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.laxser.blitz.scanning.context;

import java.io.IOException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.laxser.blitz.scanning.LoadScope;
import com.laxser.blitz.scanning.context.core.BlitzResources;

/**
 * 解析配置ApplicationContext*.xml
 * @author laxser  Date 2012-3-22 下午4:36:34
@contact [duqifan@gmail.com]
@BlitzAppContext.java

 * 
 */
public class BlitzAppContext extends AbstractXmlApplicationContext {

    private Log logger = LogFactory.getLog(getClass());

    private String[] scopeValues;

    public BlitzAppContext() {
        this("", true);
    }

    public BlitzAppContext(String scope, boolean refresh) {
        this(new LoadScope(scope, "applicationContext"), refresh);
    }

    public BlitzAppContext(LoadScope scope, boolean refresh) {
        this.scopeValues = scope.getScope("applicationContext");
        if(logger.isInfoEnabled())
        {
        	logger.info("创建一个BlitzAppcontext");
        	logger.info("create a BlitzAppContext, with scope='" + scope + "'");
        }
        if (refresh) {
            refresh();
        }
    }

    /**
     * 返回对应类型的唯一 Bean, 包括可能的祖先 {@link ApplicationContext} 中对应类型的 Bean.
     * 
     * @param beanType - Bean 的类型
     * 
     * @throws BeansException
     */
    public <T> T getBean(Class<T> beanType) throws BeansException {
        return beanType.cast(BeanFactoryUtils.beanOfTypeIncludingAncestors(this, beanType));
    }

    @Override
    protected final Resource[] getConfigResources() {
        try {
            return getConfigResourcesThrowsIOException();
        } catch (IOException e) {
            throw new ApplicationContextException("getConfigResources", e);
        }
    }

    protected Resource[] getConfigResourcesThrowsIOException() throws IOException {
        return BlitzResources.findContextResources(this.scopeValues).toArray(new Resource[0]);
    }

    @Override
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        prepareBeanFactoryByBlitz(beanFactory);
        super.prepareBeanFactory(beanFactory);
    }

    /** Blitz对BeanFactory的特殊处理，必要时可以覆盖这个方法去掉Blitz的特有的处理 
     * Blitz会使用Lama去自动生成DAO 的相应Bean配置，因此若有DAO特性不包含Lama的话会爆出NotBeanDefException
     */
    protected void prepareBeanFactoryByBlitz(ConfigurableListableBeanFactory beanFactory) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
    }

    public BlitzAppContext getApplicationContext() {
        return this;
    }

}
