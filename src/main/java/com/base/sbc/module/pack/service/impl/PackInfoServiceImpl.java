/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.SampleDesignPackInfoListVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@Service
public class PackInfoServiceImpl extends BaseServiceImpl<PackInfoMapper, PackInfo> implements PackInfoService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private SampleDesignService sampleDesignService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private OperaLogService operaLogService;
    @Autowired
    private PackBomVersionService packBomVersionService;

    @Override
    public PageInfo<SampleDesignPackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto) {

        // 查询样衣设计数据
        BaseQueryWrapper<SampleDesign> sdQw = new BaseQueryWrapper<>();
        sdQw.in("status", "1", "2");
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        Page<SampleDesign> page = PageHelper.startPage(pageDto);
        sampleDesignService.list(sdQw);
        PageInfo<SampleDesignPackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), SampleDesignPackInfoListVo.class);
        //查询bom列表
        List<SampleDesignPackInfoListVo> sdpList = pageInfo.getList();
        if (CollUtil.isNotEmpty(sdpList)) {
            //图片
            attachmentService.setListStylePic(sdpList, "stylePic");
            List<String> sdIds = sdpList.stream().map(SampleDesignPackInfoListVo::getId).collect(Collectors.toList());
            Map<String, List<PackInfoListVo>> piMaps = queryListToMapGroupByForeignId(sdIds, PackUtils.PACK_TYPE_DESIGN);
            for (SampleDesignPackInfoListVo sd : sdpList) {
                List<PackInfoListVo> packInfoListVos = piMaps.get(sd.getId());
                sd.setPackInfoList(packInfoListVos);
                if (CollUtil.isNotEmpty(packInfoListVos)) {
                    for (PackInfoListVo packInfoListVo : packInfoListVos) {
                        packInfoListVo.setStylePic(sd.getStylePic());
                    }
                }
            }
        }
        return pageInfo;
    }

    @Override
    public PackInfoListVo createBySampleDesign(String id) {
        SampleDesign sampleDesign = sampleDesignService.getById(id);
        if (sampleDesign == null) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
        PackInfo packInfo = BeanUtil.copyProperties(sampleDesign, PackInfo.class, "id", "status");
        packInfo.setPackType(PackUtils.PACK_TYPE_DESIGN);
        packInfo.setForeignId(id);
        packInfo.setBomStatus(BasicNumber.ZERO.getNumber());
        //设置编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("pack_type", PackUtils.PACK_TYPE_DESIGN);
        codeQw.eq("foreign_id", id);
        long count = count(codeQw);
        packInfo.setCode(sampleDesign.getDesignNo() + StrUtil.DASHED + (count + 1));
        save(packInfo);
        //新建bom版本
        PackBomVersionDto versionDto = BeanUtil.copyProperties(packInfo, PackBomVersionDto.class, "id");
        packBomVersionService.saveVersion(versionDto);
        return BeanUtil.copyProperties(getById(packInfo.getId()), PackInfoListVo.class);
    }

    @Override
    public Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("foreign_id", foreignIds);
        qw.eq("pack_type", packType);
        List<PackInfoListVo> list = BeanUtil.copyToList(list(qw), PackInfoListVo.class);
        return Opt.ofNullable(list).map(l -> l.stream().collect(Collectors.groupingBy(PackInfoListVo::getForeignId))).orElse(MapUtil.empty());
    }

    @Override
    public void log(String name, String foreignId, String id, String content) {
        try {
            OperaLogEntity log = new OperaLogEntity();
            log.setParentId(foreignId);
            log.setDocumentId(id);
            log.setContent(content);
            log.setName(name);
            operaLogService.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// 自定义方法区 不替换的区域【other_end】

}
