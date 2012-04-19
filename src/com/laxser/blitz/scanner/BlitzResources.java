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
package com.laxser.blitz.scanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.laxser.blitz.scanning.LoadScope;
import com.laxser.blitz.scanning.ResourceRef;
import com.laxser.blitz.scanning.BlitzScanner;

/**
 * 用于配置Blitz负责管理的模块，根据提供的applicationContext
 * 
 * @author laxser Date 2012-3-22 下午4:24:38
 * @contact [duqifan@gmail.com]
 * @BlitzResources.java
 * 
 */
public class BlitzResources
{

	protected static Log logger = LogFactory.getLog(BlitzResources.class);

	public static List<Resource> findContextResources(LoadScope load)
			throws IOException
	{
		if (logger.isInfoEnabled()) {
			logger.info("[applicationContext] start to found applicationContext files ...");
		}
		String[] scope = load.getScope("applicationContext");
		if (logger.isDebugEnabled()) {
			if (scope == null) {
				logger.debug("[applicationContext] use default scope"
						+ " [all class folders and blitz managed jar files]");
			}
			else {
				logger.debug("[applicationContext] use scope: "
						+ Arrays.toString(scope));
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[applicationContext] call 'findFiles'");
		}
		List<ResourceRef> resources = BlitzScanner.getInstance()
				.getJarOrClassesFolderResources(scope);
		if (logger.isDebugEnabled()) {
			logger.debug("[applicationContext] exits from 'findFiles'");
			logger.debug("[applicationContext] it has " + resources.size()
					+ " classes folders or jar files "
					+ "in the applicationContext scope: " + resources);

			logger.debug("[applicationContext] iterates the 'findFiles'"
					+ " classes folders or jar files; size=" + resources.size());
		}

		List<Resource> ctxResources = new LinkedList<Resource>();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		int index = 0;
		for (ResourceRef ref : resources) {
			index++;
			if (ref.hasModifier("applicationContext")) {
				Resource[] founds = ref.getInnerResources(
						resourcePatternResolver, "applicationContext*.xml");
				List<Resource> asList = Arrays.asList(founds);
				ctxResources.addAll(asList);
				if (logger.isDebugEnabled()) {
					logger.debug("[applicationContext] found applicationContext resources ("
							+ index + "/" + resources.size() + ": " + asList);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("[applicationContext] ignored bacause not marked as 'blitz:applicationContext' or 'blitz:*'  ("
							+ index + "/" + resources.size() + ": " + ref);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[applicationContext] FOUND " + ctxResources.size()
					+ " applcationContext files: " + ctxResources);
		}
		return ctxResources;
	}

	public static String[] findMessageBasenames(LoadScope load)
			throws IOException
	{
		if (logger.isInfoEnabled()) {
			logger.info("[messages] start to found blitz.root 'messages' files ...");
		}
		String[] scope = load.getScope("messages");

		if (logger.isDebugEnabled()) {
			logger.debug("[messages] call 'scoped'");
		}
		List<ResourceRef> resources = BlitzScanner.getInstance()
				.getJarOrClassesFolderResources(scope);

		if (logger.isDebugEnabled()) {
			logger.debug("[messages] exits from 'scoped'");
			logger.debug("[messages] it has " + resources.size()
					+ " classes folders or jar files "
					+ "in the messages scope: " + resources);

			logger.debug("[messages] iterates the 'scoped'"
					+ " classes folders or jar files; size=" + resources.size());
		}

		List<String> messagesResources = new LinkedList<String>();
		int index = 0;
		for (ResourceRef ref : resources) {
			index++;
			if (ref.hasModifier("messages")) {
				messagesResources.add(ref.getInnerResourcePattern("messages"));
				if (logger.isDebugEnabled()) {
					logger.debug("[messages] add messages base name (" + index
							+ "/" + resources.size() + ": " + ref);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("[messages] ignored bacause not marked as 'blitz:messages' or 'blitz:*'  ("
							+ index + "/" + resources.size() + ": " + ref);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[messages] found " + messagesResources.size()
					+ " messages base names: " + messagesResources);
		}

		return messagesResources.toArray(new String[0]);
	}
}
