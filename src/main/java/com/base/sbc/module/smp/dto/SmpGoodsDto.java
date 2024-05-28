package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.business.RFIDType;
import com.base.sbc.module.formtype.vo.GoodsDynamicFieldVo;
import com.base.sbc.module.smp.base.SmpBaseDto;
import com.base.sbc.module.smp.entity.SmpSize;
import com.base.sbc.module.style.entity.StyleSpecFabric;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/9 9:09:05
 * @mail 247967116@qq.com
 * goods-大货主数据
 */
@Data
public class SmpGoodsDto extends SmpBaseDto {
    /** 款式分类id */
    private String productTypeId;
    /** 款式分类/产品类型 */
    private String productType;
    /**品牌ID*/
    private String brandId;
    /** 品牌名称 */
    private String brandName;
    /** 年份 */
    private String year;
    /** 季节 */
    private String season;
    /** 主题 */
    private String theme;
    /** 单位 */
    private String unit;

    /**大类名称*/
    private String maxClassName;
    /** 款式大类 */
    private String styleBigClass;
    /**品类名称*/
    private String categoryName;
    /**中类名称*/
    private String middleClassName;
    /** 款式品类 */
    private String styleCategory;
    /** 款式中类 */
    private String styleMiddleClass;
    /**小类名称*/
    private String minClassName;
    /** 款式小类 */
    private String styleSmallClass;

    /** 设计款号 */
    private String designNumber;
    /** 款式名称 */
    private String styleName;

    /** 设计师id */
    private String designerId;
    /** 下稿设计师 */
    private String designer;
    /** 工艺员 */
    private String technician;
    /** 工艺员id */
    private String technicianId;
    /** 版师名称 */
    private String patternMakerName;
    /** 版师ID */
    private String patternMakerId;



    /** 目标成本 */
    private BigDecimal targetCost;
    /** 企划倍率 */
    private BigDecimal planningRate;


    /** 是否主推 */
    private Boolean mainPush;
    /** 生产类型 */
    private String productionType;
    /** 生产类型名称 */
    private String productionTypeName;
    /** 区域 */
    private String region;
    /** 销售分组 */
    private String salesGroup;
    /** 设计稿分数 */
    private Integer designScore;
    /** 尺码组id */
    private String sizeGroupId;
    /** 尺码组名称 */
    private String sizeGroupName;
    /**设计款号*/
    private String styleCode;

    /**衣长分类id*/
    private String lengthRangeId;
    /**衣长分类名称*/
    private String lengthRangeName;
    /**衣长*/
    private String coatLength;
    /**腰型id*/
    private String waistTypeId;
    /**腰型名称*/
    private String waistTypeName;
    /**袖长id*/
    private String sleeveLengthId;
    /**袖长名称*/
    private String sleeveLengthName;
    /**袖型id*/
    private String sleeveId;
    /**袖长名称*/
    private String sleeveName;
    /**胸围*/
    private String bust;
    /**门襟id*/
    private String placketId;
    /**门襟名称*/
    private String placketName;
    /**毛纱针型id*/
    private String yarnNeedleTypeId;
    /**毛纱针型名称*/
    private String yarnNeedleTypeName;
    /**毛纱针法id*/
    private String yarnNeedleId;
    /**毛纱针法名称*/
    private String yarnNeedleName;
    /**廓形id*/
    private String profileId;
    /**廓形名称*/
    private String profileName;
    /**花型id*/
    private String flowerId;
    /**花型名称*/
    private String flowerName;
    /**版型名称*/
    private String shapeName;
    /**材质id*/
    private String textureId;
    /**材质名称*/
    private String textureName;
    /**模式名称*/
    private String patternName;
    /**紧急状态id*/
    private String priorityId;
    /**紧急状态名称*/
    private String priorityName;
    /**颜色编码(色号）*/
    private String colorCode;
    /**颜色名称*/
    private String colorName;
    /**波段id*/
    private String bandId;
    /**波段名称*/
    private String bandName;
    /**吊牌价*/
    private BigDecimal price;
    /**商品吊牌价确认*/
    private Boolean priceConfirm;
    /**成本*/
    private BigDecimal cost;
    /**设计bom总成本*/
    private BigDecimal designPackCost;
    /**外协加工费*/
    private BigDecimal outsourcingProcessingCost;
    /**包装费*/
    private BigDecimal packagingCost;
    /**检测费*/
    private BigDecimal testCost;
    /**毛衫加工费*/
    private BigDecimal sweaterProcessingCost;
    /**计控实际成本*/
    private BigDecimal planCost;
    /**企划实际倍率*/
    private BigDecimal actualRate;
    /**计控实际倍率*/
    private BigDecimal planActualRate;
    /**加工标准价*/
    private BigDecimal processCost;
    /**车缝加工费*/
    private BigDecimal laborCosts;
    /**材料成本*/
    private BigDecimal materialCost;
    /**品名*/
    private String productName;
    /**唯一码*/
    private String uniqueCode;
    /**系列*/
    private String series;
    /**是不是配饰*/
    private Boolean accessories;
    /**厂家*/
    private String manufacture;
    /**厂家编码*/
    private String manufactureCode;
    /**上新时间*/
    private Date saleTime;
    /**系列id*/
    private String seriesId;
    /**系列名称*/
    private String seriesName;
    /**轻奢*/
    private Boolean luxury;
    /**外辅工艺标识*/
    private Boolean auProcess;
    /**厂家款号*/
    private String supplierArticle;
    /**厂家款色号*/
    private String supplierArticleColor;
    /**包装形式*/
    private String packageType;
    /**包装袋标准*/
    private String packageSize;
    /**产品细分*/
    private String prodSeg;
    /**销售类型*/
    private String saleType;
    /**大货款号*/
    private String bulkNumber;
    /**成份信息*/
    private String composition;
    /**主款号*/
    private String mainCode;
    /**配饰款号*/
    private String secCode;
    /** 领型id */
    private String lingXingId;
    /** 领型名称 */
    private String lingXingName;
    /**BOM阶段*/
    private String bomPhase;
    /**开发BOM完整标志*/
    private Boolean integritySample;
    /**大货BOM完整标志*/
    private Boolean integrityProduct;
    /**尺码集合*/
    private List<SmpSize> itemList;
    /**图片地址集合*/
    private List<String> imgList;
    /** rfid标识 0 否 1 是 */
    private String rfidFlag;
    /** rfid属性
     * all("洗唛&吊牌RFID"),
     * washing("洗唛RFID"),
     * hangTag("吊牌RFID"),
     * */
    private RFIDType rfidType;

    /**
     * 二检包装形式编码
     */
    private String secondPackagingFormCode;
    /**
     * 二检包装形式
     */
    private String secondPackagingForm;


    //新加字段
    /**
     * 是否撞色
     */
    @ApiModelProperty(value = "是否撞色,0否 1是")
    private String colorCrash;
    /** 设计下正确样 */
    @ApiModelProperty(value = "设计下正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designCorrectDate;
    /** 设计下明细单 */
    @ApiModelProperty(value = "设计下明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailDate;
    /**
     * 工艺部接收正确样时间
     */
    @ApiModelProperty(value = "工艺部接收正确样时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveDate;

    /**
     * 技术下工艺部正确样
     */
    @ApiModelProperty(value = "技术下工艺部正确样")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processDepartmentDate;

    /**
     * 货品停用标识
     */
    private String goodsFlag;

    /**
     * 颜色停用标识
     */
    private String goodsColorFlag;

    /**
     * 指定面料集合
     */
    private List<StyleSpecFabric> styleSpecFabricList;
    /**
     * 款式设计动态管理字段 特定字段固定下发给BCS
     */
    List<GoodsDynamicFieldVo> goodsDynamicFieldVos;

    /**
     * 款式设计动态管理字段
     */
    private List<GoodsDynamicFieldVo> goodsDynamicFieldVoList;

    /**
     * 自主研发版型
     */
    @ApiModelProperty(value = "自主研发版型")
    private String plateType;

    /**
     * 成分水洗
     */
    @ApiModelProperty(value = "成分水洗")
    private String garmentWash;

    /**
     * 外部颜色code
     */
    @ApiModelProperty(value = "外部颜色code")
    private String outsideColorCode;
    /**
     * 外部颜色名称
     */
    @ApiModelProperty(value = "外部颜色名称")
    private String outsideColorName;
}
