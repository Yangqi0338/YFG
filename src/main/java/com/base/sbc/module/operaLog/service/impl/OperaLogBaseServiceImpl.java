package com.base.sbc.module.operaLog.service.impl;

import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.mapper.OperaLogMapper;
import com.base.sbc.module.operaLog.service.OperaLogService;
import org.springframework.stereotype.Service;

/**
 * @author 卞康
 * @date 2023/6/19 15:48:45
 * @mail 247967116@qq.com
 */
@Service
public class OperaLogBaseServiceImpl extends BaseServiceImpl<OperaLogMapper, OperaLogEntity> implements OperaLogService {
}
