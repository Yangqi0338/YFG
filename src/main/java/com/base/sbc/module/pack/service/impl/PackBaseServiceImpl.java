package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.service.PackBaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：资料包 公共接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.service.impl.PackBaseServiceImpl
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-08 09:40
 */
public abstract class PackBaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements PackBaseService<T> {

    abstract String getModeName();

    @Resource
    private UserUtils userUtils;
    @Resource
    private OperaLogService operaLogService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void log(String id, String type, String content) {
        QueryWrapper<T> qw = new QueryWrapper();
        qw.in("id", StrUtil.split(id, StrUtil.COMMA));
        qw.last("limit 1");
        T byId = getOne(qw);
        if (byId == null) {
            return;
        }
        String foreignId = BeanUtil.getProperty(byId, "foreignId");
        String packType = BeanUtil.getProperty(byId, "packType");
        OperaLogEntity log = new OperaLogEntity();
        log.setDocumentId(id);
        log.setName(getModeName());
        log.setContent(content);
        log.setParentId(foreignId);
        log.setPath(CollUtil.join(CollUtil.newArrayList("资料包", packType, foreignId, getModeName()), StrUtil.DASHED));
        log.setType(type);
        operaLogService.save(log);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void log(String id, String type) {
        log(id, type, type + id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(Collection<?> list) {
        String ids = CollUtil.join(list, StrUtil.COMMA);
        log(ids, "删除", "删除" + ids);
        boolean flg = super.removeByIds(list);
        return flg;
    }

    @Override
    public boolean removeById(Serializable id) {
        log((String) id, "删除", "删除" + id);
        boolean b = super.removeById(id);
        return b;
    }

    @Override
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper) {
        OperaLogEntity log = new OperaLogEntity();
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();
        Collection<String> ids = new ArrayList<>();
        List<T> dbList = list(queryWrapper);
        JSONObject fieldJson = CommonUtils.getFieldJson(this.entityClass);
        Map<String, T> dbMaps = Opt.ofEmptyAble(dbList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
        Set<String> dbIds = dbMaps.keySet();
        List<String> ustrList = new ArrayList<>();
        List<String> istrList = new ArrayList<>();
        List<String> dstrList = new ArrayList<>();
        String parentId = Opt.ofEmptyAble(dbList).map(a -> a.get(0)).map(b -> (String) BeanUtil.getProperty(b, "foreignId")).orElse("");
        String packType = Opt.ofEmptyAble(dbList).map(a -> a.get(0)).map(b -> (String) BeanUtil.getProperty(b, "packType")).orElse("");
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
                parentId = Opt.ofBlankAble(parentId).orElse(BeanUtil.getProperty(entity, "foreignId"));
                packType = Opt.ofBlankAble(packType).orElse(BeanUtil.getProperty(entity, "packType"));
                istrList.add(CommonUtils.newStr(fieldJson, entity).toString());
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
                ustrList.add(CommonUtils.updateStr(dbMaps.get(entity.getId()), entity, fieldJson).toString());
                dbIds.remove(entity.getId());
                parentId = Opt.ofBlankAble(parentId).orElse(BeanUtil.getProperty(entity, "foreignId"));
                packType = Opt.ofBlankAble(packType).orElse(BeanUtil.getProperty(entity, "packType"));
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
        log.setName(getModeName());
        log.setParentId(parentId);
        //String pathSqEL = "'资料包-'+#p0.packType+'-'+#p0.foreignId+'-工艺说明-'+#p0.specType";
        log.setPath(CollUtil.join(CollUtil.newArrayList("资料包", packType, parentId, getModeName()), StrUtil.DASHED));
        log.setDocumentId(CollUtil.join(documentIds, StrUtil.COMMA));
        operaLogService.save(log);
        return entityList.size();

    }
}
