/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.dto;

import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailSaveDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：工作量评分数据计算结果QueryDto 实体类
 * @address com.base.sbc.module.workload.dto.WorkloadRatingDetailQueryDto
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作量评分数据计算结果 WorkloadRatingDetailDTO")
public class WorkloadRatingDetailDTO extends WorkloadRatingDetail {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    @NotEmpty
    private List<WorkloadRatingDetailSaveDTO> configList = new ArrayList<>();

    private String proxyKey;
    private String secondProcessing;
    @ApiModelProperty(value = "面料成分")
    private String ingredient;

    @Override
    @JsonIgnore
    public String getItemValue() {
        return CommonUtils.strJoin("-", configList, WorkloadRatingDetailSaveDTO::getValue);
    }

    @Override
    public String getItemId() {
        return CommonUtils.strJoin(COMMA, configList, WorkloadRatingDetailSaveDTO::getItemId);
    }

    @Override
    public Map<Object, Object> decorateWebMap(){
        Map<Object, Object> map = super.decorateWebMap();
        map.putAll(this.getExtend());
        return map;
    }

    @Override
    public void build() {
        buildCalculateTypeResult(this.getExtend(), WorkloadRatingCalculateType::getCode);
        super.build();
    }

    private <T> void buildCalculateTypeResult(Map<T, Object> map, Function<WorkloadRatingCalculateType, T> func) {
        configList.stream().collect(Collectors.groupingBy(WorkloadRatingDetailSaveDTO::getCalculateType)).forEach((calculateType, sameTypeList) -> {
            T key = func.apply(calculateType);
            if (!map.containsKey(key)) {
                BigDecimal score = CommonUtils.sumBigDecimal(sameTypeList, WorkloadRatingDetailSaveDTO::getScore);
                map.put(key, score);
            }
        });
    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
