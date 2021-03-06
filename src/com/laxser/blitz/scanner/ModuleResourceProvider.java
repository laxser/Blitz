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
package com.laxser.blitz.scanner;

import java.io.IOException;
import java.util.List;

import com.laxser.blitz.scanning.LoadScope;


/**
 * 用于发现规定查找范围内的web模块资源。
 * 
 * @see ModuleResource
 * 
 * @author laxser  Date 2012-3-22 下午4:36:08
@contact [duqifan@gmail.com]
@ModuleResourceProvider.java

 * 
 */
public interface ModuleResourceProvider {

    /**
     * 发现规定查找范围内的web模块资源。
     * 
     * @return
     */
    public List<ModuleResource> findModuleResources(LoadScope scope) throws IOException;
}
