package com.base.sbc.module.basicsdatum.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/27 10:45
 * @mail 247967116@qq.com
 * 规格门幅实体类
 */
@Data
@Api(tags = "基础资料-色号和色型")
@TableName("t_basicsdatum_specification")
public class Specification extends BaseDataEntity<String> {

    /**
     * 状态(0:启用1停止)
     */
    @ApiModelProperty(value = "状态(0:启用1停止)")
    private String status;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /*
    * 吊牌显示
    * */
    @ApiModelProperty(value = "吊牌显示")
    private String hangtags;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /*规格类型名称*/
    private String  typeName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

}
