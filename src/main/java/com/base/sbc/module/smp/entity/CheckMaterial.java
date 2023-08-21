package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/21 11:49:24
 * @mail 247967116@qq.com
 */
@Data
public class CheckMaterial {
    /**
     * 物料编号
     */
    private String materialCode;

    List<CheckSku> checkSkuList;

    @Data
    public static class CheckSku {
        private String sizeCode;
        private String colorCode;
    }

}
