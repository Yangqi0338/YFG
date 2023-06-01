/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.amc.entity;

import lombok.Data;

/**
 * 部门
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.amc.entity.Dept
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-01 15:55
 */
@Data
public class Dept {
    /**
     * 部门id
     */
    private String id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 用户类型
     */
    private String userType;

}

