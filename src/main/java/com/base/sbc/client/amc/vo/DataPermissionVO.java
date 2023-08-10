package com.base.sbc.client.amc.vo;

import lombok.Data;

import java.util.List;

@Data
public class DataPermissionVO {
    /**
     * 字段查询类型：and.且、or.或
     */
    private String selectType;
    /**
     * 范围:1.全部可读或写，2.自定义，3.全部不可读或写
     */
    private String range;

    /**
     * 字段数据权限
     */
    private List<FieldDataPermissionVO> fieldDataPermissions;
}
