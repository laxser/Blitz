/*
 * Copyright 2007-2012 the original author or authors.
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
package com.laxser.blitz.web.portal.impl;

import java.io.IOException;
import java.io.Writer;

import com.laxser.blitz.web.portal.Window;
import com.laxser.blitz.web.portal.WindowRender;


/**
 * 
 *@author laxser  Date 2012-3-23 下午4:58:18
@contact [duqifan@gmail.com]
@SimpleWindowRender.java
 
 */
public class SimpleWindowRender implements WindowRender {

    @Override
    public void render(Writer out, Window window) throws IOException {
        out.write(window.getContent());
    }

}
