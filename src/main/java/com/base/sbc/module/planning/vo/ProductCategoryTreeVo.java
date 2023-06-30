package com.base.sbc.module.planning.vo;

import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("产品季品类树Vo ProductCategoryTreeVo")
public class ProductCategoryTreeVo {

    private String id;
    /** 企划名称 */
    @ApiModelProperty(value = "企划名称"  )
    private String name;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    @ApiModelProperty(value = "子节点")
    private List<BasicStructureTreeVo> children;
}
