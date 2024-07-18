package com.base.sbc.module.pokaYoke.vo;

import com.base.sbc.module.pokaYoke.enums.PokBusinessTypeEnum;
import com.base.sbc.module.pokaYoke.enums.YokeExecuteLevelEnum;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/7/17 14:43
 **/
@Data
public class PokaYokeConfigVo {

    private String id;

    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    @NotEmpty(message = "品牌不能为空")
    private String brandName;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private PokBusinessTypeEnum type;
    /** 执行级别 */
    @ApiModelProperty(value = "执行级别"  )
    private YokeExecuteLevelEnum executeLevel;
    /** 提示语 */
    @ApiModelProperty(value = "提示语"  )
    @NotEmpty(message = "提示语不能为空")
    private String hintMsg;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String category1Code;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String category1Name;
    /** 中类 */
    @ApiModelProperty(value = "中类"  )
    private String category2Code;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String category2Name;
    /** 小类编码 */
    @ApiModelProperty(value = "小类编码"  )
    private String category3Code;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String category3Name;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 控制条件 */
    @ApiModelProperty(value = "控制条件"  )
    @NotEmpty(message = "控制条件不能为空")
    private String controlCondition;
}
