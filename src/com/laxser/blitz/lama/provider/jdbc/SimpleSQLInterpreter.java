package com.laxser.blitz.lama.provider.jdbc;

import java.sql.SQLSyntaxErrorException;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.jdbc.BadSqlGrammarException;

import com.laxser.blitz.lama.exql.ExqlPattern;
import com.laxser.blitz.lama.exql.impl.ExqlContextImpl;
import com.laxser.blitz.lama.exql.impl.ExqlPatternImpl;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Modifier;
import com.laxser.blitz.lama.provider.SQLInterpreter;
import com.laxser.blitz.lama.provider.SQLInterpreterResult;

/**
 * 提供动态: SQL 语句功能的 {@link DataAccess} 实现。
 * 
 * @author laxser  Date 2012-3-22 下午4:02:13
@contact [duqifan@gmail.com]
@SimpleSQLInterpreter.java

 */
public class SimpleSQLInterpreter implements SQLInterpreter {

	@Override
	// 转换 lamaSQL 语句为正常的 SQL 语句
	public SQLInterpreterResult interpret(DataSource dataSource, String sql,
			Modifier modifier, Map<String, Object> parametersAsMap,
			Object[] parametersAsArray) {

		// 转换语句中的表达式
		ExqlPattern pattern = ExqlPatternImpl.compile(sql);
		ExqlContextImpl context = new ExqlContextImpl(sql.length() + 32);

		try {
			pattern.execute(context, parametersAsMap, modifier.getDefinition()
					.getConstants());

		} catch (Exception e) {
			String daoInfo = modifier.toString();
			throw new BadSqlGrammarException(daoInfo, sql,
					new SQLSyntaxErrorException(daoInfo + " @SQL('" + sql
							+ "')", e));
		}
		return context;
	}

}
