package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.pack.service.PackBaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：资料包 公共接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.service.impl.AbstractPackBaseServiceImpl
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-08 09:40
 */
public abstract class AbstractPackBaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements PackBaseService<T> {

    abstract String getModeName();

    @Resource
    private UserUtils userUtils;
    @Resource
    private OperaLogService operaLogService;


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(Collection<?> list) {
        if (CollUtil.isEmpty(list)) {
            return true;
        }
        Object first = CollUtil.getFirst(list);
        T byId = getById((Serializable) first);
        String ids = CollUtil.join(list, StrUtil.COMMA);
        boolean flg = super.removeByIds(list, genOperaLogEntity(byId, "删除"));
        return flg;
    }

    @Override
    public boolean removeById(Serializable id) {
        T byId = getById(id);

        boolean b = super.removeById(id, genOperaLogEntity(byId, "删除"));

        return b;
    }


    @Override
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper, boolean delFlg) {
        String companyCode = userUtils.getCompanyCode();

        // 新增的
        List<T> addList = new ArrayList<>();
        // 修改的
        List<T> updateList = new ArrayList<>();
        Collection<String> updateIds = new ArrayList<>();
        List<T> dbList = list(queryWrapper);
        List<String> dbIds = Opt.ofEmptyAble(dbList).map(a -> a.stream().map(T::getId).collect(Collectors.toList())).orElse(CollUtil.newArrayList());
        String parentId = Opt.ofEmptyAble(dbList).map(a -> a.get(0)).map(b -> (String) BeanUtil.getProperty(b, "foreignId")).orElse("");
        String packType = Opt.ofEmptyAble(dbList).map(a -> a.get(0)).map(b -> (String) BeanUtil.getProperty(b, "packType")).orElse("");
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
                parentId = Opt.ofBlankAble(parentId).orElse(BeanUtil.getProperty(entity, "foreignId"));
                packType = Opt.ofBlankAble(packType).orElse(BeanUtil.getProperty(entity, "packType"));
            } else {
                //说明是修改的
                updateList.add(entity);
                updateIds.add(entity.getId());
                parentId = Opt.ofBlankAble(parentId).orElse(BeanUtil.getProperty(entity, "foreignId"));
                packType = Opt.ofBlankAble(packType).orElse(BeanUtil.getProperty(entity, "packType"));
            }
        }

        OperaLogEntity baseLog = new OperaLogEntity();
        baseLog.setParentId(parentId);
        baseLog.setName(getModeName());
        System.out.println(getModeName());
        baseLog.setPath(packType);
        if (delFlg) {
            List<String> delIds = dbIds.stream().filter(item -> !updateIds.contains(item)).collect(Collectors.toList());
            OperaLogEntity delLog = BeanUtil.copyProperties(baseLog, OperaLogEntity.class);
            this.removeByIds(delIds, delLog);

        }
        //新增
        this.saveBatch(addList);
        OperaLogEntity addLog = BeanUtil.copyProperties(baseLog, OperaLogEntity.class);
        addLog.setType("新增");
        saveBatchOperaLog(addList, addLog);
        //修改
        this.updateBatchById(updateList, getModeName());
        OperaLogEntity updateLog = BeanUtil.copyProperties(baseLog, OperaLogEntity.class);
        updateLog.setType("修改");
        updateBatchOperaLog(updateList, dbList, updateLog);
        return entityList.size();

    }

    @Override
    public Integer addAndUpdateAndDelListSub(List<T> entityList, QueryWrapper<T> queryWrapper, String pid) {
        return 0;

    }

    @Override
    public OperaLogEntity genOperaLogEntity(Object bean, String type) {
        OperaLogEntity log = new OperaLogEntity();
        log.setDocumentId(BeanUtil.getProperty(bean, "id"));
        log.setType(type);
        log.setName(getModeName());
        log.setPath(BeanUtil.getProperty(bean, "packType"));
        log.setParentId(BeanUtil.getProperty(bean, "foreignId"));
        return log;
    }

    @Override
    public boolean save(T entity) {
        boolean save = super.save(entity);
        saveOrUpdateOperaLog(entity, null, genOperaLogEntity(entity, "新增"));
        return save;
    }


    @Override
    public boolean del(String foreignId, String packType) {
        QueryWrapper<T> delQw = new QueryWrapper<>();
        delQw.eq("foreign_id", foreignId);
        delQw.eq("pack_type", packType);
        return remove(delQw);
    }

    @Override
    public boolean physicsDel(String foreignId, String packType) {
        QueryWrapper<T> delQw = new QueryWrapper<>();
        delQw.eq("foreign_id", foreignId);
        delQw.eq("pack_type", packType);
        return physicalDeleteQWrap(delQw)>0;
    }

    @Override
    public boolean delByIds(String id) {
        return removeByIds(StrUtil.split(id, StrUtil.COMMA));
    }

    @Override
    public T get(String foreignId, String packType) {
        QueryWrapper<T> query = new QueryWrapper<>();
        query.eq("foreign_id", foreignId);
        query.eq("pack_type", packType);
        query.last("limit 1");
        return getOne(query);
    }

    @Override
    public List<T> list(String foreignId, String packType) {
        QueryWrapper<T> query = new QueryWrapper<>();
        query.eq("foreign_id", foreignId);
        query.eq("pack_type", packType);
        return list(query);
    }

    @Override
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        if (StrUtil.equals(overlayFlag, BaseGlobal.YES)) {
            del(targetForeignId, targetPackType);
        }
        QueryWrapper<T> query = new QueryWrapper<>();
        query.eq("foreign_id", sourceForeignId);
        query.eq("pack_type", sourcePackType);
        List<T> list = list(query);
        if (CollUtil.isNotEmpty(list)) {
            for (T t : list) {
                BeanUtil.setProperty(t, "foreignId", targetForeignId);
                BeanUtil.setProperty(t, "packType", targetPackType);
                t.preInsert();
                t.setId(null);
                try {
                    BeanUtil.setProperty(t, "historicalData", BaseGlobal.NO);
                } catch (Exception e) {

                }
            }
            return saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean move(String id, String column, int moveType) {
        T byId = getById(id);
        QueryWrapper<T> query = new QueryWrapper<>();
        query.eq("foreign_id", BeanUtil.getProperty(byId, "foreignId"));
        query.eq("pack_type", BeanUtil.getProperty(byId, "packType"));
        query.orderByAsc(column);
        List<Object> objects = listObjs(query);
        if (CollUtil.size(objects) < 1) {
            return true;
        }
        int i = CollUtil.indexOf(objects, (a) -> ObjectUtil.equals(a, id));
        int moveIndex = i + moveType;
        if (moveIndex >= 0 && moveIndex < objects.size()) {
            Collections.swap(objects, i, moveIndex);
        }
        for (int j = 0; j < objects.size(); j++) {
            UpdateWrapper<T> uw = new UpdateWrapper<>();
            uw.set(column, j + 1);
            uw.eq("id", objects.get(j));
            update(uw);
        }
        return true;
    }

    @Override
    public boolean sort(String ids, String column) {
        List<String> idList = StrUtil.split(ids, CharUtil.COMMA);

        for (int i = 0; i < idList.size(); i++) {
            UpdateWrapper<T> uw = new UpdateWrapper<>();
            uw.eq("id", idList.get(i));
            uw.set(column, i);
            update(uw);
        }
        return true;
    }

    @Override
    public boolean log(String id, String type) {
        T byId = getById(id);
        OperaLogEntity operaLogEntity = genOperaLogEntity(byId, type);
        operaLogService.save(operaLogEntity);
        return true;
    }

    @Override
    public long count(String foreignId, String packType) {
        QueryWrapper<T> query = new QueryWrapper<>();
        query.eq("foreign_id", foreignId);
        query.eq("pack_type", packType);
        return count(query);
    }
}
