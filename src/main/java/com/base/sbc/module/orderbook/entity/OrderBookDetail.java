/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.orderbook.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.lang.Opt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.PushRespStatus;
import com.base.sbc.config.enums.business.PutInProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDepartmentEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailOrderStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：订货本 实体类
 *
 * @version 1.0
 * @date 创建时间：2023-9-20 14:48:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_book_detail")
@ApiModel("订货本列表")
public class OrderBookDetail extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 套装款号
     */
    @ApiModelProperty(value = "套装款号")
    private String suitNo;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private String serialNumber;
    /**
     * 订货本id
     */
    @ApiModelProperty(value = "订货本id")
    private String orderBookId;
    /**
     * 总投产
     */
    @ApiModelProperty(value = "总投产")
    private String totalProduction;
    /**
     * 倍率
     */
    @ApiModelProperty(value = "倍率")
    private BigDecimal rate;
    /**
     * 投产日期
     */
    @ApiModelProperty(value = "投产日期")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date commissioningDate;


    /**
     *维度信息（尺寸）
     */
    @ApiModelProperty(value = "维度信息（尺寸）")
    private String dimensionInfo;

    /**
     * 克重
     */
    @ApiModelProperty(value = "克重")
    private String gramWeight;

    /**
     * 面料吊牌
     */
    @Excel(name = "面料吊牌")
    private String fabricDrop;


    /**
     * 款式配色id
     */
    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态:0：未提交，1：分配设计师，2：分配商企，3：待审核,4:审核通过,5:审核未通过")
    private OrderBookDetailStatusEnum status;
    /**
     * 设计师分配
     */
    @ApiModelProperty(value = "状态:0：未分配，1：已分配")
    private YesOrNoEnum designerDistribute;
    /**
     * 线下商企分配
     */
    @ApiModelProperty(value = "状态:0：未分配，1：已分配")
    private YesOrNoEnum offlineBusinessDistribute;
    /**
     * 线上商企分配
     */
    @ApiModelProperty(value = "状态:0：未分配，1：已分配")
    private YesOrNoEnum onlineBusinessDistribute;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态:0：未审批，1：待审批，2：已审批")
    private OrderBookDetailAuditStatusEnum auditStatus;
    /**
     * 系数编码
     */
    @ApiModelProperty(value = "系数编码")
    private String coefficientCode;
    /**
     * 系数名称
     */
    @ApiModelProperty(value = "系数名称")
    private String coefficientName;
    /**
     * 4倍价
     */
    @ApiModelProperty(value = "4倍价")
    private String multiplePrice;
    /**
     * 目标入仓日期
     */
    @ApiModelProperty(value = "目标入仓日期")
    private String targetTime;
    /**
     * 生产紧急程名称
     */
    @ApiModelProperty(value = "生产紧急程度名称")
    private String productionUrgencyName;
    /**
     * 生产紧急程度
     */
    @ApiModelProperty(value = "生产紧急程度编码")
    private String productionUrgencyCode;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 其他
     */
    @ApiModelProperty(value = "其他")
    private String other;



    /**
     * 单件用量/里 关联bomid
     */
    @ApiModelProperty(value = "单件用量/里 关联bomid")
    private String unitDosageIds;

    /**
     * 面料状态
     */
    @ApiModelProperty(value = "cmt面料状态")
    protected String fabricState;
    /**
     * 是否延续
     */
    @ApiModelProperty(value = "是否延续")
    private String continuationPoint;
    /**
     * 面料厂家编码
     */
    @ApiModelProperty(value = "cmt面料厂家编码")
    private String fabricFactoryCode;
    /**
     * 面料厂家名称
     */
    @ApiModelProperty(value = "cmt面料厂家名称")
    private String fabricFactoryName;
    /**
     * 面料厂家色号
     */
    @ApiModelProperty(value = "cmt面料厂家色号")
    private String fabricFactoryColorNumber;
    /**
     * 面料编码
     */
    @ApiModelProperty(value = "cmt面料编码")
    private String fabricCode;
    /**
     * 面料成分
     */
    @ApiModelProperty(value = "cmt面料成分")
    private String fabricComposition;

    /**
     * 含税单价/米
     */
    @ApiModelProperty(value = "含税单价/米")
    private String unitPrice;
    /**
     * 货期
     */
    @ApiModelProperty(value = "货期")
    private String deliveryTime;
    /**
     * 库存面料米数
     */
    @ApiModelProperty(value = "库存面料米数")
    private String inventoryFabricMeter;
    /**
     * 库存可做件数
     */
    @ApiModelProperty(value = "库存可做件数")
    private String inventoryDoableNumber;

    /**
     * 面料备注
     */
    @ApiModelProperty(value = "面料备注")
    private String fabricRemarks;


    /**
     * 设计师编码
     */
    @ApiModelProperty(value = "设计师编码")
    private String designerCode;
    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designerName;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;

    /**
     * 颜色波段
     */
    @ApiModelProperty(value = "颜色波段")
    private String colorBand;
    /**
     * 商企编码
     */
    @ApiModelProperty(value = "商企编码")
    private String businessCode;
    /**
     * 商企名称
     */
    @ApiModelProperty(value = "商企名称")
    private String businessName;
    /**
     * 商企id
     */
    @ApiModelProperty(value = "商企id")
    private String businessId;



    /**
     * 线下投产
     */
    @ApiModelProperty(value = "线下投产")
    private String offlineProduction;

    /**
     * 线上投产
     */
    @ApiModelProperty(value = "线上投产")
    private String onlineProduction;

    /**
     * 备料
     */
    @ApiModelProperty(value = "备料")
    private String material;

    /**
     * 备胚
     */
    @ApiModelProperty(value = "备胚")
    private String braiding;

    /**
     * 备料
     */
    @ApiModelProperty(value = "线上备料")
    private String onlineMaterial;

    /**
     * 备胚
     */
    @ApiModelProperty(value = "线上备胚")
    private String onlineBraiding;

    /**
     * 总投产尺码
     */
    @ApiModelProperty(value = "总投产尺码")
    private String totalCommissioningSize;

    /**
     * 投产尺码
     */
    @ApiModelProperty(value = "投产尺码")
        private String commissioningSize;

    /**
     * 原款号
     */
    @ApiModelProperty(value = "原款号")
    private String originalNo;

    /**
     * 面料编号
     */
    @ApiModelProperty(value = "面料编号")
    private String companyFabricNumber;

    /**
     * 单件面料用量/米 关联bom
     */
    @ApiModelProperty(value = "单件面料用量/米  关联bom" )
    private String unitFabricDosageIds;

    /**
     * 公司面料备注
     */
    @ApiModelProperty(value = "公司面料备注")
    private String companyFabricRemarks;

    /**
     * 面料类型
     */
    @ApiModelProperty(value = "fabricType")
    private String fabricType;

    /**
     * 下单状态
     */
    @ApiModelProperty(value = "下单状态 0未下单 1投产失败 2下单中 3投产中 4已下单")
    private OrderBookDetailOrderStatusEnum orderStatus;
    /**
     * 是否锁定（0：否，1：是）
     */
    @ApiModelProperty(value = "是否锁定（0：否，1：是）")
    private YesOrNoEnum isLock = YesOrNoEnum.NO;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "brand")
    private String brand;


    /**
     * 部门
     * {@link OrderBookDepartmentEnum}
     */
    @ApiModelProperty(value = "部门：design 设计部，offline 线下商企，online 线上商企"  )
    private String department;

    /**
     * 线下投产尺码
     */
    @ApiModelProperty(value = "投产尺码")
    private String offlineCommissioningSize;

    /**
     * 线上投产尺码
     */
    @ApiModelProperty(value = "投产尺码")
    private String onlineCommissioningSize;

    @ApiModelProperty(value = "版本号"  )
    @Version
    private Integer version;

    @ApiModelProperty(value = "线上版本号")
    private Integer onlineVersion;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String orderPerson;

    /**
     * 下单人名
     */
    @ApiModelProperty(value = "下单人名")
    private String orderPersonName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date orderDate;

    /**
     * 投产编码
     */
    @ApiModelProperty(value = "投产编码")
    private String orderNo;

    /**
     * 投产下发状态
     */
    @ApiModelProperty(value = "投产下发状态")
    private PushRespStatus orderSendStatus;

    /**
     * 投产下发错误信息
     */
    @ApiModelProperty(value = "投产下发错误信息")
    private String orderSendWarnMsg;

    /** 商品要求货期 */
    @ApiModelProperty(value = "商品要求货期")
    private Date deliveryAt;

    /** 销售分类 */
    @ApiModelProperty(value = "销售分类")
    private String saleTypeId;

    /** 预算号 */
    @ApiModelProperty(value = "预算号")
    private String budgetNo;

    /** 预算号 */
    @ApiModelProperty(value = "预算号")
    private String budgetNoId;

    /** 合并投产 */
    @ApiModelProperty(value = "合并投产")
    private YesOrNoEnum facMerge;

    /** 下单类型 */
    @ApiModelProperty(value = "下单类型")
    private StylePutIntoType placeOrderType;

    /** 投产类型 */
    @ApiModelProperty(value = "投产类型")
    private PutInProductionType devtType;

    /**
     * 参考款款号
     */
    @ApiModelProperty(value = "参考款款号")
    private String similarBulkStyleNo;

    public String getCommissioningSizeTotal(){
        //线下
        JSONObject offlineJsonObject = Opt.ofNullable(JSON.parseObject(this.offlineCommissioningSize)).orElse(new JSONObject());
        //线上
        JSONObject onlineJsonObject = Opt.ofNullable(JSON.parseObject(this.onlineCommissioningSize)).orElse(new JSONObject());

        offlineJsonObject.putAll(onlineJsonObject);
        return JSON.toJSONString(offlineJsonObject);
    }

}

