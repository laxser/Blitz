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
package com.laxser.blitz.web.instruction;

import com.laxser.blitz.web.Invocation;

/**
 * 
 * @author laxser  Date 2012-4-12 上午10:28:00
@contact [duqifan@gmail.com]
@InstructionExecutor.java
指令执行器，用来接收不同指令并且执行
 * 
 */
public interface InstructionExecutor {

    public Object render(Invocation inv, Object instruction) throws Exception;

}
