/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.dto.AttachmentSaveDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackPatternAttachmentSaveDto;
import com.base.sbc.module.pack.dto.PackTechSpecSavePicDto;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：附件 service类
 * @address com.base.sbc.module.common.service.AttachmentService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 16:05:59
 * @version 1.0  
 */
public interface AttachmentService extends BaseService<Attachment> {


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 通过fid 查询
     *
     * @param foreignId
     * @return
     */

    List<AttachmentVo> findByforeignId(String foreignId, String type);

    List<AttachmentVo> findByforeignIds(List<String> foreignId, String type);

    /**
     * 通过fid 查询
     *
     * @param foreignId
     * @return
     */

    List<AttachmentVo> findByforeignIdTypeLikeStart(String foreignId, String typeLikeStart);

    AttachmentVo getAttachmentById(String id);

    List<AttachmentVo> findByQw(QueryWrapper queryWrapper);


    void setListStylePic(List list, String fileIdKey);

    Integer saveAttachment(List<AttachmentSaveDto> dto, String foreignId, String type);

    /**
     * 资料包-图样附件-分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<AttachmentVo> patternAttachmentPageInfo(PackCommonPageSearchDto dto);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean del(String id);

    /**
     * 保存资料包 -图样附件
     *
     * @param dto
     * @return
     */
    AttachmentVo saveByPA(PackPatternAttachmentSaveDto dto);

    /**
     * 修改备注
     *
     * @param id
     * @param remarks
     * @return
     */
    boolean updateRemarks(String id, String remarks);

    /**
     * 保存工艺说明图片
     *
     * @param dto
     * @return
     */
    AttachmentVo savePackTechSpecPic(PackTechSpecSavePicDto dto);

    /**
     * 注释
     *
     * @param foreignId
     * @param type
     * @return
     */
    boolean delByForeignIdType(String foreignId, String type);

    /**
     * 复制
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @return
     */
    boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag,String specType);

    /**
     * 复制工艺说明文件
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param item
     * @return
     */
    boolean copyTechFile(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String item);

    AttachmentVo getAttachmentByFileId(String fileId);

    /**
     * 保存附件
     * @param id
     * @param files
     * @param type
     */
    void saveFiles(String id, List<SampleAttachmentDto> files, String type);
/** 自定义方法区 不替换的区域【other_end】 **/


}
