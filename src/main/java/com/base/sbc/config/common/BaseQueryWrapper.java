package com.base.sbc.config.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

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
        if (coll == null) {
            return this;
        }
        return this.in(CollUtil.isNotEmpty(coll), column, coll);
    }

    public QueryWrapper<T> notEmptyIn(String column, String str) {
        if (StrUtil.isBlank(str)) {
            return this;
        }
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

    public QueryWrapper<T> notNull(String column) {
        this.isNotNull(column);
        this.ne(column, "");
        return this;
    }

    public QueryWrapper<T> isNull(String column) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, "")));
        return this;
    }

    /**
     * findInSet
     * 转为
     * and ( FIND_IN_SET('1',column) or FIND_IN_SET('2',column) )
     *
     * @param column 字典
     * @param value  1,2
     * @return
     */
    public QueryWrapper<T> findInSet(String column, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        this.and(wrapper -> {

            wrapper.and(i -> {
                for (int k = 0; k < split.size(); k++) {
                    i.apply("FIND_IN_SET({0}," + column + ")", split.get(k)).or(k < split.size() - 1);
                }
            });
        });
        return this;
    }
}
