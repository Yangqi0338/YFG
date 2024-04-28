package com.base.sbc.module.smp.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.base.sbc.config.constant.SmpProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/7/9 17:21:45
 * @mail 247967116@qq.com
 */
@Accessors(chain = true)
@Data
public class HttpReq {
    private String userId;
    private String url;
    private String code;
    private String name;
    private String moduleName;
    private String functionName;
    private String data;

    public HttpReq(String url) {
        this.url = url;
    }
}
