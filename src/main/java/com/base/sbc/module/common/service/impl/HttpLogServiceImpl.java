package com.base.sbc.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.mapper.HttpLogMapper;
import com.base.sbc.module.httplog.service.HttpLogService;
import org.springframework.stereotype.Service;
/**
 * @author 卞康
 * @date 2023/5/16 18:51:04
 * @mail 247967116@qq.com
 */
@Service
public class HttpLogServiceImpl extends ServiceImpl<HttpLogMapper, HttpLog> implements HttpLogService {
    // 可以在这里编写额外的业务逻辑方法
}
