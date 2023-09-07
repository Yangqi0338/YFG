package com.base.sbc.module.pack.dto;


import com.base.sbc.module.pack.vo.PackBomVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：物料清单打印明细单
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.BomPrintVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-07 09:22
 */

@Data
@ApiModel("资料包-物料清单打印明细单Vo BomPrintVo")
public class BomPrintVo {
    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "品名")
    private String productName;

    @ApiModelProperty(value = "下单日期")
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private Date placeOrderDate;

    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "款号")
    private String styleNo;


    @ApiModelProperty(value = "是否主推(0否,1:是)")
    private String isMainly;

    @ApiModelProperty(value = "设计编号")
    private String designNo;
    @ApiModelProperty(value = "特殊工艺备注")
    private String specialProcess;

    @ApiModelProperty(value = "洗唛材质备注")
    private String washingMaterialRemarks;
    @ApiModelProperty(value = "款图")
    private String stylePic;
    @ApiModelProperty(value = "特别注意")
    private String particularAttention;

    @ApiModelProperty(value = "设计师")
    private String designer;
    @ApiModelProperty(value = "设计组长")
    private String designTeamLeader;
    @ApiModelProperty(value = "设计总监")
    private String designDirector;
    @ApiModelProperty(value = "设计经理")
    private String designManager;
    @ApiModelProperty(value = "粘衬信息")
    private String adhesiveLiningInfo;

    @ApiModelProperty(value = "注意事项")
    private String mattersAttention;

    @ApiModelProperty(value = "物料清单")
    private List<PackBomVo> bomList;


}
