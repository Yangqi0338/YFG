package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackBom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 类描述：资料包-物料清单
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackBomVersionVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 16:45
 */
@Data
@ApiModel("资料包-物料清单 PackBomVo")
public class PackBomVo extends PackBom {

    @ApiModelProperty(value = "尺码信息")
    private List<PackBomSizeVo> packBomSizeList;

    @ApiModelProperty(value = "物料清单-配色列表")
    private List<PackBomColorVo> packBomColorVoList;

    /**
     * 物料数据来源：1.新增、2.面料企划、3.其他
     */
    @ApiModelProperty(value = "物料数据来源：1.新增、2.面料企划、3.其他")
    private String source;

    /**
     * 供应商简称
     */
    @ApiModelProperty(value = "供应商简称")
    private String supplierAbbreviation;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private BigDecimal stockQuantity;
}
