package com.base.sbc.client.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/9 10:37:13
 * @mail 247967116@qq.com
 */
@Data
public class  SmpMaterialDto {
    /** 材料编号 */
    private String materialNumber;
    /** 材料名称名称 */
    private String materialName;
    /** 材料单位（采购单位） */
    private String materialUnit;
    /** 库存单位（新增） */
    private String stockUnit;
    /** 三级分类 */
    private String thirdLevelCategory;
    /** 物料来源 */
    private String materialSource;
    /** 二级分类 */
    private String secondLevelCategory;
    /** 开发年份 */
    private int developmentYear;
    /** 开发季节 */
    private String developmentSeason;
    /** 开发季节ID */
    private int developmentSeasonId;
    /** 开发品牌 */
    private String developmentBrand;
    /** 开发品牌ID */
    private int developmentBrandId;
}
