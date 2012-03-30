package com.laxser.blitz.lama.provider.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.core.RowMapper;

import com.laxser.blitz.lama.cache.Cache;
import com.laxser.blitz.lama.cache.CacheProvider;
import com.laxser.blitz.lama.provider.DataAccess;
import com.laxser.blitz.lama.provider.Modifier;

/**
 * 提供包含缓存的 {@link DataAccess} 实现。
 * 
 * @author han.liao
 */
public class CacheDataAccess implements DataAccess {

    // 输出日志
    private static final Log logger = LogFactory.getLog(CacheDataAccess.class);

    // 参数的模板
    private static final Pattern PATTERN = Pattern.compile("\\:([a-zA-Z0-9_\\.]*)");

    // 可配置的缓存实现
    private final CacheProvider cacheProvider;

    private final DataAccess dataAccess;

    public CacheDataAccess(DataAccess dataAccess, CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
        this.dataAccess = dataAccess;
    }

    @Override
    public List<?> select(String sql, Modifier modifier, Map<String, Object> parameters,
            RowMapper rowMapper) {

        com.laxser.blitz.lama.annotation.Cache cacheAnno = modifier
                .getAnnotation(com.laxser.blitz.lama.annotation.Cache.class);
        if (cacheAnno != null) {

            // 检查返回值类型是否能够缓存
            if (isReturnTypeCacheable(modifier.getReturnType())) {

                Cache cache = cacheProvider.getCacheByPool(cacheAnno.pool());

                // 处理主键值, 替换其中的 :name 参数
                String cacheKey = buildKey(cacheAnno.key(), parameters);

                // 尝试从缓存获取对象
                Object value = cache.get(cacheKey);
                if (value != null) {
                    return Arrays.asList(value);
                }

                // 查询对象，并添加到缓存
                List<?> list = dataAccess.select(sql, modifier, parameters, rowMapper);

                final int size = list.size();
                if (size == 1) {
                    cache.set(cacheKey, list.get(0), cacheAnno.expiry());
                }

                return list; // 返回列表

            } else {

                // 输出警告
                if (logger.isWarnEnabled()) {
                    logger.warn("@Cache annotated method need return bean:\n    " + modifier);
                }
            }
        }

        // 执行原有的查询
        return dataAccess.select(sql, modifier, parameters, rowMapper);
    }

    @Override
    public int update(String sql, Modifier modifier, Map<String, Object> parameters) {

        // 先执行原有的语句
        int number = dataAccess.update(sql, modifier, parameters);

        com.laxser.blitz.lama.annotation.CacheDelete cacheDelete = modifier
                .getAnnotation(com.laxser.blitz.lama.annotation.CacheDelete.class);
        if (cacheDelete != null) {

            Cache cache = cacheProvider.getCacheByPool(cacheDelete.pool());

            // 清除缓存的主键
            for (String key : cacheDelete.key()) {

                // 处理主键值, 替换其中的 :name 参数
                String cacheKey = buildKey(key, parameters);

                // 从缓存清除对象
                cache.delete(cacheKey);
            }
        }

        return number;
    }

    @Override
    public Object insertReturnId(String sql, Modifier modifier, Map<String, Object> parameters) {

        // 先执行原有的语句
        Object number = dataAccess.insertReturnId(sql, modifier, parameters);

        com.laxser.blitz.lama.annotation.CacheDelete cacheDelete = modifier
                .getAnnotation(com.laxser.blitz.lama.annotation.CacheDelete.class);
        if (cacheDelete != null) {

            Cache cache = cacheProvider.getCacheByPool(cacheDelete.pool());

            // 清除缓存的主键
            for (String key : cacheDelete.key()) {

                // 处理主键值, 替换其中的 :name 参数
                String cacheKey = buildKey(key, parameters);

                // 从缓存清除对象
                cache.delete(cacheKey);
            }
        }

        return number;
    }

    /**
     * 检查返回值类型是否能够缓存。
     * 
     * @param returnClassType - 返回值类型
     * 
     * @return 类型是否能够缓存
     */
    private static boolean isReturnTypeCacheable(Class<?> returnClassType) {

        return (List.class != returnClassType) && (Collection.class != returnClassType)
                && (Set.class != returnClassType) && !returnClassType.isArray();
    }

    /**
     * 查找模板 KEY 中所有的 :name, :name.property 参数替换成实际值。
     * 
     * @param key - 作为模板的 KEY
     * @param parameters - 传入的参数
     * 
     * @return 最终的缓存 KEY
     */
    private static String buildKey(String key, Map<String, Object> parameters) {

        // 匹配符合  :name 格式的参数
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {

            StringBuilder builder = new StringBuilder();

            int index = 0;

            do {
                // 提取参数名称
                final String name = matcher.group(1).trim();

                Object value = null;

                // 解析  a.b.c 类型的名称 
                int find = name.indexOf('.');
                if (find >= 0) {

                    // 用  BeanWrapper 获取属性值
                    Object bean = parameters.get(name.substring(0, find));
                    if (bean != null) {
                        value = new BeanWrapperImpl(bean)
                                .getPropertyValue(name.substring(find + 1));
                    }

                } else {

                    // 获取参数值
                    value = parameters.get(name);
                }

                // 拼装参数值
                builder.append(key.substring(index, matcher.start()));
                builder.append(value);

                index = matcher.end();

            } while (matcher.find());

            // 拼装最后一段
            builder.append(key.substring(index));

            return builder.toString();
        }

        return key;
    }

    @Override
    public int[] batchUpdate(String sql, Modifier modifier, List<Map<String, Object>> parametersList) {
        throw new UnsupportedOperationException();
    }

    // 测试代码
    public static void main(String... args) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("id", 102l);
        parameters.put("name", "han.liao");
        parameters.put("size", 5);

        String cacheKey = buildKey("obj-:id-:name:size", parameters);

        System.out.println(cacheKey);
    }
}
