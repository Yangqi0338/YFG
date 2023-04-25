package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("商品企划-波段企划 PlanningSeasonSaveVo")
public class PlanningBandVo {
    @ApiModelProperty(value = "编号" ,example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "产品季编号" ,example = "689467740238381051")
    private String planningSeasonId;
    /** 波段名称 */
    @ApiModelProperty(value = "波段企划名称" ,example = "23年冬常规产品1B企划")
    private String name;
    /** 性别 */
    @ApiModelProperty(value = "性别" ,example = "男")
    private String sex;
    /** 波段 */

    @ApiModelProperty(value = "波段" ,example = "1b")
    private String bandCode;

    /** 生成模式 */
    @ApiModelProperty(value = "生产模式" ,example = "CMT")
    private String devtType;
    /** 渠道 */
    @ApiModelProperty(value = "渠道" ,example = "线下")
    private String channel;
    @ApiModelProperty(value = "用户头像" ,example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;
    @ApiModelProperty(value = "创建者" ,example = "张三")
    private String createName;
    @ApiModelProperty(value = "创建id" ,example = "12331423412412")
    private String createId;
    @ApiModelProperty(value = "品类信息" )
    private List<PlanningCategory> categoryData;
    @ApiModelProperty(value = "坑位信息" )
    private List<PlanningCategoryItemVo> categoryItemData;
    @ApiModelProperty(value = "草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String status;
    @ApiModelProperty(value = "skc数" )
    private Long skcCount;

}
