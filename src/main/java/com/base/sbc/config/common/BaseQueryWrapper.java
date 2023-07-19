package com.base.sbc.config.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author 卞康
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
public class BaseQueryWrapper<T> extends QueryWrapper<T> {
    public QueryWrapper<T> notEmptyEq(String column, Object val) {
        return this.eq(!StringUtils.isEmpty(val), column, val);
    }


    public QueryWrapper<T> notEmptyIn(String column, Collection<?> coll) {
        return this.in(CollUtil.isNotEmpty(coll), column, coll);
    }

    public QueryWrapper<T> notEmptyIn(String column, String str) {
        return this.in(StrUtil.isNotBlank(str), column, StrUtil.split(str, CharUtil.COMMA));
    }

    public QueryWrapper<T> notEmptyLike(String column, Object val) {
        return this.like(!StringUtils.isEmpty(val), column, val);
    }

    public QueryWrapper<T> between(String column, String[] strings) {
        if (strings != null && strings.length > 0) {
            this.ge(!StringUtils.isEmpty(strings[0]), column, strings[0]);
            if (strings.length > 1) {
                return this.and(i -> i.le(!StringUtils.isEmpty(strings[1]), column, strings[1]));
            }
        }
        return this;
    }

    public QueryWrapper<T> andLike(String value, String... columns) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < columns.length; j++) {
                    i.like(columns[j], value).or(j < columns.length - 1);
                }
            });
        });
        return this;
    }
}
