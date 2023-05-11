package com.base.sbc.module.sample.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣分页返回
 * @address com.base.sbc.module.sample.vo.SamplePageVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-10 11:54
 * @version 1.0
 */
@Data
@ApiModel("样衣分页返回 SamplePageVo ")
public class SamplePageVo {
    @ApiModelProperty(value = "编号"  )
    private String id ;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 月份 */
    @ApiModelProperty(value = "月份"  )
    private String month;
    /** 波段(编码) */
    @ApiModelProperty(value = "波段(编码)"  )
    private String bandCode;

    @ApiModelProperty(value = "设计师名称"  )
    private String designer;
    /** 设计师id */
    @ApiModelProperty(value = "设计师id"  )
    private String designerId;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 旧设计款号 */
    @ApiModelProperty(value = "旧设计款号"  )
    private String hisDesignNo;

    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板"  )
    private String status;

}
