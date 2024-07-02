/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.sample.vo.StyleUploadVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：样衣-款式配色 dao类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.dao.SampleStyleColorDao
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Mapper
public interface StyleColorMapper extends BaseMapper<StyleColor> {
// 自定义方法区 不替换的区域【other_start】

    /*查询大货款号是否存在*/
    int isStyleNoExist(String styleNo);

    /*查询款式下的额配色*/
    String getStyleColorNumber(@Param("styleNo") String styleNo, @Param("length1") Integer length1);

    /**
     * 查询款式配色
     *
     * @param qw
     * @return
     */
    List<StyleColorVo> styleColorList(@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 查询款式设计
     *
     * @return
     */
    List<Style> pageBySampleDesign(@Param(Constants.WRAPPER) BaseQueryWrapper<Style> qw);


    /**
     * 查询配色列表
     *
     * @param qw
     * @return
     */
    List<StyleColorVo> colorList(@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 大货款列表
     * @param qw
     * @return
     */
    List<CompleteStyleVo> pageCompleteStyle(@Param(Constants.WRAPPER) QueryWrapper qw );

    /**
     * 修改所有引用的大货款号
     *
     * @param styleNo
     * @param nweStyleNo
     * @return
     */
    Boolean reviseAllStyleNo(@Param("styleNo") String styleNo, @Param("nweStyleNo") String nweStyleNo);

    /**
     * 获取款式图上传信息
     *
     * @param styleColorId
     * @return
     */
    StyleUploadVo getStyleUploadInfo(String styleColorId);

    /**
     * 用于查询匹配企划需求维度 已下单的配色信息
     *
     * @param qw
     * @return
     */
    List<DemandOrderSkcVo> queryDemandOrderSkc(@Param(Constants.WRAPPER) QueryWrapper qw);


    /**
     * 查询出配色中关联的主款或配饰
     *
     * @param ids
     * @return
     */
    List<StyleColor> getStyleMainAccessories(@Param("ids") List<String> ids);

    List<StyleColor> getStyleMainAccessoriesNoSendFlag(@Param("ids") List<String> ids);

    List<StyleMarkingCheckVo> markingCheckPage(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    /**
     * 根据大货款获取设计师，版师，样衣工信息
     *
     * @param styleNos
     * @return
     */
    List<StyleNoUserInfoVo> getStyleDesignerInfo(@Param("styleNos") List<String> styleNos);

    List<StyleColorAgentVo> agentList(@Param(Constants.WRAPPER) BaseQueryWrapper queryWrapper);

// 自定义方法区 不替换的区域【other_end】

    List<StyleColorVo> styleColorList_COUNT(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<StyleColorVo> colorList_COUNT(@Param(Constants.WRAPPER) QueryWrapper qw);
}