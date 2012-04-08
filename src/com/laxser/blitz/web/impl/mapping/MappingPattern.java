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
package com.laxser.blitz.web.impl.mapping;

/**
 * {@link MappingPattern} 封装字符串的匹配规则，不同的 {@link MatchMode} 匹配规则不一样
 * 
 * @see MatchMode
 * 
 *@author laxser  Date 2012-4-5 下午3:17:22
@contact [duqifan@gmail.com]
@MappingPattern.java

 * 
 */
public interface MappingPattern {

    /**
     * 返回匹配结果,返回空表示不能匹配
     * 
     * @param path
     * @return
     */
    java.util.regex.MatchResult match(CharSequence path);
}
