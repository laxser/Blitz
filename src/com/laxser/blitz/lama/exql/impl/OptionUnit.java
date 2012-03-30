package com.laxser.blitz.lama.exql.impl;

import com.laxser.blitz.lama.exql.ExprResolver;
import com.laxser.blitz.lama.exql.ExqlContext;
import com.laxser.blitz.lama.exql.ExqlUnit;

/**
 * 条件输出子单元的语句单元, 例如一个: {...}? 语句段。
 * 
 * @author han.liao
 */
public class OptionUnit implements ExqlUnit {

	private final ExqlUnit unit;

	public OptionUnit(ExqlUnit unit) {
		this.unit = unit;
	}

	@Override
	public boolean isValid(ExprResolver exprResolver) {

		// 条件单元始终有效, 因为若子单元无效
		// 它就不会产生输出。
		return true;
	}

	@Override
	public void fill(ExqlContext exqlContext, ExprResolver exprResolver)
			throws Exception {

		// 当且仅当子单元有效时输出
		if (unit.isValid(exprResolver)) {
			unit.fill(exqlContext, exprResolver);
		}
	}

	@Override
	public void toXml(StringBuffer xml, String prefix) {
		xml.append(prefix).append("<unit>\n");
		xml.append(prefix).append(BLANK).append("<type>OptionUnit</type>\n");
		if (unit != null) {
			unit.toXml(xml, prefix + BLANK);
		}
		xml.append(prefix).append("</unit>\n");
	}
}
