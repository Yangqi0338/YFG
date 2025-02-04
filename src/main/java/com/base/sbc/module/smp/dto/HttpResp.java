package com.base.sbc.module.smp.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/7/9 17:21:45
 * @mail 247967116@qq.com
 */
@Data
public class HttpResp {
    private String statusCode;
    private String code;
    private boolean success;

    private String message;
    private String msg;
    private String userId;
    private String name;

    public String getMessage(){
        return Opt.ofBlankAble(message).orElse(msg);
    }

    private String url;
    private String data;
    private List<Map<String,Object>> getDataMap(){
        if (JSONUtil.isTypeJSON(data)) {
            return JSON.parseObject(data, new TypeReference<List<Map<String,Object>>>() {}.getType());
        }
        return new ArrayList<>();
    }
    public <T> List<T> getData(Class<T> clazz) {
        List<Map<String, Object>> dataMap = getDataMap();
        if (CollUtil.isEmpty(dataMap)) return new ArrayList<>();
        return dataMap.stream().map(it-> BeanUtil.mapToBean(it, clazz, true, CopyOptions.create())).collect(Collectors.toList());
    }
}
