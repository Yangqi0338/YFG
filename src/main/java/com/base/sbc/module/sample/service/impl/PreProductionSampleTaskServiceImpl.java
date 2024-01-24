/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.*;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.patternmaking.dto.NodeStatusChangeDto;
import com.base.sbc.module.patternmaking.dto.SamplePicUploadDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto;
import com.base.sbc.module.sample.dto.PreTaskAssignmentDto;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.mapper.PreProductionSampleTaskMapper;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVoExcel;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
    Logger log = LoggerFactory.getLogger(getClass());

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
    private MinioUtils minioUtils;
    @Autowired
    private AmcFeignService amcFeignService;
    @Autowired
    private DataPermissionsService dataPermissionsService;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private StyleColorService styleColorService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean enableSetting(String id, String enableFlag, String code) {
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.set("enable_flag", enableFlag);
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));

        String type;
        if ("1".equals(enableFlag)) {
            type = "启用";
        } else {
            type = "停用";
        }
        // 记录日志
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setType(type);
        operaLogEntity.setName("产前样看板");
        // operaLogEntity.setDocumentId(id);
        operaLogEntity.setDocumentCode(code);
        this.saveLog(operaLogEntity);
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
        List<PreProductionSampleTask> preProductionSampleTasks = listByIds(ids);
        for (PreProductionSampleTask task : preProductionSampleTasks) {
            sort(task, false);
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
        qw.andLike(dto.getSearch(), "s.style_no", "t.code","p.style_no");
        qw.notEmptyIn("s.year", dto.getYear());
        qw.notEmptyIn("s.season", dto.getSeason());
        qw.notEmptyIn("s.month", dto.getMonth());
         qw.notEmptyEq("s.prod_category", dto.getProdCategory());
        Page<PreProductionSampleTaskVo> objects = PageHelper.startPage(dto);
        if (YesOrNoEnum.NO.getValueStr().equals(dto.getFinishFlag())) {
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.pre_production_sample_task.getK(), "s.");
        } else {
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.pre_production_sample_board.getK(), "s.");
        }
        List<PreProductionSampleTaskVo> list = getBaseMapper().taskList(qw);
        // 设置头像
        amcFeignService.setUserAvatarToList(list);
        nodeStatusService.setNodeStatus(list);
        /**/
        if(StrUtil.equals(dto.getImgFlag(),BaseGlobal.YES)){
            /*带图片只能导出3000条*/
            if(objects.toPageInfo().getList().size() >2000){
                throw new OtherException("带图片最多只能导出2000条");
            }
            return objects.toPageInfo();
        }
        // 设置图
        stylePicUtils.setStylePic(list, "stylePic");
        minioUtils.setObjectUrlToList(objects.toPageInfo().getList(), "samplePic");
        return objects.toPageInfo();
    }

    /**
     * 任务-列表导出
     *
     * @param response
     * @param dto
     */
    @Override
    public void taskderiveExcel(HttpServletResponse response, PreProductionSampleTaskSearchDto dto) throws IOException {
        /**/
        List<PreProductionSampleTaskVo> sampleTaskVoList = taskList(dto).getList();

        List<PreProductionSampleTaskVoExcel> list = CopyUtil.copy(sampleTaskVoList, PreProductionSampleTaskVoExcel.class);


        /*开启一个线程池*/
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                .build();
        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*获取图片链接*/
                stylePicUtils.setStylePic(list, "stylePic",30);
                minioUtils.setObjectUrlToList(list, "samplePic");
                /*计时器*/
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (PreProductionSampleTaskVoExcel preProductionSampleTaskVoExcel : list) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = preProductionSampleTaskVoExcel.getStylePic();
                            preProductionSampleTaskVoExcel.setPic(HttpUtil.downloadBytes(stylePic));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list, PreProductionSampleTaskVoExcel.class, "产前样看板.xlsx", new ExportParams("产前样看板", "产前样看板", ExcelType.HSSF), response);
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public boolean nextOrPrev(Principal user, String id, String np) {
        PreProductionSampleTask pm = getById(id);
        sort(pm, true);
        GroupUser groupUser = userUtils.getUserBy(user);
        if (pm == null) {
            throw new OtherException("任务不存在");
        }
        // 跳转
        boolean flg = nodeStatusService.nextOrPrev(groupUser, pm, NodeStatusConfigService.PRE_PRODUCTION_SAMPLE_TASK, np);
        updateById(pm);
        sort(pm, false);
        return flg;
    }

    /**
     * 排序
     *
     * @param pm
     * @param excludeSelf 排除自己
     */
    void sort(PreProductionSampleTask pm, boolean excludeSelf) {
        String node = pm.getNode();
        String status = pm.getStatus();
        JSONObject nodeStatusConfig = nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PRE_PRODUCTION_SAMPLE_TASK, node, status);
        if (nodeStatusConfig == null) {
            return;
        }
        String userField = nodeStatusConfig.getString("userName");
        // 样衣组长特殊处理
        if (StrUtil.equals(userField, "样衣组长")) {
            userField = "patternRoomId";
        }
        if (StrUtil.isBlank(userField)) {
            return;
        }
        boolean hasField = ReflectUtil.hasField(PatternMaking.class, userField);
        if (!hasField) {
            return;
        }
        // 重新排序
        QueryWrapper<PreProductionSampleTask> qw = new QueryWrapper<>();
        qw.select("id");
        qw.lambda().eq(PreProductionSampleTask::getNode, node).eq(PreProductionSampleTask::getStatus, status);
        if (excludeSelf) {
            qw.ne("id", pm.getId());
        }
        qw.eq(StrUtil.toUnderlineCase(userField), BeanUtil.getProperty(pm, userField));
        qw.exists("select id from t_style where id=t_pre_production_sample_task.style_id and del_flag='0'");
        qw.exists("select id from t_pack_info where id=t_pre_production_sample_task.pack_info_id and del_flag='0'");
        qw.last("order by sort is null  , sort asc ");

        List<Map<String, Object>> ids = listMaps(qw);
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<PreProductionSampleTask> updataList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            PreProductionSampleTask u = new PreProductionSampleTask();
            u.setId(MapUtil.getStr(ids.get(i), "id"));
            u.setSort(new BigDecimal(String.valueOf(i)).setScale(0));
            updataList.add(u);
        }
        updateBatchById(updataList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean createByPackInfo(PackInfoListVo vo) {

        //
        Style style = styleService.getById(vo.getStyleId());

        if (style == null) {
            throw new OtherException("款式信息为空");
        }
        PackInfoListVo packInfo = packInfoService.getDetail(vo.getId(), vo.getPackType());
        if (packInfo == null) {
            throw new OtherException("标准资料包数据为空");
        }

        // 判断是否重复
        //region 20231226-hq-禅道4530编号 一个资料包下可以有多个产前样任务
        /*BaseQueryWrapper<PreProductionSampleTask> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("style_id", style.getId());
        queryWrapper.eq("pack_info_id", packInfo.getId());

        List<PreProductionSampleTask> list = this.list(queryWrapper);
        if (!list.isEmpty()) {
            throw new OtherException("该资料包已经生成产前样任务");
        }*/
        //endregion

        PreProductionSampleTask task = new PreProductionSampleTask();
        task.setStyleId(style.getId());
        task.setPackInfoId(packInfo.getId());
        task.setPlanningSeasonId(style.getPlanningSeasonId());
        task.setDesignDetailTime(packInfo.getToBigGoodsDate());

        QueryWrapper<PreProductionSampleTask> countQc = new QueryWrapper<>();
        countQc.eq("style_id", style.getId());
        long count = getBaseMapper().countByQw(countQc);
        task.setCode(Opt.ofBlankAble(packInfo.getStyleNo()).orElse(packInfo.getDesignNo()) + StrUtil.DASHED + (count + 1));

        //查询款式配色数据，保持技术接收时间（工艺接收明细单时间）一致
        String styleColorId = packInfo.getStyleColorId();
        StyleColor styleColor = styleColorService.getById(styleColorId);
        task.setTechReceiveTime(styleColor.getTechReceiveTime());

        return save(task, "产前样看板");
    }

    @Override
    public PreProductionSampleTaskDetailVo getTaskDetailById(String id) {
        List<PreProductionSampleTaskVo> taskList = getBaseMapper().taskList(new QueryWrapper<PreProductionSampleTask>().eq("t.id", id));
        if (CollUtil.isEmpty(taskList)) {
            return null;
        }
        PreProductionSampleTaskVo task = taskList.get(0);

        PreProductionSampleTaskDetailVo result = new PreProductionSampleTaskDetailVo();
        // 查询任务信息
        PreProductionSampleTaskVo taskVo = BeanUtil.copyProperties(task, PreProductionSampleTaskVo.class);
        // 设置头像
        amcFeignService.setUserAvatarToObj(taskVo);
        // 查询款式设计信息
        StyleVo sampleDesignVo = styleService.getDetail(task.getStyleId());
        // 设置头像
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
        CommonUtils.removeQuery(dto, "samplePic","stylePic");
        PreProductionSampleTask task = getById(dto.getId());
        if (task == null) {
            throw new OtherException("任务不存在");
        }

        // 齐套状态发生改变,修改齐套时间
        if (!task.getKitting().equals(dto.getKitting())) {
            dto.setKittingTime(new Date());
        }
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.eq("id", dto.getId());
        update(dto, uw);

        // 修改裁剪时间和车缝时间
        QueryWrapper<NodeStatus> queryWrapper1 = new BaseQueryWrapper<>();
        queryWrapper1.eq("data_id", dto.getId());
        queryWrapper1.eq("node", "产前样衣任务");
        queryWrapper1.eq("status", "裁剪开始");
        if (dto.getCutterStartTime() != null) {
            NodeStatus nodeStatus1 = new NodeStatus();
            nodeStatus1.setDataId(dto.getId());
            nodeStatus1.setNode("产前样衣任务");
            nodeStatus1.setStatus("裁剪开始");
            nodeStatus1.setStartFlg("1");
            nodeStatus1.setStartDate(dto.getCutterStartTime());
            nodeStatusService.saveOrUpdate(nodeStatus1, queryWrapper1);
        }else {
            nodeStatusService.remove(queryWrapper1);
        }


        QueryWrapper<NodeStatus> queryWrapper2 = new BaseQueryWrapper<>();
        queryWrapper2.eq("data_id", dto.getId());
        queryWrapper2.eq("node", "产前样衣任务");
        queryWrapper2.eq("status", "裁剪完成");
        if (dto.getCutterEndTime()!=null){
            NodeStatus nodeStatus2 = new NodeStatus();
            nodeStatus2.setDataId(dto.getId());
            nodeStatus2.setNode("产前样衣任务");
            nodeStatus2.setStatus("裁剪完成");
            nodeStatus2.setEndFlg("1");
            nodeStatus2.setStartDate(dto.getCutterEndTime());
            nodeStatusService.saveOrUpdate(nodeStatus2, queryWrapper2);
        }else {
            nodeStatusService.remove(queryWrapper2);
        }

        QueryWrapper<NodeStatus> queryWrapper3 = new BaseQueryWrapper<>();
        queryWrapper3.eq("data_id", dto.getId());
        queryWrapper3.eq("node", "产前样衣任务");
        queryWrapper3.eq("status", "车缝进行中");
       if (dto.getStitchStartTime()!=null){
           NodeStatus nodeStatus3 = new NodeStatus();
           nodeStatus3.setDataId(dto.getId());
           nodeStatus3.setNode("产前样衣任务");
           nodeStatus3.setStatus("车缝进行中");
           nodeStatus3.setStartFlg("1");
           nodeStatus3.setStartDate(dto.getStitchStartTime());
           nodeStatusService.saveOrUpdate(nodeStatus3, queryWrapper3);
       }else {
              nodeStatusService.remove(queryWrapper3);
       }

        QueryWrapper<NodeStatus> queryWrapper4 = new BaseQueryWrapper<>();
        queryWrapper4.eq("data_id", dto.getId());
        queryWrapper4.eq("node", "产前样衣任务");
        queryWrapper4.eq("status", "车缝完成");
        if (dto.getStitchEndTime()!=null){
            NodeStatus nodeStatus4 = new NodeStatus();
            nodeStatus4.setDataId(dto.getId());
            nodeStatus4.setNode("产前样衣任务");
            nodeStatus4.setStatus("车缝完成");
            nodeStatus4.setEndFlg("1");
            nodeStatus4.setStartDate(dto.getStitchEndTime());
            nodeStatusService.saveOrUpdate(nodeStatus4, queryWrapper4);
        }else {
            nodeStatusService.remove(queryWrapper4);
        }

        //技术接收时间（工艺接收明细单时间）修改时
        if(dto.getTechReceiveTime() != null){
            //1.款式配色大货款保持一致
            //通过资料包数据拿到 款式配色id
            String packInfoId = task.getPackInfoId();
            PackInfo packInfo = packInfoService.getById(packInfoId);
            //查询出款式配色数据修改 工艺接收明细单时间 字段
            String styleColorId = packInfo.getStyleColorId();

            StyleColor old = styleColorService.getById(styleColorId);

            LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(StyleColor::getTechReceiveTime, dto.getTechReceiveTime())
                    .eq(StyleColor::getId, styleColorId);
            styleColorService.update(updateWrapper);

            StyleColor styleColor1 = styleColorService.getById(styleColorId);

            styleColorService.saveOperaLog("修改", "款式配色", old.getColorName(), old.getStyleNo(), styleColor1, old);

            //2.产前样看板中，相同大货款号的数据也保持一致
            BaseQueryWrapper<PreProductionSampleTask> qw = new BaseQueryWrapper<>();
            qw.eq("p.style_no",old.getStyleNo());
            List<PreProductionSampleTaskVo> preProductionSampleTaskVos = getBaseMapper().taskList(qw);
            List<String> ids = preProductionSampleTaskVos.stream().map(BaseEntity::getId).filter(o->!o.equals(dto.getId())).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(ids)){
                List<PreProductionSampleTask> oldList = listByIds(ids);
                LambdaUpdateWrapper<PreProductionSampleTask> uwf = new LambdaUpdateWrapper<>();
                uwf.set(PreProductionSampleTask::getTechReceiveTime, dto.getTechReceiveTime());
                uwf.in(PreProductionSampleTask::getId, ids);
                update(uwf);
                //记录修改日志
                List<PreProductionSampleTask> newList = listByIds(ids);
                Map<String, PreProductionSampleTask> voMap = newList.stream().collect(Collectors.toMap(BaseEntity::getId, o -> o));
                for (PreProductionSampleTask preProductionSampleTask : oldList) {
                    PreProductionSampleTask preProductionSampleTask1 = voMap.get(preProductionSampleTask.getId());
                    this.saveOperaLog("修改", "产前样看板", preProductionSampleTask1, preProductionSampleTask);
                }
            }
        }

        // 记录日志
        this.saveOperaLog("修改", "产前样看板", dto, task);
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

    /**
     * @param dto
     * @return
     */
    @Override
    public boolean samplePicUpload(SamplePicUploadDto dto) {
        PreProductionSampleTask preProductionSampleTask = baseMapper.selectById(dto.getId());
        preProductionSampleTask.setSamplePic(CommonUtils.removeQuery(dto.getSamplePic()));
        baseMapper.updateById(preProductionSampleTask);
        return true;
    }

    @Override
    public void saveTechReceiveDate(PreProductionSampleTaskDto task) {
        LambdaUpdateWrapper<PreProductionSampleTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PreProductionSampleTask::getTechReceiveDate, task.getTechReceiveDate());
        updateWrapper.set(PreProductionSampleTask::getUpdateId, getUserId());
        updateWrapper.set(PreProductionSampleTask::getUpdateName, getUserName());
        updateWrapper.set(PreProductionSampleTask::getUpdateDate, new Date());
        updateWrapper.eq(PreProductionSampleTask::getId, task.getId());
        update(updateWrapper);
    }

// 自定义方法区 不替换的区域【other_end】

}
