package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.entity.BiStyle;
import com.base.sbc.open.mapper.BiStyleMapper;
import com.base.sbc.open.service.BiStyleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/24 16:31:00
 * @mail 247967116@qq.com
 */
@Service
public class BiStyleServiceImpl extends ServiceImpl<BiStyleMapper,BiStyle> implements BiStyleService {
    /**
     *
     */
    @Override
    public void style() {
        List<BiStyle> list = new ArrayList<>();


        this.remove(null);
        this.saveBatch(list,100);
    }
}
