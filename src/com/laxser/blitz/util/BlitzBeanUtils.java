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

package com.laxser.blitz.util;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
/**
 * 
 * @author laxser  Date 2012-3-28 上午10:34:29
@contact [duqifan@gmail.com]
@BlitzBeanUtils.java

 * 
 */
public class BlitzBeanUtils extends BeanUtils {

    public static Object instantiateClass(@SuppressWarnings("rawtypes") Class clazz) throws BeanInstantiationException {
        // spring's : Object mappedObject = BeanUtils.instantiateClass(this.mappedClass);
        // jade's : private Object instantiateClass(this.mappedClass);
        // why: 经过简单的笔记本测试，mappedClass.newInstrance性能比BeanUtils.instantiateClass(mappedClass)快1个数量级
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new BeanInstantiationException(clazz, ex.getMessage(), ex);
        }
    }
}
