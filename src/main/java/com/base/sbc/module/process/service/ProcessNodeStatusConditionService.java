/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import com.base.sbc.module.process.entity.ProcessNodeStatusCondition;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusConditionDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;

/** 
 * 类描述：流程配置-节点状态条件 service类
 * @address com.base.sbc.module.process.service.ProcessNodeStatusConditionService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 17:10:23
 * @version 1.0  
 */
public interface ProcessNodeStatusConditionService extends BaseService<ProcessNodeStatusCondition> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<ProcessNodeStatusConditionVo> getProcessNodeStatusConditionList(QueryDto queryDto);




        /**
        * 方法描述：新增修改流程配置-节点状态条件
        *
        * @param addRevampProcessNodeStatusConditionDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampProcessNodeStatusCondition(AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto);



        /**
        * 方法描述：删除流程配置-节点状态条件
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delProcessNodeStatusCondition(String id);


        /**
         * 方法描述：查询明细
         *
         * @param addRevampProcessNodeStatusConditionDto
         * @return boolean
         */
        ProcessNodeStatusConditionVo getNodeStatusConditionDetail(AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto);


        /**
        * 方法描述：启用停止流程配置-节点状态条件
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopProcessNodeStatusCondition( StartStopDto startStopDto);


// 自定义方法区 不替换的区域【other_end】

	
}
