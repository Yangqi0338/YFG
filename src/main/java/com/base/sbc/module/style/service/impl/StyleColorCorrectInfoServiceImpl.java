/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.style.dto.AddRevampStyleColorDto;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.mapper.StyleColorCorrectInfoMapper;
import com.base.sbc.module.style.service.StyleColorCorrectInfoService;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Override
    public PageInfo<StyleColorCorrectInfoVo> findList(QueryStyleColorCorrectDto page) {
        /*分页*/
        Page<StyleColorCorrectInfoVo> objects = PageHelper.startPage(page);
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.andLike(page.getSearch(), "ts.design_no", "tsc.style_no");
        queryWrapper.notEmptyEq("ts.planning_season_id", page.getPlanningSeasonId());
        queryWrapper.notEmptyEq("ts.prod_category", page.getProdCategory());
        queryWrapper.notEmptyEq("ts.devt_type_name", page.getDevtTypeName());
        if(StrUtil.isNotBlank(page.getDesigner())){
            queryWrapper.in("ts.designer", StringUtils.convertList(page.getDesigner()));
        }
        queryWrapper.notEmptyEq("ts.task_level_name", page.getTaskLevelName());
        queryWrapper.like(StringUtils.isNotBlank(page.getTechnicianName()), "ts.technician_name", page.getTechnicianName());
        queryWrapper.eq(StringUtils.isNotBlank(page.getBomStatus()), "tsc.bom_status", page.getBomStatus());
        if (StringUtils.isNotBlank(page.getTechnicsDate())) {
            queryWrapper.between("tcci.technics_date", page.getTechnicsDate().split(","));
        }
        if(StrUtil.equals(page.getTechnicsDateNullFlag(), BaseGlobal.IN)){
            queryWrapper.isNull("tcci.technics_date");
        }
        if(StrUtil.equals(page.getTechnicsDateNullFlag(),BaseGlobal.YES)){
            queryWrapper.isNotNull("tcci.technics_date");
        }
        queryWrapper.notEmptyEq("tsc.id", page.getStyleColorId());
        queryWrapper.notExists("select 1 from t_style_color_correct_info t1 WHERE t1.style_color_id = tsc.id AND t1.del_flag = '1'");
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

        //修改产前样看板的工艺确认时间
        /*if(StrUtil.isNotBlank(styleColorCorrectInfo.getProductionSampleId())){
            PreProductionSampleTaskDto task = new PreProductionSampleTaskDto();
            task.setId(styleColorCorrectInfo.getProductionSampleId());
            task.setTechReceiveDate(styleColorCorrectInfo.getTechnicsDate());
            preProductionSampleTaskService.saveTechReceiveDate(task);
            //如果没有关联到  则保存在原始表中
            styleColorCorrectInfo.setTechnicsDate(null);
        }*/

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
}
