/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.module.replay.entity.ReplayConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-复盘管理 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplayConfigDTO extends ReplayConfig {

    public ReplayConfigDTO(BasicBaseDict baseDict) {
        super();
        this.setBrand(baseDict.getValue());
        this.setBrandName(baseDict.getName());
    }

    /**
     * 将多条数据用换行符拼接, 适应 BaseDataExtendEntity 的前端显示
     */
    @Override
    public void decorateMapByDetail(Map<Object, Object> map, String key, List<?> objects) {
        map.put(key, objects.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }
}
