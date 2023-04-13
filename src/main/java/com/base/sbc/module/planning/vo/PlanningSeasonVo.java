package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品企划-产品季列表 PlanningSeasonSaveVo")
public class PlanningSeasonVo {
    @ApiModelProperty(value = "编号" ,example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "名称" ,example = "23年秋常规产品企划")
    private String name;
    @ApiModelProperty(value = "品牌" ,required = true,example = "0" )
    private String brand;
    @ApiModelProperty(value = "年份" ,example = "2023")
    private String year;
    @ApiModelProperty(value = "季节" ,example = "冬")
    private String season;
    @ApiModelProperty(value = "月份" ,example = "11月")
    private String month;
    @ApiModelProperty(value = "状态" ,example = "1")
    private String status;
    @ApiModelProperty(value = "删除标记(0正常，1删除)" ,example = "1")
    private String delFlag;
    @ApiModelProperty(value = "用户头像" ,example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;
    @ApiModelProperty(value = "创建者" ,example = "张三")
    private String createName;
    @ApiModelProperty(value = "创建者id" ,example = "111")
    private String createId;
    @ApiModelProperty(value = "备注" )
    private String remarks;

}
