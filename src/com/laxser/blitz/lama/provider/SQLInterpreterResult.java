package com.laxser.blitz.lama.provider;

/**
 * SQL语句编译填充参数后输出结果。
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * @author 廖涵 [in355hz@gmail.com]
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
