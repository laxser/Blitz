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
package com.laxser.blitz.web;

/**
 * 
 * @author laxser  Date 2012-3-22 下午4:57:03
@contact [duqifan@gmail.com]
@ControllerErrorHandler.java

 * 
 */
public interface ControllerErrorHandler {

    /**
     * 
     * @param ex
     * @return
     */
    public Object onError(Invocation inv, Throwable ex) throws Throwable;

}
