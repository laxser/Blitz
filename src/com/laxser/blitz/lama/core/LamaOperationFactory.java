package com.laxser.blitz.lama.core;

import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Modifier;

/**
 * 定义创建: {@link LamaOperation} 的工厂。
 * 
 *@author laxser  Date 2012-3-22 下午3:43:47
@contact [duqifan@gmail.com]
@LamaOperationFactory.java

 */
public interface LamaOperationFactory {

    /**
     * 创建: {@link LamaOperation} 对象。
     * 
     * @return {@link LamaOperation} 对象
     */
    public LamaOperation getOperation(DataAccess dataAccess, Modifier modifier);
}
