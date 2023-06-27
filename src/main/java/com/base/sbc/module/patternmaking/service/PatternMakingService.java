/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：打版管理 service类
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 * @version 1.0  
 */
public interface PatternMakingService extends IServicePlus<PatternMaking>{

/** 自定义方法区 不替换的区域【other_start】 **/



    /**
     * 通过样衣设计id 查找
     *
     * @param sampleDesignId
     * @return
     */
    List<PatternMakingListVo> findBySampleDesignId(String sampleDesignId);

    /**
     * 保存
     * @param dto
     * @return
     */
    PatternMaking savePatternMaking(PatternMakingDto dto);

    /**
     * 样衣设计下发
     * @param dto
     * @return
     */
    boolean sampleDesignSend(SampleDesignSendDto dto);

    /**
     * 节点状态改变
     *
     * @param dto
     * @return
     */
    boolean nodeStatusChange(NodeStatusChangeDto dto, GroupUser groupUser);

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
     * @return
     */
    boolean breakOffSample(String id);

    /**
     * 中断打版
     *
     * @param id
     * @return
     */
    boolean breakOffPattern(String id);

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
    List<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto);

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
    SampleDesignPmDetailVo getDetailById(String id);

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
     * 状态改版批量
     *
     * @param list
     * @return
     */
    boolean nodeStatusChange(List<NodeStatusChangeDto> list, GroupUser groupUser);

    PageInfo<SampleBoardVo> sampleBoardList(PatternMakingCommonPageSearchDto dto);

    /**
     * 确认收到样衣
     *
     * @param id 打版id
     * @return
     */
    boolean receiveSample(String id);

    /**
     * 获取所有版师列表
     *
     * @param companyCode
     * @return
     */
    List<SampleUserVo> getAllPatternDesignList(String companyCode);

    List prmDataOverview(String time);

    JSONObject getNodeStatusConfig(String node, String status);

    boolean assignmentUser(GroupUser groupUser,AssignmentUserDto dto);

    List<PatternDesignVo> pdTaskDetail(String companyCode);

    PageInfo queryPageInfo(PatternMakingCommonPageSearchDto dto);

/** 自定义方法区 不替换的区域【other_end】 **/


}

