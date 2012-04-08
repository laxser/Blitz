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
package com.laxser.blitz.web.impl.module;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.laxser.blitz.web.annotation.ReqMethod;


/**
 * 
 * @author laxser  Date 2012-4-5 下午3:23:11
@contact [duqifan@gmail.com]
@MethodRef.java

 * 
 */
public class MethodRef {

    private Method method;

    private Map<String, Set<ReqMethod>> mappings = new HashMap<String, Set<ReqMethod>>();

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void addMapping(ReqMethod reqMethod, String[] mappingPaths) {
        for (String mappingPath : mappingPaths) {
            Set<ReqMethod> mapping = mappings.get(mappingPath);
            if (mapping == null) {
                mapping = new HashSet<ReqMethod>();
                mappings.put(mappingPath, mapping);
            }
            mapping.add(reqMethod);
        }
    }

    public Map<String, Set<ReqMethod>> getMappings() {
        return mappings;
    }

}
