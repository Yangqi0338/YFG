package com.base.sbc.module.material.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.material.entity.MaterialDetails;
import com.base.sbc.module.material.mapper.MaterialDetailsMapper;
import com.base.sbc.module.material.service.MaterialDetailsService;
import org.springframework.stereotype.Service;

/**
 * 素材详情
 * @author 卞康
 * @date 2023/4/1 11:00:12
 */
@Service
public class MaterialDetailsServiceImpl extends ServiceImpl<MaterialDetailsMapper,MaterialDetails> implements MaterialDetailsService {
}
