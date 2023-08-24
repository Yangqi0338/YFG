package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.open.entity.BiColorway;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.mapper.BiSampleMapper;
import com.base.sbc.open.service.BiSampleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/24 16:26:28
 * @mail 247967116@qq.com
 */
@Service
public class BiSampleServiceImpl extends ServiceImpl<BiSampleMapper,BiSample> implements BiSampleService {
    /**
     *
     */
    @Override
    public void sample() {
        List<BiSample> list = new ArrayList<>();


        this.remove(null);
        this.saveBatch(list,100);
    }
}
