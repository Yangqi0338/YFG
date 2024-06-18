/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailDimensionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
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
@ApiModel("基础资料-复盘评分 ReplayRatingSaveDTO")
public class ReplayRatingSaveDTO extends ReplayRating {

    /** 详情map */
    @ApiModelProperty(value = "详情map")
    private Map<ReplayRatingDetailType, ReplayRatingDetailList> detailListMap;

    @JsonAnyGetter
    public Map<ReplayRatingDetailType, ReplayRatingDetailList> getDetailListMap() {
        if (MapUtil.isEmpty(detailListMap))
            detailListMap = Arrays.stream(ReplayRatingDetailType.values()).collect(CommonUtils.toKeyMap(ReplayRatingDetailList::new));
        return detailListMap;
    }

    @JsonAnySetter
    public void setDetailListMap(String key, Object obj) {
        getDetailListMap().forEach((replayRatingDetailType, replayRatingDetailListDTO) -> {
            if (replayRatingDetailType.getCode().equals(key) && obj.getClass().isAssignableFrom(ReplayRatingDetailList.class)) {
                String jsonStr = JSONUtil.toJsonStr(obj);
                List<ReplayRatingDetailDTO> list = JSONUtil.toList(jsonStr, ReplayRatingDetailDTO.class);
                getDetailListMap().put(replayRatingDetailType, new ReplayRatingDetailList(list));
            } else {
                this.getExtend().put(key, obj);
            }
        });
    }

    public List<ReplayRatingDetailDimensionType> getImproveDimensionList() {
        return getDetailListMap().get(ReplayRatingDetailType.NEXT_IMPROVE).improveDimensionList();
    }

    public YesOrNoEnum getReplayFlag() {
        return YesOrNoEnum.findByValue(getDetailListMap().containsKey(ReplayRatingDetailType.RATING) || getDetailListMap().containsKey(ReplayRatingDetailType.NEXT_IMPROVE));
    }

    public YesOrNoEnum getRatingFlag() {
        return YesOrNoEnum.findByValue(getDetailListMap().containsKey(ReplayRatingDetailType.RATING));
    }


}
