/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.PackSampleReviewItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：资料包-样衣评审 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackSampleReview
 * @email your email
 * @date 创建时间：2023-7-13 20:34:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_sample_review")
@ApiModel("资料包-样衣评审 PackSampleReview")
public class PackSampleReview extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 打版类型
     */
    @ApiModelProperty(value = "打版类型")
    private String sampleType;
    /**
     * 制版单号
     */
    @ApiModelProperty(value = "制版单号")
    private String patternMakingCode;
    /**
     * 评估岗位
     */
    @ApiModelProperty(value = "评估岗位")
    private String post;
    /**
     * 评估结果
     */
    @ApiModelProperty(value = "评估结果")
    private String result;
    /**
     * 评估内容
     */
    @ApiModelProperty(value = "评估内容")
    private String content;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private PackSampleReviewItemType itemType;
    /**
     * 面料测试结果
     */
    @ApiModelProperty(value = "面料测试结果")
    private YesOrNoEnum fabricTestFlag;

    /**
     * 面料测试说明
     */
    @ApiModelProperty(value = "面料测试说明")
    private String fabricTestContent;

    /**
     * 面料测试文件id
     */
    @ApiModelProperty(value = "面料测试文件id")
    private String fabricTestFileId;

    /**
     * 面料测试文件地址
     */
    @ApiModelProperty(value = "面料测试文件地址")
    private String fabricTestFileUrl;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    public String getSourceFabricTestFileUrl() {
        return fabricTestFileUrl;
    }
}

