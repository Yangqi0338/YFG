package com.base.sbc.module.band.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.mapper.BandMapper;
import com.base.sbc.module.band.service.BandService;
import org.springframework.stereotype.Service;

/**
 * @author 卞康
 * @data 2023/4/7 10:35
 */
@Service
public class BandServiceImpl extends ServiceImpl<BandMapper, Band> implements BandService {
}
