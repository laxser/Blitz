/*
 * Copyright 2007-2010 the original author or authors.
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
package com.laxser.blitz.advancedinterceptor;

/**
 * 如果拦截器实现这个接口那么将拦截所有名字顺序的
 *@author laxser  Date 2012-4-7 下午5:52:35
@contact [duqifan@gmail.com]
@Named.java

 * 
 */
public interface Named  {

    /**
     * 
     */
    public void setName(String name);

    /**
     * 
     * @return
     */
    public String getName();
}
