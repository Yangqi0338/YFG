package com.base.sbc.config.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.exception.OtherException;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * @author 卞康
 * @date 2023/3/31 15:46:55
 */
public class CommonUtils {

    public static final String[] image_accept = {"jpg", "png", "jpeg"};
    public static final StrJoiner iCommaJoiner = StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.IGNORE);
    public static final StrJoiner eCommaJoiner = StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.TO_EMPTY);

    /**
     * 取多个set集合相交的数据，集合可为null，不进行筛选，但是任何一个set集合的长度为0，则无任何相交对象
     *
     * @param sets 可变集合数组
     * @param <T>  集合类型
     * @return 相交的set集合
     */
    @SafeVarargs
    public static <T> Set<T> findCommonElements(Set<T>... sets) {
        List<Set<T>> setList = new ArrayList<>();
        for (Set<T> set : sets) {
            if (set != null) {
                if (set.isEmpty()) {
                    return new HashSet<>();
                }
                setList.add(set);
            }
        }

        Set<T> commonSet = setList.get(0);
        for (Set<T> set : setList) {
            commonSet.retainAll(set);
        }
        return commonSet;
    }


    /**
     * 判断是否是初始化id
     * 为空 或者包含 -
     *
     * @param id
     * @return
     */
    public static boolean isInitId(String id) {
        return StringUtils.isBlank(id) || StringUtils.contains(id, StrUtil.DASHED);
    }

    public static StringBuilder newStr(JSONObject fieldJson, Object newObj) {
        StringBuilder istr = new StringBuilder();
        istr.append("{");
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());
        for (String s : arrayList) {
            Object val = BeanUtil.getProperty(newObj, fieldJson.getString(s));
            if (ObjectUtil.isEmpty(val)) {
                continue;
            }

            istr.append(s).append(":").append(val);
            istr.append(";");
        }
        istr.append("}");
        return istr;
    }

    /**
     * 获取所有ApiModelProperty注解字段
     */
    public static JSONObject getFieldJson(Object model) {
        Class<?> clazz = model.getClass();
        return getFieldJson(clazz);
    }

    public static JSONObject getFieldJson(Class<?> clazz) {
        Field[] declaredFields = ReflectUtil.getFields(clazz);
//        Field[] fields = ReflectUtil.getFields(clazz);
        JSONObject jsonObject = new JSONObject();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                String value = annotation.value();
                value = value.split("[:：(（;]")[0];
                jsonObject.put(value, field.getName());
            }
        }
        return jsonObject;
    }

    /**
     * 比较变更字段
     *
     * @param oldObj
     * @param newObj
     * @return
     */
    public static StringBuilder updateStr(Object oldObj, Object newObj, JSONObject fieldJson, JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{(id=" + BeanUtil.getProperty(oldObj, "id") + ")");
        for (String name : arrayList) {
            String key = fieldJson.getString(name);
            Object oldStr = BeanUtil.getProperty(oldObj, key);
            Object newStr = BeanUtil.getProperty(newObj, key);
            if (ObjectUtil.isEmpty(oldStr) && ObjectUtil.isEmpty(newStr)) {
                continue;
            }

            if (ObjectUtil.equals(newStr, oldStr)) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("oldStr", oldStr);
            jsonObject.put("newStr", newStr);
            jsonArray.add(jsonObject);
            stringBuilder.append(name).append(":");
            stringBuilder.append(oldStr).append("->").append(newStr);

            stringBuilder.append(";");
        }
        stringBuilder.append("}");
        return stringBuilder;
    }

    /**
     * 将创建修改信息设置为空
     *
     * @param t
     * @param <T>
     */
    public static <T extends BaseDataEntity> void resetCreateUpdate(T t) {
        t.setCreateId(null);
        t.setUpdateId(null);
        t.setCreateName(null);
        t.setUpdateName(null);
        t.setCreateDate(null);
        t.setUpdateDate(null);
    }


    /**
     * 获取所有字段,包括父类
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        // 获取当前类的所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));

        // 获取父类的所有字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            fields.addAll(getAllFields(superClass));
        }
        return fields;
    }

    /**
     * 获取所有字段,包括父类
     */
    public static List<Field> getAllFields(Object object) {
        return getAllFields(object.getClass());
    }


    /**
     * 获取所有的公开方法,包括父类
     */
    public static List<Method> getAllMethod(Object object){
       return Arrays.asList(object.getClass().getMethods());
    }


    /**
     * 比较所有记录字段
     */
    public static JSONArray recordField(Object newEntity, Object oldEntity) {
        JSONArray jsonArray = new JSONArray();
        Object object = null;
        List<Field> allFields = new ArrayList<>();
        try {
            if (oldEntity == null) {
                oldEntity = newEntity.getClass().newInstance();
            }
            object = oldEntity.getClass().newInstance();

            BeanUtil.copyProperties(newEntity, object);
            allFields = CommonUtils.getAllFields(oldEntity.getClass());
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        for (Field field : allFields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                try {
                    String oldStr = "";
                    Object o = field.get(oldEntity);
                    if (o != null) {
                        if (o instanceof Date) {
                            oldStr = DateUtils.formatDateTime((Date) o);
                        } else if (o instanceof BigDecimal) {
                            /*判断是不是BigDecimal类型 保留两位小数转字符串*/
                            oldStr = ((BigDecimal) o).setScale(2, RoundingMode.DOWN).toPlainString();
                        } else {
                            oldStr = o.toString();
                        }

                    }
                    Object o1 = field.get(object);
                    String newStr = "";
                    if (o1 != null) {
                        if (o1 instanceof Date) {
                            newStr = DateUtils.formatDateTime((Date) o1);
                        } else if (o1 instanceof BigDecimal) {
                            /*判断是不是BigDecimal类型 保留两位小数转字符串*/
                            newStr = ((BigDecimal) o1).setScale(2, RoundingMode.DOWN).toPlainString();
                        } else{
                            newStr = o1.toString();
                        }

                    }
                    if (!org.apache.commons.lang3.StringUtils.equals(newStr, oldStr)) {
                        JSONObject jsonObject = new JSONObject();
                        String name = annotation.value();
                        jsonObject.put("name", name);
                        jsonObject.put("oldStr", oldStr);
                        jsonObject.put("newStr", newStr);
                        jsonArray.add(jsonObject);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return jsonArray;
    }

    /**
     * 去除参数
     *
     * @param url
     * @return
     */
    public static String removeQuery(String url) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        int i = url.indexOf("?");
        if (i == -1) {
            return url;
        }
        return URLUtil.decode(url.substring(0, url.indexOf("?")));
    }

    public static void removeQueryList(List list, String... p) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (Object o : list) {
            removeQuery(o, p);
        }
    }

    public static void removeQuery(Object o, String... p) {
        if (ObjectUtil.isEmpty(o) || ObjectUtil.isEmpty(p)) {
            return;
        }
        for (String s : p) {
            String url = BeanUtil.getProperty(o, s);
            if (StrUtil.isBlank(url)) {
                continue;
            }

            BeanUtil.setProperty(o, s, removeQuery(url));
        }
    }

    public static void removeQuerySplit(Object o, String split, String p) {
        if (ObjectUtil.isEmpty(o) || ObjectUtil.isEmpty(p)) {
            return;
        }
        String url = BeanUtil.getProperty(o, p);
        if (StrUtil.isBlank(url)) {
            return;
        }
        String newUrl = StrUtil.split(url, StrUtil.COMMA).stream().map(CommonUtils::removeQuery).collect(Collectors.joining(","));
        BeanUtil.setProperty(o, p, newUrl);
    }

    /**
     * 判断是否图片
     *
     * @param fileName
     * @param throwException
     * @return
     */
    public static boolean isImage(String fileName, boolean throwException) {
        String s = FileUtil.extName(fileName).toLowerCase();
        if (ArrayUtil.contains(image_accept, s)) {
            return true;
        } else {
            if (throwException) {
                throw new OtherException("图片格式只支持:" + ArrayUtil.join(image_accept, ","));
            }
            return false;
        }
    }

    public static <T, U extends Comparable<U>> Comparator<T> nullFirstComparing(Function<? super T, ? extends U> keyExtractor) {
        return comparing(keyExtractor, false);
    }

    public static <T, U extends Comparable<U>> Comparator<T> nullLastComparing(Function<? super T, ? extends U> keyExtractor) {
        return comparing(keyExtractor, true);
    }

    public static <T, U extends Comparable<U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, boolean nullLast) {
        return Comparator.comparing(keyExtractor, nullLast ? Comparator.nullsLast(Comparable::compareTo): Comparator.nullsFirst(Comparable::compareTo));
    }

    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.toList());
    }

    public static <T, K, U> Collector<T, ?, Map<K, List<U>>> groupingBy(Function<? super T, ? extends K> classifier, Function<? super T, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> groupingSingleBy(Function<? super T, ? extends K> classifier, Function<? super List<T>, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), mapper::apply));
    }

    public static <T, K> Collector<T, ?, Map<K, T>> toMap(Function<? super T, ? extends K> classifier) {
        return toMap(classifier, Function.identity());
    }

    public static <T, V> Collector<T, ?, Map<T, V>> toKeyMap(Function<? super T, ? extends V> classifier) {
        return toMap(Function.identity(), classifier);
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> classifier, Function<? super T, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), value -> mapper.apply(value.get(0))));
    }

    public static <T, V> Map<V, List<T>> inverse(Map<T, V> map) {
        final Map<V, List<T>> result = MapUtil.createMap(map.getClass());
        map.forEach((key, value) -> {
            List<T> list = result.getOrDefault(value, new ArrayList<>());
            list.add(key);
            result.put(value, list);
        });
        return result;
    }

    public static <T, U extends Enum<U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
                (c1, c2) -> Integer.compare(keyExtractor.apply(c1).ordinal(), keyExtractor.apply(c2).ordinal());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    // 封装Hutool的StrJoiner
    public static <T> String saftyStrJoin(CharSequence delimiter, List<T> list, Function<T, Object> func) {
        if (CollUtil.isEmpty(list)) return "";
        return saftyStrJoin(delimiter, list.stream().map(func).map(Object::toString).toArray(Object[]::new)).toString();
    }

    public static <T> String strJoin(CharSequence delimiter, List<T> list, Function<T, Object> func) {
        if (CollUtil.isEmpty(list)) return "";
        return strJoin(delimiter, list.stream().map(func).map(StrUtil::utf8Str).toArray(Object[]::new)).toString();
    }

    public static StrJoiner strJoin(CharSequence delimiter, Object... str) {
        return strJoin(delimiter, StrJoiner.NullMode.IGNORE, str);
    }

    public static StrJoiner saftyStrJoin(CharSequence delimiter, Object... str) {
        return strJoin(delimiter).append(Arrays.stream(str).map(it -> ObjectUtil.isNotEmpty(it) ? it : " ").collect(Collectors.toList()));
    }

    public static StrJoiner strJoin(CharSequence delimiter, StrJoiner.NullMode nullMode, Object... str) {
        return StrJoiner.of(delimiter).setNullMode(nullMode).append(str);
    }

    public static StrJoiner appendPreAndSuffix(StrJoiner joiner, CharSequence prefix, CharSequence suffix) {
        return joiner.setPrefix(prefix).setSuffix(suffix);
    }

    public static <T> T listGet(Collection<T> collection, int index, T defaultValue) {
        try {
            return CollUtil.get(collection,index);
        }catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static boolean judge(Collection<Integer> judgeList, int index, int defaultValue) {
        return judge(judgeList, index, defaultValue, (judge)-> NumberUtil.equals(judge, (Number) 1) );
    }

    public static boolean judge(Collection<Integer> judgeList, int index, int defaultValue, Function<Integer, Boolean> handler) {
        Integer result = CollUtil.get(judgeList, index);
        return result == null ? handler.apply(defaultValue) : handler.apply(result);
    }

    public static <T> Comparator<? super T> sizeNameSort(Function<? super T, String> func) {
        return Comparator.comparing((it) -> {
            String sizeName = func.apply(it);
            int base = 1;
            Integer rate = null;
            for (char c : sizeName.toCharArray()) {
                if (c == 'X') {
                    base += 1;
                } else if (c == 'S') {
                    rate = -1;
                } else if (c == 'M') {
                    rate = 0;
                } else if (c == 'L') {
                    rate = 1;
                }
            }
            if (rate == null) return Integer.MIN_VALUE;
            return rate * base;
        });
    }

    public static Comparator<? super String> sizeNameSort() {
        return sizeNameSort(Function.identity());
    }

    public static <T> BigDecimal sumBigDecimal(Collection<T> list, Function<T, BigDecimal> func) {
        return BigDecimal.valueOf(list.stream().map(func).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP);
    }

    public static <T> List<T> listFlatten(List<T>... sourceList) {
        return Arrays.stream(sourceList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> List<T> listTreeFlatten(List<T> sourceList, Function<T, List<T>> childrenListFunc) {
        List<T> result = new ArrayList<>();
        if (CollUtil.isEmpty(sourceList)) return sourceList;
        for (T source : sourceList) {
            result.add(source);
            listTreeFlatten(result, source, childrenListFunc);
        }
        return result;
    }

    private static <T> void listTreeFlatten(List<T> newList, T source, Function<T, List<T>> childrenListFunc) {
        if (source == null) return;
        List<T> list = childrenListFunc.apply(source);
        if (CollUtil.isEmpty(list)) return;
        for (T t : list) {
            newList.add(t);
            listTreeFlatten(newList, t, childrenListFunc);
        }
    }

    public static <T> Supplier<? extends T> getListOne(List<T> list, T dto) {
        list.add(dto);
        return () -> dto;
    }

    public static <T> Collection<T> filterNotEmpty(List<T> list, Function<T, ?> func) {
        return CollUtil.filterNew(list, notEmptyFunc(func));
    }

    public static <T> Collector<T, ?, Map<Boolean, List<T>>> groupNotBlank(Function<T, ?> classifier) {
        return Collectors.groupingBy((it) -> notEmptyFunc(classifier).accept(it), LinkedHashMap::new, Collectors.toList());
    }

    public static <T> Filter<T> notEmptyFunc(Function<T, ?> func) {
        return (t) -> ObjectUtil.isNotEmpty(func.apply(t));
    }

    public static <T extends BaseEntity> List<String> getIds(List<T> list, Function<T, String> func) {
        return list.stream().map(func).collect(Collectors.toList());
    }

    public static <T> List<List<T>> flattenStructure(List<T> list, Function<T, List<T>> func) {
        List<List<T>> result = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        result.add(index.getAndIncrement(), list);
        do{
            list = list.stream().map(func).filter(CollUtil::isNotEmpty)
                    .flatMap(Collection::stream).collect(Collectors.toList());
            result.add(index.getAndIncrement(), list);
        } while (list.stream().anyMatch(it-> CollUtil.isNotEmpty(func.apply(it))));
        return result;
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(suffix)) {
            return StrUtil.str(str);
        }

        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            // 截取前半段
            str2 = removeSuffix(StrUtil.subPre(str2, str2.length() - suffix.length()), suffix);
        }
        return str2;
    }

}
