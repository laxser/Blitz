/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.lama.provider.jdbc;

/**
 * 
 * @author laxser  Date 2012-3-22 下午4:01:46
@contact [duqifan@gmail.com]
@JdbcWrapper.java

 * 
 */
public interface JdbcWrapper extends Jdbc {

    /**
     * 
     * @param jdbc
     */
    public void setJdbc(Jdbc jdbc);

    /**
     * 
     * @return
     */
    public Jdbc getJdbc();

}
