package com.base.sbc.module.material.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：素材库 实体类
 * @author 卞康
 * @date 2023/3/29 16:15:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material")
public class Material extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 来源人 */
    private String sourcePerson;
    /** 采集时间 */
    private Date collectionTime;
    /** 来源部门id */
    private String sourceDeptId;
    /** 来源部门 */
    private String sourceDeptName;
    /**
     * 收藏id
     */
    @TableField(exist = false)
    public String collectId;

    /**
     * 所有标签
     */
    @TableField(exist = false)
    public List<MaterialLabel> labels;

    /**
     * 所有尺码
     */
    @TableField(exist = false)
    private List<MaterialSize> sizes;

    /**
     * 所有颜色
     */
    @TableField(exist = false)
    private List<MaterialColor> colors;

    /**
     * ids 查询的id集合
     */
    @TableField(exist = false)
    public List<String> ids;

    /** 图片地址 */
    private String pictureUrl;
    /** 图片格式 */
    private String pictureFormat;
    /** 图片大小 */
    private String pictureSize;
    /** 素材文件名 */
    private String materialName;
    /** 素材编码 */
    private String materialCode;
    /** 所属素材库id */
    private String materialLibrary;
    /** 所属素材库名称 */
    private String libraryName;
    /** 审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过） */
    private String status;
    /** 所属分类id */
    private String materialType;
    /** 所属分类名称 */
    private String typeName;
    /** 年份 */
    private String particularYear;
    /** 月份 */
    private String month;
    /** 来源地 */
    private String source;
    /** 季节 */
    private String season;
    /** 品牌 */
    private String brand;
    /** 品牌规模 */
    private String brandScale;
    /** 缺点 */
    private String drawback;
    /** 风险评估 */
    private String assess;
    /** 备注 */
    private String remarks;
}

