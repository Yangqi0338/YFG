package com.base.sbc.module.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/4/13 11:50:06
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements BaseService<T> {

    @Resource
    private UserUtils userUtils;
    @Resource
    private OperaLogService operaLogService;

    public static final String COMPANY_CODE = "company_code";
    public static final String DEL_FLAG = "del_flag";

    /**
     * 获取企业编码
     *
     * @return
     */
    public String getCompanyCode() {
        return userUtils.getCompanyCode();
    }

    public String getUserId(){
        return userUtils.getUserCompany().getUserId();
    }
    /**
     * 批量提交修改，逻辑删除新增修改
     * @param entityList 实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids=new ArrayList<>();
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
            }
        }
        queryWrapper.eq("company_code",companyCode);
        //逻辑删除传进来不存在的
        if (ids.size() > 0) {
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        //新增
        this.saveBatch(addList);
        //修改
        this.updateBatchById(updateList);

        return entityList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper, OperaLogEntity log) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids = new ArrayList<>();
        List<T> dbList = list(queryWrapper);
        JSONObject fieldJson = CommonUtils.getFieldJson(this.entityClass);
        Map<String, T> dbMaps = Optional.ofNullable(dbList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
        Set<String> dbIds = dbMaps.keySet();
        List<String> ustrList = new ArrayList<>();
        List<String> istrList = new ArrayList<>();
        List<String> dstrList = new ArrayList<>();
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
                istrList.add(CommonUtils.newStr(fieldJson, entity).toString());
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
                ustrList.add(CommonUtils.updateStr(dbMaps.get(entity.getId()), entity, fieldJson).toString());
                dbIds.remove(entity.getId());
            }
        }
        if (CollUtil.isNotEmpty(dbIds)) {
            dstrList.add(String.join(StrUtil.COMMA, dbIds));
        }
        StringBuffer stringBuilder = new StringBuffer();
        if (CollUtil.isNotEmpty(istrList)) {
            stringBuilder.append("新增[" + CollUtil.join(istrList, StrUtil.COMMA) + "]");
        }
        if (CollUtil.isNotEmpty(ustrList)) {
            stringBuilder.append("修改[" + CollUtil.join(ustrList, StrUtil.COMMA) + "]");
        }
        if (CollUtil.isNotEmpty(dstrList)) {
            stringBuilder.append("删除[" + CollUtil.join(dstrList, StrUtil.COMMA) + "]");
        }

        queryWrapper.eq("company_code", companyCode);
        //逻辑删除传进来不存在的
        if (ids.size() > 0) {
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        //新增
        this.saveBatch(addList);
        //修改
        this.updateBatchById(updateList);

        List<String> documentIds = new ArrayList<>();
        documentIds.addAll(dbIds);
        documentIds.addAll(addList.stream().map(t -> t.getId()).collect(Collectors.toList()));
        documentIds.addAll(updateList.stream().map(t -> t.getId()).collect(Collectors.toList()));
        log.setContent(stringBuilder.toString());
        log.setType("修改");
        log.setDocumentId(CollUtil.join(documentIds, StrUtil.COMMA));
        operaLogService.save(log);
        return entityList.size();
    }

    public void setUpdateInfo(UpdateWrapper uw) {
        UserCompany userCompany = userUtils.getUserCompany();
        uw.set("update_id", userCompany.getId());
        uw.set("update_name", userCompany.getAliasUserName());
        uw.set("update_date", new Date());
    }


}
