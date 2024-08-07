/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.utils.GenTechSpecPdfFile;
import com.base.sbc.module.pack.vo.*;
import com.base.sbc.module.pricing.vo.PricingVO;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.base.sbc.open.dto.OpenStyleDto;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
public interface PackInfoService extends PackBaseService<PackInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过款式设计查询
     *
     * @param pageDto
     * @return
     */
    PageInfo<StylePackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto);

    /**
     * 通过款式设计创建BOM基础信息
     *
     * @param dto@return
     */
    PackInfoListVo createByStyle(CreatePackInfoByStyleDto dto);

    /**
     * 查询
     *
     * @param foreignIds
     * @return
     */
    Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds, String packType);

    /**
     * 修改日志
     *
     * @param pageDto
     * @return
     */
    PageInfo<OperaLogEntity> operationLog(PackCommonPageSearchDto pageDto);

    /**
     * 技术BOM 转 大货
     *
     * @param dto@return
     */
    boolean toBigGoods(PackCommonSearchDto dto);

    /**
     * 资料包拷贝
     *
     * @param sourceForeignId 源主数据id
     * @param sourcePackType  源资料包类型
     * @param targetForeignId 目标主数据id
     * @param targetPackType  目标资料包类型
     * @param flg             0 正常拷贝,  1 转大货 ,2 反审
     * @param flag            bom阶段复制
     * @return
     */
    boolean copyPack(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag, String flg,String flag );

    /**
     * 开启审批
     *
     * @param id
     * @return
     */
    boolean startReverseApproval(String id);

    /**
     * 处理反审
     *
     * @param dto
     * @return
     */
    boolean reverseApproval(AnswerDto dto);

    List<PackInfoListVo> queryByQw(QueryWrapper queryWrapper);

    PackInfoListVo getByQw(QueryWrapper queryWrapper);

    PageInfo<BigGoodsPackInfoListVo> pageByBigGoods(PackInfoSearchPageDto pageDto);

    /**
     * 资料包导出
     * @param response
     * @param pageDto
     * @return
     */
    void pageByBigGoodsDerive(HttpServletResponse response,  PackInfoSearchPageDto pageDto) throws IOException;

    /**
     * 获取明细
     *
     * @param id       资料包id
     * @param packType 资料包类型,用于获取资料包状态
     * @return
     */
    PackInfoListVo getDetail(String id, String packType);

    /**
     * @param foreignId 款式设计id
     * @param packType
     * @return
     */
    PackInfoListVo getByQw(String foreignId, String packType);

    boolean startApproval(String id);

    boolean approval(AnswerDto dto);

    /**
     * 核价管理选择制版单列表
     *
     * @param pricingSelectSearchDTO
     * @return
     */
    PageInfo<PricingSelectListVO> pricingSelectList(PricingSelectSearchDTO pricingSelectSearchDTO);

    /**
     * 生成工艺说明文件
     *
     * @param dto
     * @return
     */
    AttachmentVo genTechSpecFile(PackCommonSearchDto dto);

    /**
     * 删除工艺说明文件
     *
     * @param dto
     * @return
     */
    boolean delTechSpecFile(PackCommonSearchDto dto);

    /**
     * 通过id获取核价对象
     *
     * @param id
     * @return
     */
    PricingVO getPricingVoById(String id);

    /**
     * 样衣id查询bom
     *
     * @param styleId
     * @return
     */
    PageInfo<PackInfoListVo> getInfoListByDesignNo(String styleId);

    boolean association(PackInfoAssociationDto dto);

    /**
     * 取消关联配色
     * @param dto
     * @return
     */
    boolean cancelAssociation(PackInfoAssociationDto dto);

    /**
     * 改变bom 状态
     *
     * @param packInfoId
     */
    void changeBomStatus(String packInfoId, String bomStatus);

    PageInfo<PackInfoListVo> pageInfo(PackInfoSearchPageDto pageDto);

    boolean setPatternNo(PackInfoSetPatternNoDto dto);

    /**
     * 生成文件
     *
     * @param groupUser
     * @param dto
     * @return
     */
    AttachmentVo genTechSpecFile2(GroupUser groupUser, PackCommonSearchDto dto);

    GenTechSpecPdfFile queryGenTechSpecPdfFile(GroupUser groupUser, PackCommonSearchDto dto);

    Pair<String, JSONObject> genTechSpecFile2Html(GroupUser groupUser, PackCommonSearchDto dto);

    CopyItemsVo copyItems(GroupUser user, PackCopyDto dto);

    BomPrintVo getBomPrint(GroupUser user, PackCommonSearchDto dto);

    boolean updatePackInfoStatusField(PackInfoStatusVo dto);

    /**
     * 修改资料包信息
     * @param packInfo
     * @return
     */
    boolean updatePackInfo(PackInfoDto packInfo);

    AttachmentVo saveVideoFile(String foreignId, String packType, String fileId);

    /**
     * 领猫同步数据
     * @param companyCode
     * @return
     */
    List<OpenStyleDto> getStyleListForLinkMore(String companyCode);

    /**
     * 重写删除资料报
     *
     * @param removeDto 删除参数
     * @return
     */
    boolean removeByIds(RemoveDto removeDto);

    /**
     * BOM 复制
     *
     * @param dto
     * @return
     */
    PackInfoListVo copyBom(CopyBomDto dto);


    /**
     * 修改BOM名称
     * @param infoCode
     * @param styleNo
     * @return
     */
    Boolean updateBomName(String infoCode, String styleNo);

    PageInfo<FabricSummaryInfoVo> selectFabricSummaryStyle(FabricSummaryV2Dto dto);

    String getByIdBrandName(String foreignId);

    void setTechReceiveDate(String id, Date techReceiveDate, String orderDept);


// 自定义方法区 不替换的区域【other_end】


}
