package com.base.sbc.open.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/10 14:53:02
 * @mail 247967116@qq.com
 * 样品下发
 */
@Data
public class SmpOpenSampleDto extends SmpBaseDto {
    /**设计师*/
    private String proofingDesigner;
    /**设计师工号*/
    private String proofingDesignerId;
    /**外辅标志*/
    private Boolean BExtAuxiliary;
    /**样衣编号*/
    private String barcode;
    /**外辅发出时间*/
    private Date EAValidFromTime;
    /**外辅接收时间*/
    private Date EAValidToTime;
    /**样衣结束标志*/
    private Boolean finished;
    /**齐套时间*/
    private Date MCDate;
    /**打样URL*/
    private String pmlId;
    /**打版难度编号*/
    private String patDiff;
    /**打版难度名称*/
    private String patDiffName;

    /**打样顺序编号*/
    private String patSeq;
    /**打样顺序名称*/
    private String patSeqName;
    /**版号*/
    private String sampleNumber;
    /**版号名称*/
    private String sampleNumberName;
    /**大货款号*/
    private String colorwayCode;
    /**大货款号唯一标识*/
    private String colorwayPlmId;
    /**样衣名*/
    private String nodeName;
    /**设计收样日期*/
    private Date sampleReceivedDate;
    /**状态*/
    private String sampleStatus;
    /**状态名称*/
    private String sampleStatusName;
    /**样衣类型*/
    private String sampleType;
    /**样衣类型名称*/
    private String sampleTypeName;
    /**大类*/
    private String majorCategories;
    /**大类名称*/
    private String majorCategoriesName;
    /**品类*/
    private String category;
    /**品类名称*/
    private String categoryName;
    /**品牌code*/
    private String brandCode;
    /**品牌名称*/
    private String brandName;
    /**季节code*/
    private String quarterCode;
    /**年份*/
    private String year;
    /**设计师*/
    private String designer;
    /**设计师工号*/
    private String designerId;
    /**版师*/
    private String patternMaker;
    /**版师id*/
    private String patternMakerId;
    /**工艺员*/
    private String technician;
    /**工艺员id*/
    private String technicianId;
    /**中类id*/
    private String middleClassId;
    /**中类名称*/
    private String middleClassName;
    /**款式URL*/
    private String styleUrl;
    /**设计款号*/
    private String styleCode;
    /**供应商：打样部门*/
    private String supplier;
    /**打样部门编号*/
    private String supplierNumber;
    /**图片集合*/
    private List<String> imgList;
}
