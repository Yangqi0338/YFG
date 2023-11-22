package com.base.sbc.module.smp.dto;

import lombok.Data;

import java.util.List;

/**
 * 尺寸表和外辅工艺明细数据
 * @author hq
 */
@Data
public class BomSizeAndProcessDto {

    private List<BomSize> bomSizeList;

    private List<BomProcess> bomBomProcessList;

    /**
     * 外辅工艺数据
     */
    @Data
    public static class BomProcess {

        private String id;
        /**工艺项目*/
        private String item;
        /**工艺项目code*/
        private String itemCode;
        /**描述 */
        private String content;
        /**排序-*/
        private int sort;
    }

    /**
     * bom尺寸数据
     */
    @Data
    public static class BomSize {
        private String id;
        /** 部位编码 */
        private String partCode;
        /** 部位名称 */
        private String partName;
        /**量法*/
        private String method;
        /**标准值*/
        private String standard;
        /**标准值*/
        private String size;
        /**正公差+*/
        private String positive;
        /**负公差-*/
        private String minus;
        /**排序-*/
        private int sort;
    }
}
