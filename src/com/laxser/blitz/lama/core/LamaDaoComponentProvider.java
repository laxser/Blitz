/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.lama.core;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.laxser.blitz.lama.annotation.DAO;

/**
 * 
 * 
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Ramnivas Laddad
 * 
 * @author laxser  Date 2012-3-22 下午3:42:54
@contact [duqifan@gmail.com]
@LamaDaoComponentProvider.java

 * 
 * @see LamaDaoComponentProvider
 * @see org.springframework.core.type.classreading.MetadataReaderFactory
 * @see org.springframework.core.type.AnnotationMetadata
 * @see ScannedGenericBeanDefinition
 */
public class LamaDaoComponentProvider implements ResourceLoaderAware {

    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*DAO.class";

    protected final Log logger = LogFactory.getLog(getClass());

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
            this.resourcePatternResolver);

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

/**
     * Create a JadeDaoComponentProvider.
     * 
     * @param useDefaultFilters whether to register the default filters for
     *        the {@link Component @Component}, {@link Repository
     *        @Repository}, {@link Service @Service}, and
     *        {@link Controller @Controller} stereotype annotations
     * @see #registerDefaultFilters()
     */
    public LamaDaoComponentProvider(boolean useDefaultFilters) {
        if (useDefaultFilters) {
            registerDefaultFilters();
        }
    }

    /**
     * Set the ResourceLoader to use for resource locations. This will
     * typically be a ResourcePatternResolver implementation.
     * <p>
     * Default is PathMatchingResourcePatternResolver, also capable of
     * resource pattern resolving through the ResourcePatternResolver
     * interface.
     * 
     * @see org.springframework.core.io.support.ResourcePatternResolver
     * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    /**
     * Return the ResourceLoader that this component provider uses.
     */
    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    /**
     * Set the resource pattern to use when scanning the classpath. This
     * value will be appended to each base package name.
     * 
     * @see #findCandidateComponents(String)
     * @see #DEFAULT_RESOURCE_PATTERN
     */
    public void setResourcePattern(String resourcePattern) {
        Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
        this.resourcePattern = resourcePattern;
    }

    /**
     * Add an include type filter to the <i>end</i> of the inclusion list.
     */
    private void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * Add an exclude type filter to the <i>front</i> of the exclusion
     * list.
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    /**
     * 
     * @param useDefaultFilters
     */
    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
        if (useDefaultFilters) {
            registerDefaultFilters();
        }
    }

    /**
     * Scan the class path for candidate components.
     * 
     * @param basePackage the package to check for annotated classes
     * @return a corresponding Set of autodetected bean definitions
     */
    public Set<BeanDefinition> findCandidateComponents(String uriPrefix) {
        if (!uriPrefix.endsWith("/")) {
            uriPrefix = uriPrefix + "/";
        }
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            String packageSearchPath = uriPrefix + this.resourcePattern;
            boolean traceEnabled = logger.isDebugEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            if (debugEnabled) {
                logger.debug("[jade/find] find " + resources.length + " resources for "
                        + packageSearchPath);
            }
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                if (traceEnabled) {
                    logger.trace("[jade/find] scanning " + resource);
                }
                // resourcePatternResolver.getResources出来的classPathResources，metadataReader对其进行getInputStream的时候为什么返回null呢？
                // 不得不做一个exists判断
                if (!resource.exists()) {
                    if (debugEnabled) {
                        logger.debug("Ignored because not exists:" + resource);
                    }
                } else if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory
                            .getMetadataReader(resource);
                    if (isCandidateComponent(metadataReader)) {
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(
                                metadataReader);
                        sbd.setResource(resource);
                        sbd.setSource(resource);
                        if (isCandidateComponent(sbd)) {
                            if (debugEnabled) {
                                logger.debug("Identified candidate component class: " + resource);
                            }
                            candidates.add(sbd);
                        } else {
                            if (traceEnabled) {
                                logger.trace("Ignored because not a interface top-level class: "
                                        + resource);
                            }
                        }
                    } else {
                        if (traceEnabled) {
                            logger.trace("Ignored because not matching any filter: " + resource);
                        }
                    }
                } else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: " + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during jade scanning", ex);
        }
        return candidates;
    }

    /**
     * Register the default filter for {@link Component @Component}. This
     * will implicitly register all annotations that have the
     * {@link Component @Component} meta-annotation including the
     * {@link Repository @Repository}, {@link Service @Service}, and
     * {@link Controller @Controller} stereotype annotations.
     */
    protected void registerDefaultFilters() {
        addIncludeFilter(new AnnotationTypeFilter(DAO.class));
    }

    /**
     * Determine whether the given class does not match any exclude filter
     * and does match at least one include filter.
     * 
     * @param metadataReader the ASM ClassReader for the class
     * @return whether the class qualifies as a candidate component
     */
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the given bean definition qualifies as candidate.
     * <p>
     * The default implementation checks whether the class is concrete
     * (i.e. not abstract and not an interface). Can be overridden in
     * subclasses.
     * 
     * @param beanDefinition the bean definition to check
     * @return whether the bean definition qualifies as a candidate
     *         component
     */
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata()
                .isIndependent());
    }

}
