package com.ezylang.evalex.utils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CollUtil {

    // ----------------------------------------------------------------------------------------------- new HashSet

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return set(false, ts);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     * @since 4.1.10
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
        return (LinkedHashSet<T>) set(true, ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> set(boolean isSorted, T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<>() : new HashSet<>();
        }
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        final HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param collection 集合，用于初始化Set
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param iter     {@link Iterator}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
        if (null == iter) {
            return set(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>         集合元素类型
     * @param isSorted    是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param enumeration {@link Enumeration}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumeration) {
        if (null == enumeration) {
            return set(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }


    /**
     * 排序集合，排序不会修改原集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        List<T> list = new ArrayList<>(collection);
        list.sort(comparator);
        return list;
    }


    /**
     * 排序Map
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param map        Map
     * @param comparator Entry比较器
     * @return {@link TreeMap}
     * @since 3.0.9
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        final TreeMap<K, V> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    /**
     * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
     *
     * @param <K>             键类型
     * @param <V>             值类型
     * @param entryCollection Entry集合
     * @param comparator      {@link Comparator}
     * @return {@link LinkedList}
     * @since 3.0.9
     */
    public static <K, V> LinkedHashMap<K, V> sortToMap(Collection<Map.Entry<K, V>> entryCollection, Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> list = new LinkedList<>(entryCollection);
        list.sort(comparator);

        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param map        被排序的Map
     * @param comparator {@link Comparator}
     * @return {@link LinkedList}
     * @since 3.0.9
     */
    public static <K, V> LinkedHashMap<K, V> sortByEntry(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return sortToMap(map.entrySet(), comparator);
    }

    /**
     * 将Set排序（根据Entry的值）
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param collection 被排序的{@link Collection}
     * @return 排序后的Set
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> List<Map.Entry<K, V>> sortEntryToList(Collection<Map.Entry<K, V>> collection) {
        List<Map.Entry<K, V>> list = new LinkedList<>(collection);
        list.sort((o1, o2) -> {
            V v1 = o1.getValue();
            V v2 = o2.getValue();

            if (v1 instanceof Comparable) {
                return ((Comparable) v1).compareTo(v2);
            } else {
                return v1.toString().compareTo(v2.toString());
            }
        });
        return list;
    }

    // ------------------------------------------------------------------------------------------------- forEach

    /**
     * 循环遍历 {@link Iterable}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @param consumer {@link Consumer} 遍历的每条数据处理器
     * @since 5.4.7
     */
    public static <T> void forEach(Iterable<T> iterable, Consumer<T> consumer) {
        if (iterable == null) {
            return;
        }
        forEach(iterable.iterator(), consumer);
    }

    /**
     * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @param consumer {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
        if (iterator == null) {
            return;
        }
        int index = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next(), index);
            index++;
        }
    }

    /**
     * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @param consumer    {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        if (enumeration == null) {
            return;
        }
        int index = 0;
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement(), index);
            index++;
        }
    }



    /**
     * 获取指定Map列表中所有的Value
     *
     * @param <V>           值类型
     * @param mapCollection Map列表
     * @return Value集合
     * @since 4.5.12
     */
    public static <V> List<V> values(Collection<Map<?, V>> mapCollection) {
        final List<V> values = new ArrayList<>();
        for (Map<?, V> map : mapCollection) {
            values.addAll(map.values());
        }

        return values;
    }

    /**
     * 转为只读集合
     *
     * @param <T> 元素类型
     * @param c   集合
     * @return 只读集合
     * @since 5.2.6
     */
    public static <T> Collection<T> unmodifiable(Collection<? extends T> c) {
        return Collections.unmodifiableCollection(c);
    }

    /**
     * 根据给定的集合类型，返回对应的空集合，支持类型包括：
     * *
     * <pre>
     *     1. NavigableSet
     *     2. SortedSet
     *     3. Set
     *     4. List
     * </pre>
     *
     * @param <E>             元素类型
     * @param <T>             集合类型
     * @param collectionClass 集合类型
     * @return 空集合
     * @since 5.3.1
     */
    @SuppressWarnings("unchecked")
    public static <E, T extends Collection<E>> T empty(Class<?> collectionClass) {
        if (null == collectionClass) {
            return (T) Collections.emptyList();
        }

        if (Set.class.isAssignableFrom(collectionClass)) {
            if (NavigableSet.class == collectionClass) {
                return (T) Collections.emptyNavigableSet();
            } else if (SortedSet.class == collectionClass) {
                return (T) Collections.emptySortedSet();
            } else {
                return (T) Collections.emptySet();
            }
        } else if (List.class.isAssignableFrom(collectionClass)) {
            return (T) Collections.emptyList();
        }

        // 不支持空集合的集合类型
        throw new IllegalArgumentException(String.format("[%s] is not support to get empty!", collectionClass));
    }


    /**
     * 填充List，以达到最小长度
     *
     * @param <T>    集合元素类型
     * @param list   列表
     * @param minLen 最小长度
     * @param padObj 填充的对象
     * @since 5.3.10
     */
    public static <T> void padLeft(List<T> list, int minLen, T padObj) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            padRight(list, minLen, padObj);
            return;
        }
        for (int i = list.size(); i < minLen; i++) {
            list.add(0, padObj);
        }
    }

    /**
     * 填充List，以达到最小长度
     *
     * @param <T>    集合元素类型
     * @param list   列表
     * @param minLen 最小长度
     * @param padObj 填充的对象
     * @since 5.3.10
     */
    public static <T> void padRight(Collection<T> list, int minLen, T padObj) {
        Objects.requireNonNull(list);
        for (int i = list.size(); i < minLen; i++) {
            list.add(padObj);
        }
    }

    /**
     * 使用给定的map将集合中的原素进行属性或者值的重新设定
     *
     * @param <E>         元素类型
     * @param <K>         替换的键
     * @param <V>         替换的值
     * @param iterable    集合
     * @param map         映射集
     * @param keyGenerate 映射键生成函数
     * @param biConsumer  封装映射到的值函数
     * @author nick_wys
     * @since 5.7.18
     */
    public static <E, K, V> void setValueByMap(Iterable<E> iterable, Map<K, V> map, Function<E, K> keyGenerate, BiConsumer<E, V> biConsumer) {
        iterable.forEach(x -> Optional.ofNullable(map.get(keyGenerate.apply(x))).ifPresent(y -> biConsumer.accept(x, y)));
    }

    // ---------------------------------------------------------------------------------------------- Interface start

    /**
     * 针对一个参数做相应的操作<br>
     * 此函数接口与JDK8中Consumer不同是多提供了index参数，用于标记遍历对象是第几个。
     *
     * @param <T> 处理参数类型
     * @author Looly
     */
    @FunctionalInterface
    public interface Consumer<T> extends Serializable {
        /**
         * 接受并处理一个参数
         *
         * @param value 参数值
         * @param index 参数在集合中的索引
         */
        void accept(T value, int index);
    }

    /**
     * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
     *
     * @param <K> KEY类型
     * @param <V> VALUE类型
     * @author Looly
     */
    @FunctionalInterface
    public interface KVConsumer<K, V> extends Serializable {
        /**
         * 接受并处理一对参数
         *
         * @param key   键
         * @param value 值
         * @param index 参数在集合中的索引
         */
        void accept(K key, V value, int index);
    }

}
