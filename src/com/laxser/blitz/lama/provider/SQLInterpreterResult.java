package com.laxser.blitz.lama.provider;

/**
 * SQL语句编译填充参数后输出结果。
 * 
 *@author laxser  Date 2012-4-11 上午10:50:16
@contact [duqifan@gmail.com]
@SQLInterpreterResult.java

 * 
 */
public interface SQLInterpreterResult {

	/**
	 * 返回真正的SQL语句
	 * 
	 * @return
	 */
	String getSQL();

	/**
	 * 返回参数列表
	 * 
	 * @return
	 */
	Object[] getParameters();
}
