package com.base.sbc.open.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/18 15:22:32
 * @mail 247967116@qq.com
 */
@Data
public class SmpPostDto {
    /** 发送状态(1:新增, 2:修改) */
    private String type;
    /** 岗位状态(3:作废,其他正常) */
    private String objStatus;
    /** 中心编号 */
    private String centerId;
    /** 公司编号 */
    private String companyId;
    /** 岗位ID */
    private String positionId;
    /** 岗位名称 */
    private String name;
    /** 中心名称 */
    private String centerName;
}
