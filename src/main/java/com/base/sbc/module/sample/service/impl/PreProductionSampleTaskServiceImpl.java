/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.patternmaking.dto.NodeStatusChangeDto;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto;
import com.base.sbc.module.sample.dto.PreTaskAssignmentDto;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.mapper.PreProductionSampleTaskMapper;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类描述：产前样-任务 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.service.PreProductionSampleTaskService
 * @email your email
 * @date 创建时间：2023-7-18 11:04:08
 */
@Service
public class PreProductionSampleTaskServiceImpl extends BaseServiceImpl<PreProductionSampleTaskMapper, PreProductionSampleTask> implements PreProductionSampleTaskService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private NodeStatusService nodeStatusService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private StyleService styleService;

    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private AmcFeignService amcFeignService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean enableSetting(String id, String enableFlag) {
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.set("enable_flag", enableFlag);
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean taskAssignment(PreTaskAssignmentDto dto) {
        EnumNodeStatus ens = EnumNodeStatus.GARMENT_CUTTING_WAITING_RECEIVED;
        String node = "产前样衣任务";
        String status = "裁剪待接收";
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        List<String> ids = StrUtil.split(dto.getId(), CharUtil.COMMA);
        uw.lambda().in(PreProductionSampleTask::getId, ids)
                .set(PreProductionSampleTask::getCutterId, dto.getCutterId())
                .set(PreProductionSampleTask::getCutterName, dto.getCutterName())
                .set(PreProductionSampleTask::getTechnologistId, dto.getTechnologistId())
                .set(PreProductionSampleTask::getTechnologistName, dto.getTechnologistName())
                .set(PreProductionSampleTask::getGradingId, dto.getGradingId())
                .set(PreProductionSampleTask::getGradingName, dto.getGradingName())
                .set(PreProductionSampleTask::getStitcher, dto.getStitcher())
                .set(PreProductionSampleTask::getStitcherId, dto.getStitcherId())
                .set(PreProductionSampleTask::getNode, node)
                .set(PreProductionSampleTask::getStatus, status);
        setUpdateInfo(uw);
        boolean flg = update(uw);
        for (String id : ids) {
            nodeStatusService.nodeStatusChange(id, node, status, BaseGlobal.YES, BaseGlobal.NO);
        }
        return flg;
    }

    @Override
    public boolean nodeStatusChange(String userId, List<NodeStatusChangeDto> list, GroupUser groupUser) {
        for (NodeStatusChangeDto dto : list) {
            nodeStatusChange(userId, dto, groupUser);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(String userId, NodeStatusChangeDto dto, GroupUser groupUser) {
        nodeStatusService.nodeStatusChange(dto.getDataId(), dto.getNode(), dto.getStatus(), dto.getStartFlg(), dto.getEndFlg());
        // 修改单据
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.eq("id", dto.getDataId());
        if (CollUtil.isNotEmpty(dto.getUpdates())) {
            for (Map.Entry<String, Object> kv : dto.getUpdates().entrySet()) {
                uw.set(StrUtil.toUnderlineCase(kv.getKey()), kv.getValue());
            }
        }
        uw.set("node", dto.getNode());
        uw.set("status", dto.getStatus());
        setUpdateInfo(uw);
        // 修改单据
        return update(uw);
    }

    @Override
    public PageInfo<PreProductionSampleTaskVo> taskList(PreProductionSampleTaskSearchDto dto) {
        BaseQueryWrapper<PreProductionSampleTask> qw = new BaseQueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(dto.getNode()), "t.node", dto.getNode());
        qw.eq(StrUtil.isNotBlank(dto.getStatus()), "t.status", dto.getStatus());
        qw.notEmptyIn("t.finish_flag", dto.getFinishFlag());
        qw.andLike(dto.getSearch(), "s.style_no", "t.code");
        qw.notEmptyIn("s.year", dto.getYear());
        qw.notEmptyIn("s.season", dto.getSeason());
        qw.notEmptyIn("s.month", dto.getMonth());
        Page<PreProductionSampleTaskVo> objects = PageHelper.startPage(dto);
        List<PreProductionSampleTaskVo> list = getBaseMapper().taskList(qw);
        //设置图
        attachmentService.setListStylePic(list, "stylePic");
        //设置头像
        amcFeignService.setUserAvatarToList(list);
        nodeStatusService.setNodeStatus(list);
        return objects.toPageInfo();
    }

    @Override
    public boolean nextOrPrev(Principal user, String id, String np) {
        PreProductionSampleTask pm = getById(id);
        GroupUser groupUser = userUtils.getUserBy(user);
        if (pm == null) {
            throw new OtherException("任务不存在");
        }
        //跳转
        boolean flg = nodeStatusService.nextOrPrev(groupUser, pm, NodeStatusConfigService.PRE_PRODUCTION_SAMPLE_TASK, np);
        updateById(pm);
        return flg;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean createByPackInfo(PackInfoListVo vo) {
        //
        Style style = styleService.getById(vo.getStyleId());

        if (style == null) {
            throw new OtherException("款式信息为空");
        }
        PackInfoListVo packInfo = packInfoService.getByQw(vo.getStyleId(), vo.getPackType());
        if (packInfo == null) {
            throw new OtherException("标准资料包数据为空");
        }
        PreProductionSampleTask task = new PreProductionSampleTask();
        task.setStyleId(style.getId());
        task.setPackInfoId(packInfo.getId());
        task.setPlanningSeasonId(style.getPlanningSeasonId());
        task.setDesignDetailTime(packInfo.getToBigGoodsDate());

        QueryWrapper<PreProductionSampleTask> countQc = new QueryWrapper<>();
        countQc.eq("style_id", style.getId());
        long count = getBaseMapper().countByQw(countQc);
        task.setCode(packInfo.getStyleNo() + StrUtil.DASHED + (count + 1));
        return save(task);
    }

    @Override
    public PreProductionSampleTaskDetailVo getTaskDetailById(String id) {
        List<PreProductionSampleTaskVo> taskList = getBaseMapper().taskList(new QueryWrapper<PreProductionSampleTask>().eq("t.id", id));
        if (CollUtil.isEmpty(taskList)) {
            return null;
        }
        PreProductionSampleTaskVo task = taskList.get(0);

        PreProductionSampleTaskDetailVo result = new PreProductionSampleTaskDetailVo();
        //查询任务信息
        PreProductionSampleTaskVo taskVo = BeanUtil.copyProperties(task, PreProductionSampleTaskVo.class);
        //设置头像
        amcFeignService.setUserAvatarToObj(taskVo);
        //查询款式设计信息
        StyleVo sampleDesignVo = styleService.getDetail(task.getStyleId());
        //设置头像
        amcFeignService.setUserAvatarToObj(sampleDesignVo);
        result.setTask(taskVo);
        result.setStyle(sampleDesignVo);
        // 设置状态
        nodeStatusService.setNodeStatusToBean(taskVo, "nodeStatusList", "nodeStatus");
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateByDto(PreProductionSampleTaskDto dto) {
        PreProductionSampleTask task = getById(dto.getId());
        if (task == null) {
            throw new OtherException("任务不存在");
        }

        //齐套状态发生改变,修改齐套时间
        if (!task.getKitting().equals(dto.getKitting())){
            dto.setKittingTime(new Date());
        }
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.eq("id", dto.getId());
        update(dto, uw);
        return true;
    }

    @Override
    public boolean sampleMakingScore(Principal user, String id, BigDecimal score) {

        PreProductionSampleTask bean = getById(id);
        if (bean == null) {
            throw new OtherException("打版信息为空");
        }
        GroupUser groupUser = userUtils.getUserBy(user);
//        //校验是否是样衣组长
//        boolean sampleTeamLeader = amcFeignService.isSampleTeamLeader(bean.getPatternRoomId(), groupUser.getId());
//        if(!sampleTeamLeader){
//            throw new OtherException("您不是"+bean.getPatternRoom()+"的样衣组长");
//        }
        PreProductionSampleTask updateBean = new PreProductionSampleTask();
        updateBean.setSampleMakingScore(score);
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.lambda().eq(PreProductionSampleTask::getId, id);
        return update(updateBean, uw);
    }

// 自定义方法区 不替换的区域【other_end】

}
