package com.laxser.blitz.lama.exql.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.laxser.blitz.lama.exql.ExqlContext;
import com.laxser.blitz.lama.exql.util.ExqlUtils;
import com.laxser.blitz.lama.provider.SQLInterpreterResult;


/**
 * Exql解析上下文，同时包括了SQL解析的输出结果数据。
 * 
 * @author laxser  Date 2012-3-22 下午3:56:13
@contact [duqifan@gmail.com]
@ExqlContextImpl.java
*
*/
public class ExqlContextImpl implements ExqlContext, SQLInterpreterResult {

    // 输出的常量
    private static final String NULL = "NULL";

    private static final char QUESTION = '?';

    private static final char COMMA = ',';

    // 参数列表
    protected final ArrayList<Object> params = new ArrayList<Object>();

    // 输出缓冲区
    protected final StringBuilder builder;

    /**
     * 构造上下文对象。
     * 
     * @param capacity - 缓存的容量
     */
    public ExqlContextImpl(int capacity) {
        builder = new StringBuilder(capacity);
    }

    @Override
    public Object[] getParams() {
        return params.toArray();
    }

    @Override
    public void fillChar(char ch) {
        builder.append(ch);
    }

    @Override
    public void fillText(String string) {

        // 直接输出字符串
        builder.append(string);
    }

    @Override
    public void fillValue(Object obj) {

        if (obj instanceof Collection<?>) {

            // 展开  Collection 容器, 输出逗号分隔以支持 IN (...) 语法
            // "IN (:varlist)" --> "IN (?, ?, ...)"
            fillCollection((Collection<?>) obj);

        } else if ((obj != null) && obj.getClass().isArray() && obj.getClass() != byte[].class) {

            // 用数组构造  Collection 容器
            fillCollection(ExqlUtils.asCollection(obj));

        } else {

            // 直接输出参数, "uid > :var" --> "uid > ?"
            setParam(obj);

            builder.append(QUESTION);
        }
    }

    @Override
    public String flushOut() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return flushOut();
    }

    /**
     * 设置参数的内容。
     * 
     * @param value - 参数的内容
     */
    protected void setParam(Object value) {
        params.add(value);
    }

    /**
     * 输出集合对象到语句内容, 集合将被展开成 IN (...) 语法。
     * 
     * PS: IN :varlist --> IN (?, ?, ...)
     * 
     * @param collection - 输出的集合
     */
    private void fillCollection(Collection<?> collection) {

        int count = 0;

        // 展开  Collection 容器, 输出逗号分隔以支持 IN (...) 语法
        // "IN :varlist" --> "IN (?, ?, ...)"
        if (collection.isEmpty()) {

            // 输出  "IN (NULL)" 保证不会产生错误
            builder.append(NULL);

        } else {

            // 输出逗号分隔的参数表
            for (Object value : collection) {

                if (value != null) {

                    if (count > 0) {
                        builder.append(COMMA);
                    }

                    // 输出参数内容
                    setParam(value);

                    builder.append(QUESTION);

                    count++;
                }
            }
        }
    }

    //------------------implements InterpreterResult methods
    @Override
    public Object[] getParameters() {
        return getParams();
    }

    @Override
    public String getSQL() {
        return flushOut();
    }


}
