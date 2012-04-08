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
package com.laxser.blitz.web.impl.mapping;

import com.laxser.blitz.util.BlitzStringUtil;
/**
 *常量的一些映射定义 
 * @author laxser  Date 2012-4-5 下午3:11:46
@contact [duqifan@gmail.com]
@ConstantMapping.java

 */
public class ConstantMapping implements Mapping {

    /** 该映射代表的结点 */
    private MappingNode mappingNode;

    /** 规范化的地址定义 */
    private final String definition;

    public ConstantMapping(String definition) {
        this.definition = definition;
    }

    @Override
    public MappingNode getMappingNode() {
        return mappingNode;
    }

    @Override
    public void setMappingNode(MappingNode mappingNode) {
        this.mappingNode = mappingNode;
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    @Override
    public String getParameterName() {
        // 没有参数
        return null;
    }

    @Override
    public MatchResult match(CharSequence input) {
        boolean matched = BlitzStringUtil.startsWith(input, definition);
        return !matched ? null : new MatchResultImpl(mappingNode, definition);
    }

    @Override
    public int compareTo(Mapping o) {
        if (o instanceof ConstantMapping) {
            String opath = ((ConstantMapping) o).definition;
            if (opath.length() == definition.length()) {
                return this.definition.compareTo(opath); // 字母顺序
            }
            if (this.definition.length() == 0) {
                return 1;
            }
            return opath.length() - definition.length();
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "ConstantMapping[" + this.definition + "]";
    }

}
