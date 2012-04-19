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

/**
 * Forward的相关操作的指示
 * @author laxser  Date 2012-4-12 上午10:25:08
@contact [duqifan@gmail.com]
@Forward.java

 * 
 */
public class Forward implements InstructionHelper {

    public static ForwardInstruction path(String path) {
        ForwardInstruction instruction = new ForwardInstruction();
        instruction.path(path);
        return instruction;
    }

    public static ForwardInstruction module(String module) {
        ForwardInstruction instruction = new ForwardInstruction();
        instruction.module(module);
        return instruction;
    }

    public static ForwardInstruction controller(String controller) {
        ForwardInstruction instruction = new ForwardInstruction();
        instruction.controller(controller);
        return instruction;
    }

    public static ForwardInstruction action(String action) {
        ForwardInstruction instruction = new ForwardInstruction();
        instruction.action(action);
        return instruction;
    }

}
