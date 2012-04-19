package com.laxser.blitz.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An tool for config and debug.<br>
 * You can change config online.<br>
 * The object is thread safe. There are three <code>Map</code>:
 * <ol>
 * <li>
 * 
 * <pre>
 * Map&lt;String, Integer&gt;: store 'int'
 * </pre>
 * 
 * </li>
 * <li>
 * 
 * <pre>
 * Map&lt;String, String&gt;: store 'String'
 * </pre>
 * 
 * </li>
 * <li>
 * 
 * <pre>
 * Map&lt;String, Boolean&gt;: store 'boolean'
 * </pre>
 * 
 * </li>
 * </ol>
 * Using three map for <i>'ClassCaseException'</i> and <i>'instanceof'</i>
 * operation
 * <pre>
 * The key is <code>String</code> format, so the key may be duplicate.
 * Using  {@link java.util.UUID} to avoid repetition of the key.
 * <b>For example:homeaction_68a3c046-745d-4824-8915-3d5d29d93c19 or homeaction_68a3c046745d482489153d5d29d93c19</b>
 * </pre>
 * @author laxser  Date 2012-4-12 上午10:04:09
@contact [duqifan@gmail.com]
@ConfigCenter.java
 */
public final class ConfigCenter {

    static Map<String, Boolean> booleanMap = new ConcurrentHashMap<String, Boolean>();

    static Map<String, Integer> integerMap = new ConcurrentHashMap<String, Integer>();

    static Map<String, String> stringMap = new ConcurrentHashMap<String, String>();

    private static void checkKey(String key) {
        if (key == null) throw new IllegalArgumentException("the key must not be null");
    }

    /**
     * get all Boolean config
     * 
     * @return an unmodifiable map
     */
    public static Map<String, Boolean> getAllBooleanConfig() {
        return Collections.unmodifiableMap(booleanMap);
    }

    /**
     * get all config (include String/Boolean/Integer)
     * 
     * @return an unmodifiable map
     */
    public static Map<String, Object> getAllConfig() {
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.putAll(stringMap);
        ret.putAll(integerMap);
        ret.putAll(booleanMap);
        return Collections.unmodifiableMap(ret);
    }

    /**
     * get all Integer config
     * 
     * @return an unmodifiable map
     */
    public static Map<String, Integer> getAllIntegerConfig() {
        return Collections.unmodifiableMap(integerMap);
    }

    /**
     * get all String config
     * 
     * @return an unmodifiable map
     */
    public static Map<String, String> getAllStringConfig() {
        return Collections.unmodifiableMap(stringMap);
    }

    /**
     * get a Boolean value
     * 
     * @param key the key
     * @return the value for the value in config center or <i>false</i>
     *         when nothing matches the key.
     * @throws IllegalArgumentException if the key is null
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * get a boolean value
     * 
     * @param key the key
     * @param defaultValue the default value
     * @return the value for the value in config center or 'defaultValue'
     *         when nothing matches the key
     * @throws IllegalArgumentException if the key is null
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        checkKey(key);
        Boolean b = booleanMap.get(key);
        if (b == null) return defaultValue;
        return b.booleanValue();
    }

    /**
     * get an Integer value
     * 
     * @param key the key
     * @return the value for the value in config center or <i>-1</i> when
     *         nothing matches the key.
     * @throws IllegalArgumentException if the key is null
     */
    public static int getInteger(String key) {
        return getInteger(key, -1);
    }

    /**
     * get an Integer value
     * 
     * @param key the key
     * @param defaultValue the default value
     * @return the value for the value in config center or 'defaultValue'
     *         when nothing matches the key
     * @throws IllegalArgumentException if the key is null
     */
    public static int getInteger(String key, int defaultValue) {
        checkKey(key);
        Integer b = integerMap.get(key);
        if (b == null) return defaultValue;
        return b.intValue();
    }

    /**
     * get a String value
     * 
     * @param key the key
     * @return the value for the value in config center or <i>null</i> when
     *         nothing matches the key.
     * @throws IllegalArgumentException if the key is null
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * get a String value
     * 
     * @param key the key
     * @param defaultValue the default value
     * @return the value for the value in config center or 'defaultValue'
     *         when nothing matches the key
     * @throws IllegalArgumentException if the key is null
     */
    public static String getString(String key, String defaultValue) {
        checkKey(key);
        String b = stringMap.get(key);
        if (b == null) return defaultValue;
        return b;
    }

    /**
     * store a 'Boolean' value
     * 
     * @param key the key
     * @param value the value
     * @throws IllegalArgumentException if the key is null
     */
    public static void setBoolean(String key, boolean value) {
        checkKey(key);
        booleanMap.put(key, new Boolean(value));
    }

    /**
     * store an 'Integer' value
     * 
     * @param key the key
     * @param value the value
     * @throws IllegalArgumentException if the key is null
     */
    public static void setInteger(String key, int value) {
        checkKey(key);
        integerMap.put(key, new Integer(value));
    }

    /**
     * store a 'String' value
     * 
     * @param key the key
     * @param value the value
     * @throws IllegalArgumentException if the key is null
     */
    public static void setString(String key, String value) {
        checkKey(key);
        stringMap.put(key, value);
    }

    /**
     * remove a 'String' key if exist
     * 
     * @param key the key
     * @throws IllegalArgumentException if the key is null
     *
     */
    public static void removeString(String key) {
        checkKey(key);
        if (stringMap.containsKey(key)) stringMap.remove(key);
    }

    /**
     * remove an 'Integer' key if exist
     * 
     * @param key the key
     * @throws IllegalArgumentException if the key is null
     * 
     */
    public static void removeInteger(String key) {
        checkKey(key);
        if (integerMap.containsKey(key)) integerMap.remove(key);
    }

    /**
     * remove a 'Boolean' key if exist
     * 
     * @param key the key
     * @throws IllegalArgumentException if the key is null
     * 
     */
    public static void removeBoolean(String key) {
        checkKey(key);
        if (booleanMap.containsKey(key)) booleanMap.remove(key);
    }

    private ConfigCenter() {
    }

    @Override
    public String toString() {
        return getAllConfig().toString();
    }
}
