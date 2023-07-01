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


    AttachmentVo getAttachmentById(String id);

    List<AttachmentVo> findByQw(QueryWrapper queryWrapper);


    void setListStylePic(List list, String fileIdKey);

    Integer saveAttachment(List<AttachmentSaveDto> dto, String foreignId, String type);

/** 自定义方法区 不替换的区域【other_end】 **/


}
