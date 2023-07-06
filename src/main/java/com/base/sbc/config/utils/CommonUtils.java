package com.base.sbc.config.utils;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 卞康
 * @date 2023/3/31 15:46:55
 */
public class CommonUtils {
    /**
     * 取多个set集合相交的数据，集合可为null，不进行筛选，但是任何一个set集合的长度为0，则无任何相交对象
     * @param sets 可变集合数组
     * @param <T>  集合类型
     * @return 相交的set集合
     */
    @SafeVarargs
    public static <T> Set<T> findCommonElements(Set<T>... sets) {
        List<Set<T>> setList = new ArrayList<>();
        for (Set<T> set : sets) {
            if (set != null) {
                if (set.size() == 0) {
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

}
