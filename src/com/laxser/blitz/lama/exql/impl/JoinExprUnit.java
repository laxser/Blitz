package com.laxser.blitz.lama.exql.impl;

import com.laxser.blitz.lama.exql.ExprResolver;
import com.laxser.blitz.lama.exql.ExqlContext;
import com.laxser.blitz.lama.exql.ExqlUnit;
import com.laxser.blitz.lama.exql.util.ExqlUtils;


/**
 * 直接输出表达式内容的语句单元, 例如: '##(:expr)' 形式的表达式，内容不经转义输出。
 * 
 * @author han.liao
 */
public class JoinExprUnit implements ExqlUnit {

	private final String expr;

	/**
	 * 构造输出表达式内容的语句单元。
	 * 
	 * @param text
	 *            - 输出的表达式
	 */
	public JoinExprUnit(String expr) {
		this.expr = expr;
	}

	@Override
	public boolean isValid(ExprResolver exprResolver) {

		// 解释表达式内容
		Object obj = ExqlUtils.execExpr(exprResolver, expr);

		// 表达式内容有效
		return ExqlUtils.isValid(obj);
	}

	@Override
	public void fill(ExqlContext exqlContext, ExprResolver exprResolver)
			throws Exception {

		// 解释表达式内容
		Object obj = exprResolver.executeExpr(expr);

		// 直接输出未经转义的对象内容
		exqlContext.fillText(String.valueOf(obj));
	}

	@Override
	public void toXml(StringBuffer xml,String prefix) {
		xml.append(prefix).append("<unit>\n");
		xml.append(prefix).append(BLANK).append("<type>JoinExprUnit</type>\n");
		xml.append(prefix).append(BLANK).append("<expr>" + expr + "</expr>\n");
		xml.append(prefix).append("</unit>\n");
	}
}
