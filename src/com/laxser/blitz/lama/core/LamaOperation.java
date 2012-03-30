package com.laxser.blitz.lama.core;

import java.util.Map;

import com.laxser.blitz.lama.provider.Modifier;


/**
 * 定义一组数据库操作。
 * 
 *@author laxser  Date 2012-3-22 下午3:43:42
@contact [duqifan@gmail.com]
@LamaOperation.java

 */
public interface LamaOperation {

    /**
     * 
     * @return
     */
    public Modifier getModifier();

    /**
     * 执行所需的数据库操作。
     * 
     * @return
     */
    public Object execute(Map<String, Object> parameters);
}
