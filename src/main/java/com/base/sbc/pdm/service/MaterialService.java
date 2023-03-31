/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.pdm.dto.MaterialDto;
import com.base.sbc.pdm.entity.MaterialCollect;
import com.base.sbc.pdm.entity.MaterialLabel;
import com.base.sbc.pdm.mapper.MaterialMapper;
import com.base.sbc.pdm.dao.MaterialAllDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.dao.MaterialDao;

import javax.annotation.Resource;
import java.util.*;

/**
 * 类描述：素材库 service类
 *
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-3-24 16:26:15
 */
@Service
@Transactional(readOnly = true)
public class MaterialService extends BaseService<Material> {

    @Autowired
    private MaterialDao materialDao;

    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private UserUtils userUtils;

    @Resource
    private AmcService amcService;

    @Resource
    private MaterialLabelService materialLabelService;

    @Resource
    private MaterialCollectService materialCollectService;

    @Override
    protected BaseDao<Material> getEntityDao() {
        return materialDao;
    }


    /**
     * 为了解决太多表关联查询太慢的问题
     * 相关联的表，生成查询条件
     */
    private void addQuery(MaterialAllDto materialAllDto) {
        HashSet<String> collectSet = null;
        HashSet<String> labelSet = null;
        //我的收藏筛选条件
        if (BasicNumber.ONE.getNumber().equals(materialAllDto.getCollectId())) {
            collectSet = new HashSet<>();
            QueryCondition qc = new QueryCondition();
            qc.andEqualTo("user_id", userUtils.getUserId());
            qc.andEqualTo("del_flag", 0);
            List<MaterialCollect> materialCollects = materialCollectService.findByCondition(qc);
            for (MaterialCollect materialCollect : materialCollects) {
                collectSet.add(materialCollect.getMaterialId());
            }
        }

        //标签筛选条件
        if (materialAllDto.getLabelIds() != null && materialAllDto.getLabelIds().length > 0) {
            labelSet = new HashSet<>();
            List<MaterialLabel> materialLabels = materialLabelService.getByLabelIds(Arrays.asList(materialAllDto.getLabelIds()));
            for (MaterialLabel materialLabel : materialLabels) {
                labelSet.add(materialLabel.getMaterialId());
            }
        }
        //尺码 筛选条件
        //颜色筛选条件


        //如果有集合不为null，则说明有筛选条件
        if (collectSet != null || labelSet != null) {
            //取所有条件相交的
            Set<String> ids = CollectionUtil.intersectionDistinct(collectSet, labelSet);
            if (ids==null || ids.size()==0){
                //说明无筛选结果，给个0让查询不到数据
                ids=new HashSet<>();
                ids.add("0");
            }
            materialAllDto.setIds(new ArrayList<>(ids));
        }

    }

    /**
     * 条件查询
     *
     * @param token          用户的登录凭证
     * @param materialAllDto 请求封装对象
     * @param page           分页参数
     * @return 返回的封装对象
     */
    public PageInfo<MaterialDto> listQuery(String token, MaterialAllDto materialAllDto, Page page) {
        this.addQuery(materialAllDto);


        PageHelper.startPage(page);
        List<MaterialAllDto> materialAllDtos = materialMapper.listQuery(materialAllDto);

        if (materialAllDtos == null || materialAllDtos.size() == 0) {
            return new PageInfo<>();
        }
        List<MaterialDto> list = new ArrayList<>();

        List<String> userIds = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        for (MaterialAllDto allDto : materialAllDtos) {
            ids.add(allDto.getId());
            userIds.add(allDto.getCreateId());
        }


        //查询关联标签
        List<MaterialLabel> materialLabelList = materialLabelService.getByMaterialIds(ids);
        //查询关联尺码信息

        //查询关联颜色信息


        //远程获取用户部门信息
        String str = amcService.getDeptList(token, userIds.toArray(new String[0]));
        JSONObject jsonObject = JSONObject.parseObject(str);
        List<JSONObject> data = jsonObject.getList("data", JSONObject.class);

        for (MaterialAllDto allDto : materialAllDtos) {
            for (JSONObject json : data) {
                if (allDto.getCreateId().equals(json.getString("userId"))) {
                    allDto.setDeptName(json.getString("deptName"));
                }
            }

            //标签放入对象
            List<MaterialLabel> labels = new ArrayList<>();
            for (MaterialLabel materialLabel : materialLabelList) {
                if (allDto.getId().equals(materialLabel.getMaterialId())) {
                    labels.add(materialLabel);
                }
            }
            allDto.setLabels(labels);

            MaterialDto materialDto = new MaterialDto();
            materialDto.setMaterialDetails(allDto.toMaterialDetails());
            materialDto.setMaterial(allDto.toMaterial());
            list.add(materialDto);
        }
        return new PageInfo<>(list);
    }


}
