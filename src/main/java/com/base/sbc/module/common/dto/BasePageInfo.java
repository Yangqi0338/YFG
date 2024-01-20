package com.base.sbc.module.common.dto;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2024-01-16 9:51:09
 * @mail 247967116@qq.com
 */
@Data
public class BasePageInfo<T> extends PageInfo<T> {
    private Map<String,Object>  objectMap = new HashMap<>();

    /**
     * 包装Page对象
     *
     * @param list
     */
    public BasePageInfo(List<T> list) {
        super(list);
    }
}
