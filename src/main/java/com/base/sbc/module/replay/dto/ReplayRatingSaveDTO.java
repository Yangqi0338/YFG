/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.map.MapUtil;
import com.base.sbc.config.JacksonExtendHandler;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailDimensionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @JsonIgnore
    public Map<ReplayRatingDetailType, ReplayRatingDetailList> getDetailListMap() {
        if (MapUtil.isEmpty(detailListMap))
            detailListMap = Arrays.stream(ReplayRatingDetailType.values()).collect(CommonUtils.toKeyMap((it) -> new ReplayRatingDetailList(it, getType())));
        return detailListMap;
    }

    @Override
    public Map<Object, Object> decorateWebMap() {
        Map<Object, Object> map = super.decorateWebMap();
        map.putAll(getDetailListMap().keySet().stream().collect(Collectors.toMap(ReplayRatingDetailType::getCode, detailListMap::get)));
        return map;
    }

    @JsonAnySetter
    public void decorateWebDTO(String key, Object obj) {
        getDetailListMap().forEach((replayRatingDetailType, replayRatingDetailListDTO) -> {
            if (replayRatingDetailType.getCode().equals(key) && obj.getClass().isAssignableFrom(ReplayRatingDetailList.class)) {
                List<?> list = (List<?>) obj;
                ReplayRatingDetailList detailList = new ReplayRatingDetailList();
                list.forEach(it -> {
                    try {
                        ObjectMapper objectMapper = JacksonExtendHandler.getObjectMapper();
                        ReplayRatingDetailDTO bean = objectMapper.readValue(objectMapper.writeValueAsString(it), ReplayRatingDetailDTO.class);
                        bean.setType(replayRatingDetailType);
                        detailList.add(bean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                getDetailListMap().put(replayRatingDetailType, detailList);
            }
        });
    }

    public List<ReplayRatingDetailDimensionType> getImproveDimensionList() {
        return getDetailListMap().get(ReplayRatingDetailType.NEXT_IMPROVE).improveDimensionList();
    }

    @Override
    public YesOrNoEnum getReplayFlag() {
        return YesOrNoEnum.findByValue(isDetailInput(ReplayRatingDetailType.RATING) || isDetailInput(ReplayRatingDetailType.NEXT_IMPROVE));
    }

    @Override
    public YesOrNoEnum getRatingFlag() {
        return YesOrNoEnum.findByValue(isDetailInput(ReplayRatingDetailType.RATING));
    }

    private boolean isDetailInput(ReplayRatingDetailType detailType) {
        return getDetailListMap().getOrDefault(detailType, new ReplayRatingDetailList()).stream().anyMatch(ReplayRatingDetail::isInput);
    }


}
