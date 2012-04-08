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
package com.laxser.blitz.web.annotation.rest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表明所注解的方法可用于处理HTTP GET请求。
 * <p>
 * Get参数：<br>
 * 没有没有设置值，表示所注解的方法用于处理对控制器资源的GET请求；<br>
 * 如果设置值(可多个)，表示所注解的方法用于处理所设定地址资源的GET请求。
 * <p>
 * example： UserController下有一个方法xyz<br>
 * 1、在没有配置任何注解的情况下，xyz方法代表的资源是/user/xyz，支持GET和POST两种访问<br>
 * 2、如果对xyz标注了@Get()注解，xyz代表的资源是/user，并且仅支持GET访问<br>
 * 3、如果对xyz标注了@Get("abc")，xyz代表的是资源/user/abc，并且仅支持GET访问<br>
 * 
 * 
 * @author laxser  Date 2012-4-5 下午3:09:38
@contact [duqifan@gmail.com]
@Get.java

 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Get {

    String[] value() default { "" };
}
