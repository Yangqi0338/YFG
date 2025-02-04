/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.workload.dto.WorkloadRatingTitleFieldDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：工作量评分配置Vo 实体类
 * @address com.base.sbc.module.workload.vo.WorkloadRatingItemVo
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作量评分配置 WorkloadRatingItemVO")
public class WorkloadRatingItemVO extends WorkloadRatingItem {
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    @JsonIgnore
    private YesOrNoEnum enableFlag;

    /**
     * 拥有的itemValue
     */
    @ApiModelProperty(value = "拥有的itemValue")
    @JsonIgnore
    private List<WorkloadRatingItemVO> itemList;

    /**
     * 拥有的itemValue
     */
    @ApiModelProperty(value = "拥有的itemValue")
    @JsonIgnore
    private List<WorkloadRatingTitleFieldDTO> titleFieldList;

    @Override
    public Map<Object, Object> decorateWebMap() {
        try {
            Map<Object, Object> map = super.decorateWebMap();
            if (CollUtil.isNotEmpty(titleFieldList)) {
                titleFieldList.stream().filter(it -> !this.getConfigId().equals(it.getConfigId())).forEach(titleFieldDTO -> {
                    Optional<WorkloadRatingItemVO> configItemOpt = itemList.stream().filter(it -> it.getConfigId().equals(titleFieldDTO.getConfigId())).findFirst();
                    map.put(titleFieldDTO.getCode(), configItemOpt.isPresent() ? configItemOpt.get().getScore() : getItemNameByIndex(titleFieldDTO.getIndex()));
                });
            }
            return map;
        }catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    private String getItemNameByIndex(Integer level) {
        String itemName = this.getItemName();
        if (StrUtil.isBlank(itemName) && this.getItemType() != WorkloadRatingItemType.STRUCTURE) return "";
        return ArrayUtil.get(itemName.split("/"), level);
    }

}
