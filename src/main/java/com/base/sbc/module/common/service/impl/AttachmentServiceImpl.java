/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service.impl;

import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.common.mapper.AttachmentMapper;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.vo.AttachmentVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：附件 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.common.service.AttachmentService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 16:05:59
 */
@Service
public class AttachmentServiceImpl extends ServicePlusImpl<AttachmentMapper, Attachment> implements AttachmentService {


//** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 通过fid 查询
     *
     * @param fId
     * @return
     */
    @Override
    public List<AttachmentVo> findByFId(String fId) {
        return getBaseMapper().findByFId(fId);
    }

//** 自定义方法区 不替换的区域【other_end】 **/

}
