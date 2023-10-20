package com.base.sbc.module.httplog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.httplog.entity.HttpLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 卞康
 * @date 2023/5/16 18:50:39
 * @mail 247967116@qq.com
 */
@Mapper
public interface HttpLogMapper extends BaseMapper<HttpLog> {
}
