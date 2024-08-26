//package com.base.sbc.open.entity;
//
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//
//import java.util.Date;
///**
// * @author 卞康
// * @date 2023/7/24 10:17:53
// * @mail 247967116@qq.com
// * 尺寸表类
// * 用于描述尺寸表的各个属性和信息
// */
//@Data
//@TableName("bi_size_chart")
//public class BiSizeChart {
//    /**
//     * 尺寸表名称
//     */
//    private String sizechartName;
//
//    /**
//     * 尺寸表的URL
//     */
//    private String sizechartUrl;
//
//    /**
//     * 配色URL
//     */
//    private String sizechartColors;
//
//    /**
//     * 号型类型
//     */
//    private String sizerangeUrl;
//
//    /**
//     * 子尺码范围
//     */
//    private String sizerangeName;
//
//    /**
//     * 基础尺码
//     */
//    private String baseSize;
//
//    /**
//     * 档差
//     */
//    private String subrangeIncrement;
//
//    /**
//     * 品类测量组
//     */
//    private String sizechartGroup;
//
//    /**
//     * 状态
//     */
//    private String sizechartState;
//
//    /**
//     * 创建人
//     */
//    private String creator;
//
//    /**
//     * 创建时间
//     */
//    private Date createTime;
//
//    /**
//     * 修改人
//     */
//    private String updator;
//
//    /**
//     * 修改时间
//     */
//    private Date updateTime;
//
//    /**
//     * 测量点名称
//     */
//    private String dimensionName;
//
//    /**
//     * 测量点描述
//     */
//    private String dimensionDescription;
//
//    /**
//     * 尺码名称
//     */
//    private String dimensionSizeChart;
//
//    /**
//     * 是否为基础码的标识
//     */
//    private boolean isBaseSize;
//
//    public void setIsBaseSize(boolean b){
//        this.isBaseSize=b;
//    }
//
//    /**
//     * 样板尺寸
//     */
//    private String incrementsCode;
//
//    /**
//     * 成衣尺寸
//     */
//    private String patternCode;
//
//    /**
//     * 洗后尺寸
//     */
//    private String shrinkageCode;
//
//    /**
//     * 测量点的URL
//     */
//    private String dimensionUrl;
//
//    /**
//     * 测量点ID
//     */
//    private String dimensionId;
//
//    /**
//     * 设计款号
//     */
//    private String styleUrl;
//
//    /**
//     * 尺码值URL
//     */
//    private String productSizeUrl;
//
//    /**
//     * 尺码类型值
//     */
//    private String productSizeType;
//
//    /**
//     * 尺码号型
//     */
//    private String productSize;
//
//    /**
//     * 排序码
//     */
//    private String sizeCode;
//}
