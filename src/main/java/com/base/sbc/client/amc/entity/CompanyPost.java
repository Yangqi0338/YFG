package com.base.sbc.client.amc.entity;

import lombok.Data;

/**
 * 类描述： 岗位
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.amc.entity.CompanyPost
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-01 15:57
 */
@Data
public class CompanyPost {

    /**
     * 部门id
     */
    private String id;

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
}
