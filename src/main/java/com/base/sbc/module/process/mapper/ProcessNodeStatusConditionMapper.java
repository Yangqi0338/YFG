/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.process.entity.ProcessNodeStatusCondition;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 类描述：流程配置-节点状态条件 dao类
 * @address com.base.sbc.module.process.dao.ProcessNodeStatusConditionDao
 * @author mengfanjiang  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 17:10:23 
 * @version 1.0  
 */
@Mapper
public interface ProcessNodeStatusConditionMapper extends BaseMapper<ProcessNodeStatusCondition> {
// 自定义方法区 不替换的区域【other_start】

    /*获取节点状态下的所有条件*/
  ProcessNodeStatusConditionVo  getCondition(@Param(Constants.WRAPPER) QueryWrapper qc);

// 自定义方法区 不替换的区域【other_end】
}