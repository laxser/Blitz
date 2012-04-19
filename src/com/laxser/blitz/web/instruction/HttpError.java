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
 * 
 * @author laxser  Date 2012-4-12 上午10:26:27
@contact [duqifan@gmail.com]
@HttpError.java
HTTP错误的帮助
 * 
 */

public class HttpError implements InstructionHelper {

    public static HttpErrorInstruction code(int code) {
        HttpErrorInstruction instruction = new HttpErrorInstruction();
        instruction.setCode(code);
        return instruction;
    }

    public static HttpErrorInstruction code(int code, String message) {
        HttpErrorInstruction instruction = new HttpErrorInstruction();
        instruction.setCode(code);
        instruction.setMessage(message);
        return instruction;
    }
}
