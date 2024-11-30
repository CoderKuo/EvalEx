package com.ezylang.evalex.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtil {

    /**
     * 默认初始大小
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param size     初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
     * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     * @return HashMap对象
     * @since 3.0.4
     */
    public static <K, V> HashMap<K, V> newHashMap(int size, boolean isLinked) {
        final int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
        return isLinked ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
    }


    /**
     * 新建一个HashMap
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     * @return HashMap对象
     */
    public static <K, V> HashMap<K, V> newHashMap(boolean isLinked) {
        return newHashMap(DEFAULT_INITIAL_CAPACITY, isLinked);
    }


    // ----------------------------------------------------------------------------------------------- value of

    /**
     * 将单一键值对转换为Map
     *
     * @param <K>   键类型
     * @param <V>   值类型
     * @param key   键
     * @param value 值
     * @return {@link HashMap}
     */
    public static <K, V> HashMap<K, V> of(K key, V value) {
        return of(key, value, false);
    }

    /**
     * 将单一键值对转换为Map
     *
     * @param <K>     键类型
     * @param <V>     值类型
     * @param key     键
     * @param value   值
     * @param isOrder 是否有序
     * @return {@link HashMap}
     */
    public static <K, V> HashMap<K, V> of(K key, V value, boolean isOrder) {
        final HashMap<K, V> map = newHashMap(isOrder);
        map.put(key, value);
        return map;
    }

    /**
     * 根据给定的Pair数组创建Map对象
     *
     * @param <K>   键类型
     * @param <V>   值类型
     * @param pairs 键值对
     * @return Map
     * @since 5.4.1
     * @deprecated 方法容易歧义，请使用 {@code #ofEntries(Entry[])}
     */
    @SafeVarargs
    @Deprecated
    public static <K, V> Map<K, V> of(Pair<K, V>... pairs) {
        final Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }


    /**
     * 将数组转换为Map（HashMap），支持数组元素类型为：
     *
     * <pre>
     * Map.Entry
     * 长度大于1的数组（取前两个值），如果不满足跳过此元素
     * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
     * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
     * </pre>
     *
     * <pre>
     * Map&lt;Object, Object&gt; colorMap = MapUtil.of(new String[][] {
     *    { "RED", "#FF0000" },
     *    { "GREEN", "#00FF00" },
     *    { "BLUE", "#0000FF" }
     * });
     * </pre>
     * <p>
     * 参考：commons-lang
     *
     * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
     * @return {@link HashMap}
     * @since 3.0.8
     */
    @SuppressWarnings("rawtypes")
    public static HashMap<Object, Object> of(Object[] array) {
        if (array == null) {
            return null;
        }
        final HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) object;
                map.put(entry.getKey(), entry.getValue());
            } else if (object instanceof Object[]) {
                final Object[] entry = (Object[]) object;
                if (entry.length > 1) {
                    map.put(entry[0], entry[1]);
                }
            } else if (object instanceof Iterable) {
                final Iterator iter = ((Iterable) object).iterator();
                if (iter.hasNext()) {
                    final Object key = iter.next();
                    if (iter.hasNext()) {
                        final Object value = iter.next();
                        map.put(key, value);
                    }
                }
            } else if (object instanceof Iterator) {
                final Iterator iter = ((Iterator) object);
                if (iter.hasNext()) {
                    final Object key = iter.next();
                    if (iter.hasNext()) {
                        final Object value = iter.next();
                        map.put(key, value);
                    }
                }
            } else {
                throw new IllegalArgumentException(String.format("Array element %s, '%s', is not type of Map.Entry or Array or Iterable or Iterator", i, object));
            }
        }
        return map;
    }
}
