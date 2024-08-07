/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.mapper.PreProductionSampleTaskMapper;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.TagConfirmDateDto;
import com.base.sbc.module.style.dto.AddRevampStyleColorDto;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.mapper.StyleColorCorrectInfoMapper;
import com.base.sbc.module.style.service.StyleColorCorrectInfoService;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoExcel;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * 类描述：正确样管理 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.StyleColorCorrectInfoService
 * @email your email
 * @date 创建时间：2023-12-26 10:13:31
 */
@Service
public class StyleColorCorrectInfoServiceImpl extends BaseServiceImpl<StyleColorCorrectInfoMapper, StyleColorCorrectInfo> implements StyleColorCorrectInfoService {

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private PreProductionSampleTaskService preProductionSampleTaskService;

    @Autowired
    private PreProductionSampleTaskMapper preProductionSampleTaskMapper;
    @Lazy
    @Autowired
    private SmpService smpService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    public PageInfo<StyleColorCorrectInfoVo> findList(QueryStyleColorCorrectDto page) {
        /*分页*/
        Page<StyleColorCorrectInfoVo> objects = PageHelper.startPage(page);
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.andLike(page.getSearch(), "ts.design_no", "tsc.style_no");
        queryWrapper.notEmptyEq("ts.pattern_design_id", page.getPlanningSeasonId());
        queryWrapper.notEmptyEq("ts.planning_season_id", page.getPlanningSeasonId());
        queryWrapper.notEmptyEq("ts.prod_category", page.getProdCategory());
        queryWrapper.notEmptyEq("tsc.devt_type", page.getDevtTypeName());
        if(StrUtil.isNotBlank(page.getDesigner())){
            queryWrapper.likeList("ts.designer", StringUtils.convertList(page.getDesigner()));
        }
        queryWrapper.notEmptyEq("ts.task_level_name", page.getTaskLevelName());
        queryWrapper.like(StringUtils.isNotBlank(page.getTechnicianName()), "ts.technician_name", page.getTechnicianName());
        queryWrapper.eq(StringUtils.isNotBlank(page.getBomStatus()), "tsc.bom_status", page.getBomStatus());
        queryWrapper.dateBetweenOrIsNull("tsc.design_detail_date",page.getDesignDetailDate(),page.getDesignDetailDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tsc.design_correct_date",page.getDesignCorrectDate(),page.getDesignCorrectDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tsc.tech_receive_time",page.getTechnicsDate(),page.getTechnicsDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.technology_correct_date",page.getTechnologyCorrectDate(),page.getTechnologyCorrectDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.technology_check_date",page.getTechnologyCheckDate(),page.getTechnologyCheckDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.plan_control_date",page.getPlanControlDate(),page.getPlanControlDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.purchase_need_date",page.getPurchaseNeedDate(),page.getPurchaseNeedDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.purchase_recover_date",page.getPurchaseRecoverDate(),page.getPurchaseRecoverDateNullFlag());
        queryWrapper.dateBetweenOrIsNull("tcci.auxiliary_date",page.getAuxiliaryDate(),page.getAuxiliaryDateNullFlag());

        queryWrapper.notEmptyEq("tsc.id", page.getStyleColorId());
        queryWrapper.notExists("select 1 from t_style_color_correct_info t1 WHERE t1.style_color_id = tsc.id AND t1.del_flag = '1'");
        queryWrapper.eq("tsc.del_flag","0");

        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.style_color_correct_info.getK());

        List<StyleColorCorrectInfoVo> infoVoList = baseMapper.findList(queryWrapper);

        /*查询款式图*/
        stylePicUtils.setStylePic(infoVoList, "stylePic");

        return new PageInfo<>(infoVoList);
    }

    public StyleColorCorrectInfo findById(String id){
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tcci.id", id);
        queryWrapper.notExists("select 1 from t_style_color_correct_info t1 WHERE t1.style_color_id = tsc.id AND t1.del_flag = '1'");
        List<StyleColorCorrectInfoVo> infoVoList = baseMapper.findList(queryWrapper);
        StyleColorCorrectInfoVo styleColorCorrectInfoVo = infoVoList.get(0);
        return BeanUtil.copyProperties(styleColorCorrectInfoVo,StyleColorCorrectInfo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMain(StyleColorCorrectInfo styleColorCorrectInfo) {
        //改为前端提示时间准确性
        //时间有效性判断
        /*Date A = styleColorCorrectInfo.getDesignCorrectDate();
        Date B = styleColorCorrectInfo.getDesignDetailDate();
        Date C = styleColorCorrectInfo.getTechnologyCorrectDate();
        Date D = styleColorCorrectInfo.getTechnologyCheckDate();
        Date E = styleColorCorrectInfo.getTechnicsDate();
        Date F = styleColorCorrectInfo.getGstDate();
        Date G = styleColorCorrectInfo.getPlanControlDate();
        Date H = styleColorCorrectInfo.getPurchaseNeedDate();
        Date I = styleColorCorrectInfo.getPurchaseRecoverDate();
        Date J = styleColorCorrectInfo.getAuxiliaryDate();

        //AB取最晚时间
        Date AB = null;
        if (B != null) {
            if (A != null) {
                AB = A.before(B) ? B : A;
            } else {
                AB = B;
            }
            if(C == null && D == null && E != null){
                //此时E要晚于B 1小时

            }

            if(G != null){
                //此时G要晚于B 1小时
            }
        }else if (A != null){
            AB = A;
        }
        //C要晚于 AB 1小时
        if(getBetweenHour(AB, C) < 1){

        }
        //G要晚于 AB 1小时
        if(getBetweenHour(AB, C) < 1){

        }
        if(C != null && D == null && E != null){
            //此时E要晚于C 2小时
        }
        if(D != null && E != null){
            //此时E要晚于D 1小时
        }
        if(G != null && H == null && I == null && J == null){
            //此时J要晚于G 3小时
        }


        //技术接受正确样日期校验-设计下正确样时间不为空时，要晚于其1分钟
        if(getBetweenHour(A,C) < 1){
            throw new RuntimeException("技术接受正确样日期 应该晚于 设计下正确样时间 1小时以上");
        }
        //技术部查版完成日期校验-技术接受正确样日期不为空时，要晚于其1小时
        if(getBetweenHour(C,D) < 1){
            throw new RuntimeException("技术部查版完成日期 应该晚于 技术接受正确样日期 1小时以上");
        }
        //工艺部接收日期校验-1.如果技术部查版完成日期部位空，要晚于其1小时 2.如果前一个为空，设计下明细单不为空时，要晚于其1小时
        if(getBetweenHour(D,E) < 1){
            throw new RuntimeException("工艺部接收日期 应该晚于 技术部查版完成日期 1小时以上");
        }
        if(D == null){
            if(getBetweenHour(B,E) < 1){
                throw new RuntimeException("工艺部接收日期 应该晚于 设计下明细单 1小时以上");
            }
        }
        //计控接明细单日期校验-设计下明细单不为空时，要晚于其1小时
        if(getBetweenHour(B,G) < 1){
            throw new RuntimeException("计控接明细单日期 应该晚于 设计下明细单 1小时以上");
        }
        //采购需求日期校验-计控接明细单不为空时，要晚于其1小时
        if(getBetweenHour(G,H) < 1){
            throw new RuntimeException("计控接明细单日期 应该晚于 设计下明细单 1小时以上");
        }
        //采购回复货期校验-采购需求不为空时，要晚于其1小时
        if(getBetweenHour(H,I) < 1){
            throw new RuntimeException("计控接明细单日期 应该晚于 设计下明细单 1小时以上");
        }
        //辅仓接收日期校验-采购回复货期不为空时，要晚于其1小时
        if(getBetweenHour(I,J) < 1){
            throw new RuntimeException("计控接明细单日期 应该晚于 设计下明细单 1小时以上");
        }*/

        StyleColorCorrectInfo oldDto = new StyleColorCorrectInfo();
        if(StrUtil.isNotBlank(styleColorCorrectInfo.getId())){
            styleColorCorrectInfo.updateInit();
            oldDto = findById(styleColorCorrectInfo.getId());
        }else{
            styleColorCorrectInfo.insertInit();
        }

        //修改产前样看板的工艺确认时间 和款式配色的时间
        //1.款式配色大货款保持一致
        //款式配色数据修改 工艺接收明细单时间 字段
        String styleColorId = styleColorCorrectInfo.getStyleColorId();

        StyleColor old = styleColorService.getById(styleColorId);

        LambdaUpdateWrapper<StyleColor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StyleColor::getTechReceiveTime, styleColorCorrectInfo.getTechnicsDate())
                .eq(StyleColor::getId, styleColorId);
        styleColorService.update(updateWrapper);

        StyleColor styleColor1 = styleColorService.getById(styleColorId);

        styleColorService.saveOperaLog("修改", "款式配色", old.getColorName(), old.getStyleNo(), styleColor1, old);

        //2.产前样看板中，相同大货款号的数据也保持一致
        BaseQueryWrapper<PreProductionSampleTask> qw = new BaseQueryWrapper<>();
        qw.eq("p.style_no", old.getStyleNo());
        List<PreProductionSampleTaskVo> preProductionSampleTaskVos = preProductionSampleTaskMapper.taskList(qw);
        List<String> ids = preProductionSampleTaskVos.stream().map(BaseEntity::getId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)) {
            List<PreProductionSampleTask> oldList = preProductionSampleTaskService.listByIds(ids);
            LambdaUpdateWrapper<PreProductionSampleTask> uwf = new LambdaUpdateWrapper<>();
            uwf.set(PreProductionSampleTask::getTechReceiveTime, styleColorCorrectInfo.getTechnicsDate());
            uwf.in(PreProductionSampleTask::getId, ids);
            preProductionSampleTaskService.update(uwf);
            //记录修改日志
            List<PreProductionSampleTask> newList = preProductionSampleTaskService.listByIds(ids);
            Map<String, PreProductionSampleTask> voMap = newList.stream().collect(Collectors.toMap(BaseEntity::getId, o -> o));
            for (PreProductionSampleTask preProductionSampleTask : oldList) {
                PreProductionSampleTask preProductionSampleTask1 = voMap.get(preProductionSampleTask.getId());
                preProductionSampleTaskService.saveOperaLog("修改", "产前样看板", preProductionSampleTask1, preProductionSampleTask);
            }
        }

        TagConfirmDateDto technicsConfirmDateDto = new TagConfirmDateDto();
        technicsConfirmDateDto.setStyleNo(styleColorCorrectInfo.getStyleNo());
        technicsConfirmDateDto.setTechnicsDate(styleColorCorrectInfo.getTechnicsDate());
        technicsConfirmDateDto.setType("technics_date");
        smpService.styleColorCorrectInfoDate(technicsConfirmDateDto);

        styleColorCorrectInfo.setTechnicsDate(null);
        oldDto.setTechnicsDate(null);



        //region 下发scm
        boolean sendFlag = false;
        if (styleColorCorrectInfo.getPlanControlDate() == null && oldDto.getPlanControlDate() == null) {
            sendFlag = false;
        } else if (styleColorCorrectInfo.getPlanControlDate() == null && oldDto.getPlanControlDate() != null) {
            sendFlag = true;
        } else if (styleColorCorrectInfo.getPlanControlDate() != null && oldDto.getPlanControlDate() == null) {
            sendFlag = true;
        } else if (!DateUtil.isSameTime(styleColorCorrectInfo.getPlanControlDate(), oldDto.getPlanControlDate())) {
            sendFlag = true;
        }

        if (sendFlag) {
            TagConfirmDateDto planConfirmDateDto = new TagConfirmDateDto();
            planConfirmDateDto.setStyleNo(styleColorCorrectInfo.getStyleNo());
            planConfirmDateDto.setPlanControlDate(styleColorCorrectInfo.getPlanControlDate());
            planConfirmDateDto.setType("plan_control_date");
            smpService.styleColorCorrectInfoDate(planConfirmDateDto);
        }
        //endregion

        //修改款式配色的设计 时间
        AddRevampStyleColorDto styleColor = new AddRevampStyleColorDto();
        styleColor.setId(styleColorCorrectInfo.getStyleColorId());
        styleColor.setDesignCorrectDate(styleColorCorrectInfo.getDesignCorrectDate());
        styleColor.setDesignDetailDate(styleColorCorrectInfo.getDesignDetailDate());
        styleColorService.saveDesignDate(styleColor);

        //修改记录
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName("正确样");
        operaLogEntity.setType("修改");
        operaLogEntity.setDocumentId(styleColorCorrectInfo.getId());
        operaLogEntity.setDocumentCode(styleColorCorrectInfo.getStyleNo());
        operaLogEntity.setDocumentName("");
        operaLogEntity.setParentId(styleColorCorrectInfo.getId());
        saveOrUpdateOperaLog(styleColorCorrectInfo,oldDto,operaLogEntity);

        //不保存 字段，分别保存在原始表中，关联查询
        styleColorCorrectInfo.setDesignCorrectDate(null);
        styleColorCorrectInfo.setDesignDetailDate(null);
        saveOrUpdate(styleColorCorrectInfo);
        return styleColorCorrectInfo.getId();
    }

    private static long getBetweenHour(Date x, Date y) {
        if(x == null || y == null){
            return 1;
        }
        return DateUtil.between(x, y, DateUnit.HOUR, false);
    }

    @Override
    @Transactional
    public void deleteMain(StyleColorCorrectInfo styleColorCorrectInfo) {
        String id = saveMain(styleColorCorrectInfo);
        removeById(id);
    }

    @Override
    public void deriveExcel(HttpServletResponse response, QueryStyleColorCorrectDto dto) {
        dto.setExcelFlag(BaseGlobal.YES);
        List<StyleColorCorrectInfoVo> infoVoList = findList(dto).getList();
        List<StyleColorCorrectInfoExcel> list = BeanUtil.copyToList(infoVoList, StyleColorCorrectInfoExcel.class);

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                .build();

        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(infoVoList) && infoVoList.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                stylePicUtils.setStylePic(list, "stylePic",30);
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (StyleColorCorrectInfoExcel excel : list) {
                    executor.submit(() -> {
                        try {
                            final String stylePic = excel.getStylePic();
                            excel.setStylePic1(HttpUtil.downloadBytes(stylePic));
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
            ExcelUtils.exportExcel(list, StyleColorCorrectInfoExcel.class, "正确样跟踪.xlsx", new ExportParams("正确样跟踪", "正确样跟踪", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
