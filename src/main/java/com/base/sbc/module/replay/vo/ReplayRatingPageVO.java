/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 类描述：基础资料-复盘评分Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("基础资料-分页复盘评分 ReplayRatingPageVO")
public class ReplayRatingPageVO<T extends ReplayRatingVO> extends PageInfo<T> {

    private Map<String, String> totalMap;

    @JsonAnyGetter
    public Map<String, String> getTotalMap() {
        return totalMap;
    }

    public void setTotalMap(Map<String, Object> totalMap) {
        this.totalMap = MapUtil.map(MapUtil.removeNullValue(totalMap), (key, value) -> value.toString());
    }

}
