/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.personnelskills.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：人员技能表 实体类
 * @address com.base.sbc.module.personnelSkills.entity.PersonnelSkills
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-10 14:36:43
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_personnel_skills")
@ApiModel("人员技能表 PersonnelSkills")
public class PersonnelSkills extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 用户id集合 */
    @ApiModelProperty(value = "用户id集合"  )
    private String userIds;
    /** 用户名称集合 */
    @ApiModelProperty(value = "用户名称集合"  )
    private String userNames;
    /** 用户账号集合 */
    @ApiModelProperty(value = "用户账号集合"  )
    private String userAccounts;
    /** 岗位id集合 */
    @ApiModelProperty(value = "岗位id集合"  )
    private String positionIds;
    /** 岗位名称集合 */
    @ApiModelProperty(value = "岗位名称集合"  )
    private String positionNames;
    /** 品类编码集合 */
    @ApiModelProperty(value = "品类编码集合"  )
    private String categoryCodes;
    /** 品类名称集合 */
    @ApiModelProperty(value = "品类名称集合"  )
    private String categoryNames;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unit;
    /** 单位内小时数 */
    @ApiModelProperty(value = "单位内小时数"  )
    private String hoursWithinUnit;
    /** 产量 */
    @ApiModelProperty(value = "产量"  )
    private String output;
    /** 状态 */
    @ApiModelProperty(value = "状态(0:启用，1停止)"  )
    private String status;

    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
