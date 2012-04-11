package com.laxser.blitz.lama.provider;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.annotation.Order;
import com.laxser.blitz.lama.provider.Modifier;
/**
 * 可用 {@link Order}来调节优先级，根据 {@link Order} 语义，值越小越有效；
 * <p>
 * 如果没有标注 {@link Order} 使用默认值0。
 * 
 * @author laxser  Date 2012-4-11 上午10:50:24
@contact [duqifan@gmail.com]
@SQLInterpreter.java

 * 
 */
//按Spring语义规定，Order值越高该解释器越后执行
@Order(0)
public interface SQLInterpreter {

    /**
     * 
     * @param dataSource
     * @param sql
     * @param modifier
     * @param parametersAsMap
     * @param parametersAsArray 可以为null
     * @return
     */
    SQLInterpreterResult interpret(DataSource dataSource, String sql, Modifier modifier,
            Map<String, Object> parametersAsMap, Object[] parametersAsArray);

}
