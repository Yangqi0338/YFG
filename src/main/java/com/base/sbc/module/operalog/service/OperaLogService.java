package com.base.sbc.module.operalog.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operalog.dto.OperaLogDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.github.pagehelper.PageInfo;

/**
 * @author 卞康
 * @date 2023/6/19 15:47:54
 * @mail 247967116@qq.com
 */
public interface OperaLogService extends BaseService<OperaLogEntity> {


    PageInfo<OperaLogEntity> listPage(OperaLogDto operaLogDto);
}
