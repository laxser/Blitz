package com.laxser.blitz.lama.exql.impl;

import com.laxser.blitz.lama.exql.ExprResolver;
import com.laxser.blitz.lama.exql.ExqlContext;
import com.laxser.blitz.lama.exql.ExqlUnit;
import com.laxser.blitz.lama.exql.util.ExqlUtils;


/**
 * 按条件选择输出子单元内容的语句单元, 例如: '#if (:expr) {...} #else {...}' 形式的语句。
 * 
 * @author laxser  Date 2012-3-22 下午3:55:39
@contact [duqifan@gmail.com]
@ChoiceUnit.java

 */
public class ChoiceUnit implements ExqlUnit {

	private final String expr;

	private final ExqlUnit unitIfTrue, unitIfFalse;

	/**
	 * 构造一元的条件语句单元。
	 * 
	 * @param expr
	 *            - 条件表达式
	 * @param unit
	 *            - <code>true</code> 的输出
	 */
	public ChoiceUnit(String expr, ExqlUnit unit) {
		this.expr = expr;
		this.unitIfTrue = unit;
		this.unitIfFalse = null;
	}

	/**
	 * 构造二元的条件语句单元。
	 * 
	 * @param expr
	 *            - 条件表达式
	 * @param unitIfTrue
	 *            - <code>true</code> 的输出
	 * @param unitIfFalse
	 *            - <code>false</code> 的输出
	 */
	public ChoiceUnit(String expr, ExqlUnit unitIfTrue, ExqlUnit unitIfFalse) {
		this.expr = expr;
		this.unitIfTrue = unitIfTrue;
		this.unitIfFalse = unitIfFalse;
	}

	@Override
	public boolean isValid(ExprResolver exprResolver) {

		// 解释表达式内容
		Object obj = ExqlUtils.execExpr(exprResolver, expr);

		if (ExqlUtils.asBoolean(obj)) {

			// 检查第一个单元
			return unitIfTrue.isValid(exprResolver);

		} else if (unitIfFalse != null) {

			// 检查第二个单元
			return unitIfFalse.isValid(exprResolver);
		}

		// 条件单元有效
		return true;
	}

	@Override
	public void fill(ExqlContext exqlContext, ExprResolver exprResolver)
			throws Exception {

		// 解释表达式内容
		Object obj = exprResolver.executeExpr(expr);

		if (ExqlUtils.asBoolean(obj)) {

			// 输出第一个单元
			unitIfTrue.fill(exqlContext, exprResolver);

		} else if (unitIfFalse != null) {

			// 输出第二个单元
			unitIfFalse.fill(exqlContext, exprResolver);
		}
	}

	@Override
	public void toXml(StringBuffer xml, String prefix) {
		xml.append(prefix).append("<unit>\n");
		xml.append(prefix).append("<type>ChoiceUnit</type>\n");
		xml.append(prefix).append("<expr>" + expr + "</expr>\n");
		if (unitIfTrue != null) {
			unitIfTrue.toXml(xml, prefix + BLANK);
		}
		if (unitIfFalse != null) {
			unitIfFalse.toXml(xml, prefix + BLANK);
		}
		xml.append(prefix).append("</unit>\n");

	}
}
