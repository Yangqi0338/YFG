package com.base.sbc.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.open.entity.BiSample;

/**
 * @author 卞康
 * @date 2023/8/24 16:26:12
 * @mail 247967116@qq.com
 */
public interface BiSampleService extends IService<BiSample> {
    void sample();
}
