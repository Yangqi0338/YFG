/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.sample.dto.PreProductionSampleDto;
import com.base.sbc.module.sample.dto.PreProductionSampleSearchDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.entity.PreProductionSample;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.mapper.PreProductionSampleMapper;
import com.base.sbc.module.sample.service.PreProductionSampleService;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.sample.vo.PreProductionSampleVo;
import com.base.sbc.module.sample.vo.SampleDesignVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：产前样 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.service.PreProductionSampleService
 * @email your email
 * @date 创建时间：2023-7-18 11:04:06
 */
@Service
public class PreProductionSampleServiceImpl extends BaseServiceImpl<PreProductionSampleMapper, PreProductionSample> implements PreProductionSampleService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private PreProductionSampleTaskService preProductionSampleTaskService;
    @Autowired
    private AmcFeignService amcFeignService;
    @Autowired
    private NodeStatusService nodeStatusService;


    @Resource
    private AttachmentService attachmentService;
    @Resource
    private SampleDesignService sampleDesignService;

    @Override
    public PreProductionSample getByPackInfoId(String packInfoId) {
        QueryWrapper<PreProductionSample> qw = new QueryWrapper<>();
        qw.eq("pack_info_id", packInfoId);
        qw.last("limit 1");
        PreProductionSample pre = getOne(qw);
        return pre;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean createByPackInfo(String packInfoId) {
        PackInfo packInfo = packInfoService.getById(packInfoId);
        return createByPackInfo(packInfo);
    }

    @Override
    public boolean createByPackInfo(PackInfo packInfo) {
        PreProductionSample pre = getByPackInfoId(packInfo.getId());
        if (pre != null) {
            return true;
        }
        SampleDesign sampleDesign = sampleDesignService.getById(packInfo.getForeignId());
        pre = new PreProductionSample();
        BeanUtil.copyProperties(sampleDesign, pre, "id");
        BeanUtil.copyProperties(packInfo, pre, "id");
        CommonUtils.resetCreateUpdate(pre);
        pre.setPackInfoId(packInfo.getId());
        pre.setSampleDesignId(packInfo.getForeignId());
        return save(pre);
    }

    @Override
    public PageInfo<PreProductionSampleVo> pageInfo(PreProductionSampleSearchDto dto) {
        BaseQueryWrapper<PreProductionSample> qw = new BaseQueryWrapper<>();
        qw.notEmptyIn("year", dto.getYear());
        qw.notEmptyIn("month", dto.getMonth());
        qw.notEmptyIn("season", dto.getSeason());
        qw.notEmptyIn("pattern_design_id", dto.getPatternDesignId());
        Page<PreProductionSample> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PreProductionSampleVo> voPageInfo = CopyUtil.copy(page.toPageInfo(), PreProductionSampleVo.class);
        List<PreProductionSampleVo> list = voPageInfo.getList();
        if (CollUtil.isNotEmpty(list)) {
            //图片
            attachmentService.setListStylePic(list, "stylePic");
            List<String> ids = list.stream().map(PreProductionSampleVo::getId).collect(Collectors.toList());
            List<PreProductionSampleTask> taskList = preProductionSampleTaskService.list(new QueryWrapper<PreProductionSampleTask>().in("pre_production_sample_id", ids));
            List<PreProductionSampleTaskVo> voTaskList = BeanUtil.copyToList(taskList, PreProductionSampleTaskVo.class);
            Map<String, List<PreProductionSampleTaskVo>> subMap = Opt.ofNullable(voTaskList).
                    map(a -> a.stream().collect(
                            Collectors.groupingBy(PreProductionSampleTaskVo::getPreProductionSampleId))
                    ).orElse(MapUtil.empty());
            for (PreProductionSampleVo preProductionSampleVo : list) {
                preProductionSampleVo.setTaskList(subMap.get(preProductionSampleVo.getId()));
            }
        }
        return voPageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveByDto(PreProductionSampleDto dto) {
        PreProductionSample byId = getById(dto.getId());
        if (byId == null) {
            throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
        }
        BeanUtil.copyProperties(dto, byId);
        return updateById(byId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PreProductionSampleTaskVo saveTaskByDto(PreProductionSampleTaskDto dto) {
        PreProductionSample byId = getById(dto.getPreProductionSampleId());
        if (byId == null) {
            throw new OtherException("产前样信息为空");
        }
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PreProductionSample pre = getById(dto.getPreProductionSampleId());
            if (pre == null) {
                throw new OtherException("无产前样信息");
            }
            PreProductionSampleTask pageData = new PreProductionSampleTask();
            BeanUtil.copyProperties(dto, pageData, "id");
            pageData.setPlanningSeasonId(byId.getPlanningSeasonId());
            CommonUtils.resetCreateUpdate(pageData);
            QueryWrapper<PreProductionSampleTask> countQc = new QueryWrapper<>();
            countQc.eq("pre_production_sample_id", dto.getPreProductionSampleId());
            long count = preProductionSampleTaskService.count();
            pageData.setCode(byId.getStyleNo() + StrUtil.DASHED + (count + 1));
            preProductionSampleTaskService.save(pageData);
            return BeanUtil.copyProperties(pageData, PreProductionSampleTaskVo.class);
        } else {
            //修改
            dto.setPreProductionSampleId(null);
            PreProductionSampleTask dbData = preProductionSampleTaskService.getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            preProductionSampleTaskService.updateById(dbData);
            return BeanUtil.copyProperties(dbData, PreProductionSampleTaskVo.class);
        }


    }

    @Override
    public PreProductionSampleTaskDetailVo getTaskDetailById(String id) {
        PreProductionSampleTask task = preProductionSampleTaskService.getById(id);
        if (task == null) {
            return null;
        }
        PreProductionSampleTaskDetailVo result = new PreProductionSampleTaskDetailVo();
        PreProductionSample sample = getById(task.getPreProductionSampleId());
        PreProductionSampleVo preProductionSampleVo = BeanUtil.copyProperties(sample, PreProductionSampleVo.class);
        result.setPre(preProductionSampleVo);
        PreProductionSampleTaskVo taskVo = BeanUtil.copyProperties(task, PreProductionSampleTaskVo.class);
        //设置头像
        amcFeignService.setUserAvatarToObj(taskVo);
        //查询样衣设计信息
        SampleDesignVo sampleDesignVo = sampleDesignService.getDetail(sample.getSampleDesignId());
        //设置头像
        amcFeignService.setUserAvatarToObj(sampleDesignVo);
        result.setTask(taskVo);
        result.setSampleDesign(sampleDesignVo);
        // 设置状态
        nodeStatusService.setNodeStatusToBean(taskVo, "nodeStatusList", "nodeStatus");
        return result;
    }

// 自定义方法区 不替换的区域【other_end】

}
