/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.hangtag.dto.*;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：吊牌表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
public interface HangTagService extends BaseService<HangTag> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param hangTagDTO
     * @param userCompany
     * @return
     */
    PageInfo<HangTagListVO> queryPageInfo(HangTagSearchDTO hangTagDTO, String userCompany);

    /**
     * 吊牌导出
     * @param response
     * @param hangTagSearchDTO
     */
    void deriveExcel(HttpServletResponse response, HangTagSearchDTO hangTagSearchDTO, String userCompany) throws IOException;

    /**
     * 详情
     *
     * @param bulkStyleNo
     * @param userCompany
     * @return
     */
    HangTagVO getDetailsByBulkStyleNo(String bulkStyleNo, String userCompany, String selectType);


    /**
     * 刷新
     */
    HangTagVO getRefresh(String bulkStyleNo, String userCompany, String selectType);


    /**
     * 保存
     *
     * @param hangTagDTO
     * @param userCompany
     */
    String save(HangTagDTO hangTagDTO, String userCompany);


    void updateSecondPackagingFormById(HangTagDTO hangTagDTO);



    /**
     * 更新状态
     *
     * @param hangTagUpdateStatusDTO
     * @param repeatUpdate
     */
    void updateStatus(HangTagUpdateStatusDTO hangTagUpdateStatusDTO, boolean repeatUpdate, List<HangTag> hangTagList);


    /**
     * 吊牌打印，对外提供接口
     *
     * @return
     */
    List<TagPrinting> hangTagPrinting(String styleNo, boolean likeQueryFlag);

    /**
     * 通过大货款号获取工艺包PDF
     * @param styleNo
     * @return
     */
    String getTechSpecFileByStyleNo(String styleNo);


    /**
     * 复制吊牌
     *
     * @param styleNo
     * @param newStyleNo
     * @return
     */
    Boolean copyPack(String styleNo, String newStyleNo);

    Object getMoreLanguageDetailsByBulkStyleNo(HangTagMoreLanguageDTO hangTagMoreLanguageDTO);

    boolean counterReview(HangTag reviewHangTag);

    /**
     * 通过物料编码获取检查报告
     * @param dto
     * @return
     */
    List<EscmMaterialCompnentInspectCompanyDto> getInspectReport(InspectCompanyDto dto);

// 自定义方法区 不替换的区域【other_end】


}

