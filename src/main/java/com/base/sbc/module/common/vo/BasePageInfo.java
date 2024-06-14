package com.base.sbc.module.common.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/11/30 21:08:47
 * @mail 247967116@qq.com
 */
@Data
public class BasePageInfo<T> extends PageInfo<T> {
    private Map<String,Object> map;

    public BasePageInfo(List<T> list) {
        super(list);
    }
}
