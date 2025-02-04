/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.entity.ReplayConfig;
import com.base.sbc.module.replay.mapper.ReplayConfigMapper;
import com.base.sbc.module.replay.service.ReplayConfigService;
import com.base.sbc.module.replay.vo.ReplayConfigQO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.REPLAY_CV;

/**
 * 类描述：基础资料-复盘管理 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.service.ReplayConfigService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Service
public class ReplayConfigServiceImpl extends BaseServiceImpl<ReplayConfigMapper, ReplayConfig> implements ReplayConfigService {

// 自定义方法区 不替换的区域【other_start】

    private static String brandDictKey = "C8_Brand";
    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    public PageInfo<ReplayConfigDTO> queryPageInfo(ReplayConfigQO qo) {
        // 构建查询
        Page<ReplayConfig> page = qo.startPage();
        LambdaQueryWrapper<ReplayConfig> qw = new BaseLambdaQueryWrapper<ReplayConfig>()
                .notEmptyEq(ReplayConfig::getBrand, qo.getBrand())
                .notEmptyIn(ReplayConfig::getBrandName, qo.getBrandName());
        List<ReplayConfig> list = this.list(qw);
        // 如果有品牌查询,直接查询库,而不会出现未生成配置的数据,所以不走下面的拼接逻辑
        if (StrUtil.isBlank(qo.getBrand())) {
            List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(brandDictKey);
            // 将不存在数据库的数据进行拼接
            List<ReplayConfig> extendReplayConfig = dictInfoToList.stream().filter(dict -> list.stream().noneMatch(it -> dict.getValue().equals(it.getBrand()))).map(dict -> {
                ReplayConfig replayConfig = new ReplayConfig();
                replayConfig.setBrand(dict.getValue());
                replayConfig.setBrandName(dict.getName());
                return replayConfig;
            }).collect(Collectors.toList());
            page.getResult().addAll(extendReplayConfig);
        }
        return REPLAY_CV.copy2DTO(page.toPageInfo());
    }

    @Override
    public ReplayConfigDTO getDetailByBrand(String brand) {
        // 根据品牌获取详情
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(brandDictKey);
        BasicBaseDict dict = dictInfoToList.stream().filter(it -> it.getValue().equals(brand)).findFirst().orElseThrow(() -> new OtherException("错误的品牌"));
        ReplayConfig replayConfig = this.findOne(new LambdaQueryWrapper<ReplayConfig>().eq(ReplayConfig::getBrand, dict.getValue()));
        if (replayConfig == null) return new ReplayConfigDTO(dict);
        return REPLAY_CV.copy2DTO(replayConfig);
    }

    @Override
    public String doSave(ReplayConfigDTO replayConfigDTO) {
        String id = replayConfigDTO.getId();
        String brand = replayConfigDTO.getBrand();
        // 检查是否存在该品牌
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(brandDictKey);
        BasicBaseDict brandDict = dictInfoToList.stream().filter(it -> it.getValue().equals(brand)).findFirst().orElseThrow(() -> new OtherException("错误的品牌编码"));
        String brandName = brandDict.getName();
        // 若为新增,检查数据库是否存在品牌的数据
        if (StrUtil.isBlank(id)) {
            if (this.exists(new LambdaQueryWrapper<ReplayConfig>().eq(ReplayConfig::getBrand, brand)))
                throw new OtherException("不允许新增已存在的" + brandName + "品牌数据");
            replayConfigDTO.setBrandName(brandName);
            this.save(replayConfigDTO);
        } else {
            // 做复制
            ReplayConfig replayConfig = this.getById(id);
            REPLAY_CV.copy(replayConfig, replayConfigDTO);
            this.updateById(replayConfig);
        }
        return replayConfigDTO.getId();
    }

// 自定义方法区 不替换的区域【other_end】

}
