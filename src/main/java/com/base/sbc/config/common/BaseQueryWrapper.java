package com.base.sbc.config.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

/**
 * @author 卞康
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
public class BaseQueryWrapper<T> extends QueryWrapper<T> {
    public void notEmptyEq(String column, Object val) {
        super.eq(!StringUtils.isEmpty(val), column, val);
    }

    public void notEmptyLike(String column, Object val) {
        super.like(!StringUtils.isEmpty(val), column, val);
    }

    public void dateBetween(String[] date){
        if (date!=null && date.length>0){
            super.ge(!StringUtils.isEmpty(date[0]),"create_date",date[0]);
            if (date.length>1){
                super.and(i->i.le(!StringUtils.isEmpty(date[1]),"create_date",date[1]));
            }
        }
    }
}
