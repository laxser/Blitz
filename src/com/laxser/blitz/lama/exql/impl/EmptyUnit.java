package com.laxser.blitz.lama.exql.impl;

import com.laxser.blitz.lama.exql.ExprResolver;
import com.laxser.blitz.lama.exql.ExqlContext;
import com.laxser.blitz.lama.exql.ExqlUnit;

/**
 * 输出空白的语句单元, 代替空的表达式。
 * 
 * @author laxser  Date 2012-3-22 下午3:55:47
@contact [duqifan@gmail.com]
@EmptyUnit.java

 */
public class EmptyUnit implements ExqlUnit {

    @Override
    public boolean isValid(ExprResolver exprResolver) {
        // Empty unit is always valid.
        return true;
    }

    @Override
    public void fill(ExqlContext exqlContext, ExprResolver exprResolver) throws Exception {
        // Do nothing.
    }

	@Override
	public void toXml(StringBuffer xml,String prefix) {
		xml.append(prefix).append("<unit>\n");
		xml.append(prefix).append("<type>EmptyUnit</type>\n");
		xml.append(prefix).append("</unit>\n");
		
	}
}
