/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.process.dto.AddRevampProcessNodeDto;
import com.base.sbc.module.process.entity.ProcessNode;
import com.base.sbc.module.process.mapper.ProcessNodeMapper;
import com.base.sbc.module.process.service.ProcessNodeService;
import com.base.sbc.module.process.vo.ProcessNodeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-节点表 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.process.service.ProcessNodeService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:14
 */
@Service
public class ProcessNodeServiceImpl extends BaseServiceImpl<ProcessNodeMapper, ProcessNode> implements ProcessNodeService {

    @Autowired
    private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 流程配置-节点表分页查询
     *
     * @param processSchemeId
     * @return
     */
    @Override
    public List<ProcessNodeVo> getProcessNodeList(String processSchemeId) {
        QueryWrapper<ProcessNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("process_scheme_id", processSchemeId);
        queryWrapper.orderByAsc("sort");
        /*查询流程配置-节点表数据*/
        List<ProcessNode> processNodeList = baseMapper.selectList(queryWrapper);
        /*转换vo*/
        List<ProcessNodeVo> list = BeanUtil.copyToList(processNodeList, ProcessNodeVo.class);
        return list;
    }


    /**
     * 方法描述：新增修改流程配置-节点表
     *
     * @param addRevampProcessNodeDto 流程配置-节点表Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampProcessNode(AddRevampProcessNodeDto addRevampProcessNodeDto) {
        ProcessNode processNode = new ProcessNode();
        if (StringUtils.isEmpty(addRevampProcessNodeDto.getId())) {
            /*查看是否重复*/
            QueryWrapper<ProcessNode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.eq("process_scheme_id", addRevampProcessNodeDto.getProcessSchemeId());
            queryWrapper.eq("node_name", addRevampProcessNodeDto.getNodeName());
            List<ProcessNode> processNodeList = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(processNodeList)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*后节点排序加1*/
            updateSort(addRevampProcessNodeDto.getProcessSchemeId(),addRevampProcessNodeDto.getSort(),BaseGlobal.STATUS_NORMAL);
            /*新增*/
            BeanUtils.copyProperties(addRevampProcessNodeDto, processNode);
            processNode.setCompanyCode(baseController.getUserCompany());
            if(addRevampProcessNodeDto.getIsNodeAdd().equals(BaseGlobal.STATUS_NORMAL)){
                processNode.setSort( processNode.getSort()+BaseGlobal.ONE);
            }
            processNode.insertInit();
            baseMapper.insert(processNode);
        } else {
            /*修改*/
            processNode = baseMapper.selectById(addRevampProcessNodeDto.getId());
            if (ObjectUtils.isEmpty(processNode)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampProcessNodeDto, processNode);
            processNode.updateInit();
            baseMapper.updateById(processNode);
        }
        return true;
    }


    /**
     * 描述-修改该节点后的顺序字段
     * @param processSchemeId
     * @param sort
     * @param adSubtract 0加 1减
     */
    void updateSort(String processSchemeId,Integer sort,String adSubtract){
        QueryWrapper<ProcessNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.clear();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("process_scheme_id", processSchemeId);
        queryWrapper.ge("sort", sort + BaseGlobal.ONE);
        List<ProcessNode> processNodeList = baseMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(processNodeList)){
            if (adSubtract.equals(BaseGlobal.STATUS_NORMAL)){
                processNodeList.forEach(p -> p.setSort(p.getSort() + BaseGlobal.ONE));
            }else {
                processNodeList.forEach(p -> p.setSort(p.getSort() - BaseGlobal.ONE));
            }
            updateBatchById(processNodeList);
        }
    }

    /**
     * 方法描述：删除流程配置-节点表
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delProcessNode(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*调整删除的顺序*/
        ids.forEach(i ->{
            ProcessNode processNode = baseMapper.selectById(i);
            /*后节点排序减1*/
            updateSort(processNode.getProcessSchemeId(),processNode.getSort(),BaseGlobal.STATUS_CLOSE);
        });
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopProcessNode(StartStopDto startStopDto) {
        UpdateWrapper<ProcessNode> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
