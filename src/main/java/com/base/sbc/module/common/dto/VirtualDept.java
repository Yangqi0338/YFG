/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.dto;

import lombok.Data;

@Data
public class VirtualDept {

	private String id;
    /** 部门名称 */
    private String name;
    /** 部门介绍 */
    private String tips;
    /** 父类ID */
    private String parentId;
    /** 父类ID集合 */
    private String parentIds;
    /** 状态正常(0),停用(1) */
    private String status;
    /** 第几级树 */
    private String level;
    /** 是否为末级节点(0表示是，1表示不是） */
    private String isLeaf;
    /** 是否为生产部门(0是，1不是) */
    private String isProduct;
    /** 是否初始值 */
    private String isInitial;
    /** 默认(0表示是企业默认部门)(1表示不是) */
    private String isDefault;
    /** smp部门id */
    private String smpDeptId;
    /** smp部门编码 */
    private String smpDeptCode;

}