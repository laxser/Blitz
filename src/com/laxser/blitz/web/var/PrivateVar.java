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
package com.laxser.blitz.web.var;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 仅仅提供Blitz框架内部使用，外部程序请勿调用.
 * 
 * @author laxser  Date 2012-3-22 下午5:02:30
@contact [duqifan@gmail.com]
@PrivateVar.java

 */
public final class PrivateVar {

    // 当前环境的ServletContext对象，由BlitzFilter初始化时设置通过servletContext(ServlerContext)设置进来
    //    private static ServletContext servletContext;

    // ---------------------------------------------------------------

    /**
     * 获取当前Web应用的根Spring上下文环境.
     * <p>
     * 所谓根Spring上下文环境的对象都可以应用到各个module中.
     * 
     * @return
     */
    public static WebApplicationContext getRootWebApplicationContext(ServletContext servletContext) {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    /**
     * 将构造函数私有化，禁止实例化
     */
    private PrivateVar() {
        throw new AssertionError();
    }

    private static Properties blitzProperties;

    public static String getProperty(ServletContext servletContext, String name) {
        return getProperty(servletContext, name, null);
    }

    /**
     * 
     * @param name
     * @return
     */
    public static String getProperty(ServletContext servletContext, String name, String def) {
        if (blitzProperties == null) {
            String blitzPropertiesPath = "blitz.properties";
            if (servletContext != null) {
                blitzPropertiesPath = "/WEB-INF/blitz.properties";
            }
            Properties blitzProperties = new Properties();
            File file = new File(servletContext.getRealPath(blitzPropertiesPath));
            if (file.exists()) {
                InputStream in = null;
                try {
                    in = new FileInputStream(file);
                    blitzProperties.load(in);
                } catch (IOException e) {
                    throw new IllegalArgumentException(blitzPropertiesPath, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        }
                    }
                }
                PrivateVar.blitzProperties = blitzProperties;
            } else {
                PrivateVar.blitzProperties = new Properties();
            }
        }
        return blitzProperties.getProperty(name, def);
    }
}
