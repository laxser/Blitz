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
package com.laxser.blitz.web.impl.view;

import java.util.Locale;


import org.springframework.web.servlet.View;

import com.laxser.blitz.web.Invocation;

/**
 * 
 * @author laxser  Date 2012-5-21 上午11:13:21
@contact [duqifan@gmail.com]
@ViewDispatcher.java

 * 
 */
public interface ViewDispatcher {

    public View resolveViewName(Invocation inv, String viewPath, Locale locale) throws Exception;

}
