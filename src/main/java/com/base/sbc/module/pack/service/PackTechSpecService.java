/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackTechSpec;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：资料包-工艺说明 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechSpecService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
public interface PackTechSpecService extends PackBaseService<PackTechSpec> {

// 自定义方法区 不替换的区域【other_start】

    String PATH_SQ_EL = "'资料包-'+#p0.packType+'-'+#p0.foreignId+'-工艺说明-'+#p0.specType";

    /**
     * 列表
     *
     * @param dto
     * @return
     */
    List<PackTechSpecVo> list(PackTechSpecSearchDto dto);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PackTechSpecVo saveByDto(PackTechSpecDto dto);

    /**
     * 生成款式图片
     *
     * @param newContent
     * @param oldContent
     * @param bean
     */
    void genContentImgUrl(String newContent, String oldContent, PackTechSpec bean);


    /**
     * 图片列表
     *
     * @param dto
     * @return
     */
    List<PackTechAttachmentVo> picList(PackTechSpecSearchDto dto);

    /**
     * 图片列表排序
     * @param attachmentList
     */
    void picListSort(List<Attachment> attachmentList);
    public void copyItem(String id, String packType, String targetForeignId, String targetPackType, String item);

    /**
     * 保存图片
     *
     * @param dto
     * @return
     */
    AttachmentVo savePic(PackTechSpecSavePicDto dto);

    /**
     * 查询变更日志
     *
     * @param pageDto
     * @return
     */
    PageInfo<OperaLogEntity> operationLog(PackTechSpecPageDto pageDto);

    /**
     * 复制
     *
     * @param list
     * @return
     */
    List<PackTechSpecVo> copyOther(List<PackTechSpecDto> list);

    List<PackTechSpecVo> batchSave(PackTechSpecBatchSaveDto dto);

    boolean references(PackTechSpecReferencesDto dto);


    /**
     * 删除工艺说明
     * @param dto
     * @return
     */
    boolean removeById(IdsDto dto);

// 自定义方法区 不替换的区域【other_end】


}
