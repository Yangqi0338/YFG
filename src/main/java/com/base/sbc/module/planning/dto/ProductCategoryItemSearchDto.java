package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：产品季总览-波段企划查询
 * @address com.base.sbc.module.planning.dto.ProductSeasonBandSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 14:52
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-坑位信息查询 ProductCategoryItemSearchDto")
public class ProductCategoryItemSearchDto extends Page {

    @ApiModelProperty(value = "产品季id" ,required = true,example = "122222")
    @NotBlank(message = "产品季id不能为空")
    private String planningSeasonId;
    @ApiModelProperty(value = "波段企划id" ,required = false,example = "122222")
    private String planningBandId;

    @ApiModelProperty(value = "品类ids" ,required = false,example = "['1234']")
    private List<String> categoryIds;

    @ApiModelProperty(value = "设计师ids" ,required = false,example = "['1233']")
    private List<String> designerIds;

    @ApiModelProperty(value = "任务等级" ,required = false,example = "['1']")
    private List<String> taskLevels;

    @ApiModelProperty(value = "状态" ,required = false,example = "['1']")
    private List<String> statusList;
}
