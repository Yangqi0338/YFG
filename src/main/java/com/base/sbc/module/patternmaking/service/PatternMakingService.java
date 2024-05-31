/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumProcessNodeEnum;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.nodestatus.dto.NodestatusPageSearchDto;
import com.base.sbc.module.nodestatus.dto.ResearchProgressPageDto;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类描述：打版管理 service类
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 * @version 1.0
 */
public interface PatternMakingService extends BaseService<PatternMaking> {

/** 自定义方法区 不替换的区域【other_start】 **/



    /**
     * 通过款式设计id 查找
     *
     * @param styleId
     * @return
     */
    List<PatternMakingListVo> findBySampleDesignId(String styleId);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PatternMaking savePatternMaking(PatternMakingDto dto);

    /**
     * 校验制版号是否重复
     *
     * @param id
     * @param patternNo
     */
    void checkPatternNoRepeat(String id, String patternNo);

    /**
     * 款式设计下发
     *
     * @param dto
     * @return
     */
    boolean sampleDesignSend(StyleSendDto dto);

    /**
     * 节点状态改变
     *
     * @param dto
     * @return
     */
    boolean nodeStatusChange(String userId, NodeStatusChangeDto dto, GroupUser groupUser);

    /**
     * 版房主管下发
     * @param dto
     * @return
     */
    boolean prmSend(SetPatternDesignDto dto);

    /**
     * 技术中心看板-任务列表
     * @param dto
     * @return
     */
    PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto);

    void technologyCenterTaskListExcel(HttpServletResponse response, TechnologyCenterTaskSearchDto dto);

    /**
     * 设置版师
     *
     * @param dto
     * @return
     */
    boolean setPatternDesign(SetPatternDesignDto dto);

    /**
     * 获取版师列表
     *
     * @param planningSeasonId
     * @return
     */
    List<PatternDesignVo> getPatternDesignList(String planningSeasonId);

    /**
     * 中断样衣
     *
     * @param id
     * @param flag
     * @return
     */
    boolean breakOffSample(String id, String flag);

    /**
     * 中断打版
     *
     * @param id
     * @return
     */
    boolean breakOffPattern(String id, String flag);

    /**
     * 批量下发
     *
     * @param dtos
     * @return
     */

    Integer prmSendBatch(List<SetPatternDesignDto> dtos);

    /**
     * 设置版师批量
     *
     * @param dto
     * @return
     */
    boolean setPatternDesignBatch(List<SetPatternDesignDto> dto);

    /**
     * 打样任务 列表
     *
     * @param dto
     * @return
     */
    PageInfo<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto);

    /**
     * 设置顺序
     *
     * @param dtoList
     * @return
     */
    Integer setSort(List<SetSortDto> dtoList);

    /**
     * 挂起
     *
     * @param dto
     * @return
     */
    boolean suspend(SuspendDto dto);

    /**
     * 取消挂起
     *
     * @param id
     * @return
     */
    boolean cancelSuspend(String id);


    /**
     * 设置齐套
     *
     * @param p
     * @param dto
     * @return
     */
    boolean setKitting(String p, SetKittingDto dto);

    /**
     * 查询明细
     *
     * @param id
     * @return
     */
    StylePmDetailVo getDetailById(String id);

    /**
     * 保存附件
     *
     * @param dtos
     * @return
     */
    boolean saveAttachment(SaveAttachmentDto dto);

    /**
     * 类描述：打版进度列表
     *
     * @address com.base.sbc.module.patternmaking.service.PatternMakingService
     * @author lixianglin
     * @email li_xianglin@126.com
     * @date 创建时间：2023-06-12 11:37
     * @version 1.0
     */
    PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto);
    /**
     * 类描述：研发总进度
     *
     * @address com.base.sbc.module.patternmaking.service.PatternMakingService
     * @author lxf
     * @email 123456789@126.com
     * @date 创建时间：2023-09-12 11:37
     * @version 1.0
     */
    PageInfo<NodeListVo> allProgressSteps(NodestatusPageSearchDto dto, String userCompany);

    /**
     * 研发总进度看板 new
     * @param dto
     * @param userCompany
     * @return
     */
    PageInfo<StyleResearchProcessVo> researchProcessList(ResearchProgressPageDto dto, String userCompany);

    /**
     * 获取各个节点的完成时间
     * @param stylelId
     * @param styleColorId
     * @param basicsdatumProcessNodeEnum
     * @return
     */
    Date getNodeFinashTime(String stylelId,String styleColorId,BasicsdatumProcessNodeEnum basicsdatumProcessNodeEnum,StyleResearchNodeVo styleResearchNodeVo,Date preFinishDate);
    /**
     * 类描述：打版进度列表，工作台上面的
     *
     * @address com.base.sbc.module.patternmaking.service.PatternMakingService
     * @author lixianglin
     * @email li_xianglin@126.com
     * @date 创建时间：2023-06-12 11:37
     * @version 1.0
     */
    Map patternMakingSteps0(String userCompany);

    /**
     * 状态改版批量
     *
     * @param list
     * @return
     */
    boolean nodeStatusChange(String userId, List<NodeStatusChangeDto> list, GroupUser groupUser);

    PatternMakingCommonPageSearchVo sampleBoardList(PatternMakingCommonPageSearchDto dto);

    /**
     * 导出样衣看板
     * @param response
     * @param dto
     */
    void deriveExcel(HttpServletResponse response, PatternMakingCommonPageSearchDto dto) throws IOException, InterruptedException;

    /**
     * 确认收到样衣
     *
     * @param id 打版id
     * @return
     */
    boolean receiveSample(String id);



    List prmDataOverview(String time);

    JSONObject getNodeStatusConfig(GroupUser user, String node, String status, String dataId);

    boolean assignmentUser(GroupUser groupUser, AssignmentUserDto dto);

    List<PatternDesignVo> pdTaskDetail(String companyCode);

    PageInfo queryPageInfo(PatternMakingCommonPageSearchDto dto);


    /**
     * 根据时间按周月统计版类对比
     * @param patternMakingWeekMonthViewDto
     * @param token token
     * @return 根据周返回集合
     */
    ArrayList<ArrayList> versionComparisonViewWeekMonth(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String token);


    /**
     * 品类汇总统计
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token token
     * @return 结果集
     */
    ArrayList<ArrayList> categorySummaryCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String token);

    /**
     * 根据时间按周月 统计样衣产能总数
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token token
     * @return 返回集合数据
     */
    ArrayList<ArrayList> sampleCapacityTotalCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token);

    /**
     * 产能对比统计
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token                         token
     * @return 返回集合数据
     */
    ArrayList<ArrayList> capacityContrastStatistics(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token);

    void sort(PatternMaking pm, boolean excludeSelf);

    boolean nextOrPrev(Principal user, String id, String np);

    void checkBreak(PatternMaking pm);

    boolean patternMakingScore(Principal user, String id, BigDecimal score);

    boolean sampleMakingScore(Principal user, String id, BigDecimal score);

    boolean setSampleBarCode(SetSampleBarCodeDto dto);

    /**
     * 获取车缝工列表
     *
     * @param planningSeasonId
     * @return
     */
    List<PatternDesignVo> getStitcherList(String planningSeasonId);

    boolean patternMakingQualityScore(Principal user, String id, BigDecimal score);

    boolean sampleMakingQualityScore(Principal user, String id, BigDecimal score);

    /**
     * 样衣工编辑
     * @param user
     * @param dto
     * @return
     */
    boolean sampleMakingEdit(Principal user,PatternMakingDto dto);

    /**
     * 获取所有版师列表
     *
     * @param companyCode
     * @return
     */
    List<SampleUserVo> getAllPatternDesignList(PatternUserSearchVo vo);

    List<SampleUserVo> getAllCutterList(PatternUserSearchVo vo);

    List<SampleUserVo> getAllStitcherList(PatternUserSearchVo vo);

    boolean finish(String id);

    /**
     * 上传样衣图
     *
     * @param dto
     * @return
     */
    boolean samplePicUpload(SamplePicUploadDto dto);

    /**
     * 校验打样顺序是否重复
     *
     * @param styleId
     * @param patternMakingId
     * @param patSeq
     */
    void checkPatSeqRepeat(String styleId, String patternMakingId, String patSeq);

    /**
     * 打板停用启用
     * @param startStopDto
     * @return
     */
    boolean startStop(StartStopDto startStopDto);

    void saveReceiveReason(TechnologyCenterTaskVo dto);

    List<SampleUserVo> getAllPatternDesignerList(PatternUserSearchVo vo);

    /**
     * 获取款下面的初版车缝工和上次车缝工
     * @param styleId
     * @return
     */
    Map<String,String> getHeadLastTimeStitcher(String styleId);

    /**
     * 获取到设计款下面的样衣
     * @param styleId
     * @return
     */
    List<PatternMakingVo> getSampleDressBydesignNo(String styleId);

    /**
     * 是否参考样衣
     * @return
     */
    Boolean updateReferSample(PatternMakingReferSampleDto dto);

/** 自定义方法区 不替换的区域【other_end】 **/


}

