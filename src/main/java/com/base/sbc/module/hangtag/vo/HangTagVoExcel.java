package com.base.sbc.module.hangtag.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*吊牌导出*/
@Data
public class HangTagVoExcel {
    /**
     * 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认
     */
    @ApiModelProperty(value = "状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认")
    @Excel(name = "状态",replace={"未提交_1","未填写_0","未确认_2","待技术员确认_3","待品控确认_4","已确认_5","未填写_null"})
    private String status;

    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    @Excel(name = "配色")
    private String color;

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    @Excel(name = "大货款号")
    private String bulkStyleNo;

    /**
     * 款式类型名称
     */
    @ApiModelProperty(value = "款式类型名称")
    @Excel(name = "款式类型")
    private String  styleTypeName;

    @ApiModelProperty(value = "当前审核用户")
    @Excel(name = "当前审核人")
    private String  examineUserNema;

    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    @Excel(name = "设计款号")
    private String designNo;

    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    @Excel(name = "吊牌价")
    private BigDecimal tagPrice;

    /**
     * 是否计控成本确认
     */
    @ApiModelProperty(value = "是否计控成本确认")
    @Excel(name = "计控成本确认",replace={"已确认_1","未确认_0","未确认_null"})
    private String planCostConfirm;

    /**
     * 商品吊牌价是否确认
     */
    @ApiModelProperty(value = "商品吊牌价是否确认")
    @Excel(name = "商品吊牌价确认",replace={"已确认_1","未确认_0","未确认_null"})
    private String productTagPriceConfirm;

    /**
     * 计控吊牌价是否确认
     */
    @ApiModelProperty(value = "计控吊牌价是否确认")
    @Excel(name = "计控吊牌价确认",replace={"已确认_1","未确认_0","未确认_null"})
    private String planTagPriceConfirm;

    /**
     * 生产类型名称
     */
    @ApiModelProperty(value = "生产类型名称")
    @Excel(name = "生产类型")
    private String  produceTypeName;



    /**
     * 号型类型名称
     */
    @ApiModelProperty(value = "号型类型名称")
    @Excel(name = "号型类型")
    private String modelTypeName;

    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "品控确认时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDate;

    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    @Excel(name = "品名")
    private String productName;

    @ApiModelProperty(value = "品类")
    @Excel(name = "品类")
    private String prodCategoryName;
    /**
     * 执行标准
     */
    @ApiModelProperty(value = "执行标准")
    @Excel(name = "执行标准")
    private String executeStandard;

    /**
     * 安全标题
     */
    @ApiModelProperty(value = "安全标题")
    @Excel(name = "安全标题")
    private String saftyTitle;

    /**
     * 安全类别
     */
    @ApiModelProperty(value = "安全类别")
    @Excel(name = "安全类别")
    private String saftyType;

    /**
     * 质量等级
     */
    @ApiModelProperty(value = "质量等级")
    @Excel(name = "质量等级")
    private String qualityGrade;


    /**
     * 二检包装形式
     */
    @ApiModelProperty(value = "二检包装形式")
    @Excel(name = "二检包装形式")
    private String secondPackagingForm;

    /**
     * 成分信息
     */
    @ApiModelProperty(value = "成分信息")
    @Excel(name = "成分信息")
    public String ingredient;

    /**
     * 温馨提示
     */
    @ApiModelProperty(value = "温馨提示")
    @Excel(name = "温馨提示")
    private String warmTips;

    /**
     * 洗标
     */
    @ApiModelProperty(value = "洗标")
//    @Excel(name = "洗标",type = 2 ,width = 30 , height = 50)
    private String washingLabel;

    /**
     * 洗标
     */
    @ApiModelProperty(value = "洗标")
    @Excel(name = "洗标",type = 2 ,imageType = 2,width = 30)
    private byte[] washingLabel1;


    /**
     * 贮藏要求
     */
    @ApiModelProperty(value = "贮藏要求")
    @Excel(name = "贮藏要求")
    private String storageDemand;

    /**
     * 充绒量
     */
    @ApiModelProperty(value = "充绒量")
    @Excel(name = "充绒量")
    private String downContent;

    /**
     * 特殊规格
     */
    @ApiModelProperty(value = "特殊规格")
    @Excel(name = "特殊规格")
    private String specialSpec;

    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    @Excel(name = "bom状态",replace={"大货_1","样品_0"})
    private String bomStatus;

    /**
     * 洗唛材质备注
     */
    @ApiModelProperty(value = "洗唛材质备注")
    @Excel(name = "洗唛材质备注")
    private String washingMaterialRemarks;

    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    @Excel(name = "放码师名称")
    private String gradingName;


    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    @Excel(name = "工艺师名称")
    private String technologistName;

    /**
     * 样衣工名称
     */
    @ApiModelProperty(value = "样衣工名称")
    @Excel(name = "样衣工名称")
    private String sampleMakerName;

    /**
     * 下单员名称
     */
    @ApiModelProperty(value = "下单员名称")
    @Excel(name = "下单员名称")
    private String placeOrderStaffName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "下单时间")
    private Date placeOrderDate;


    /**
     * 是否是迁移历史数据 0否 1是
     */
    @ApiModelProperty(value = "是否新系统标记")
    @Excel(name = "是否新系统标记",replace={"是_0","否_1","是_null"})
    private String historicalData;

}
