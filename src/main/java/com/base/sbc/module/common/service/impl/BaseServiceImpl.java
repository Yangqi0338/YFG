package com.base.sbc.module.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/4/13 11:50:06
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Resource
    private UserUtils userUtils;

    public static final String COMPANY_CODE = "company_code";
    public static final String DEL_FLAG = "del_flag";


    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private OperaLogService operaLogService;

    /**
     * 获取企业编码
     *
     * @return
     */
    public String getCompanyCode() {
        return userUtils.getCompanyCode();
    }

    public String getUserId() {
        return userUtils.getUserCompany().getUserId();
    }

    public String getUserName() {
        return userUtils.getUserCompany().getAliasUserName();
    }

    /**
     * 批量提交修改，逻辑删除新增修改
     *
     * @param entityList   实体列表
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

        Collection<String> ids = new ArrayList<>();
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
        if (!StringUtils.isEmpty(companyCode) && !"0".equals(companyCode)) {
            queryWrapper.eq("company_code", companyCode);
        }
        //逻辑删除传进来不存在的
        if (!ids.isEmpty()) {
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        //新增
        this.saveBatch(addList);
        //修改
        this.updateBatchById(updateList);

        return entityList.size();
    }

    /**
     * 慎用！！！！！！！！。
     * 根据id物理删除数据
     *
     * @param id 主键id
     * @return 操作结果
     */
    @Override
    public Boolean physicalDeleteById(String id) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        String tableName = tableInfo.getTableName();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        int update = jdbcTemplate.update(sql, id);
        log.warn("=================> 物理删除 SQL：" + sql + " | 返回值：" + update);
        return update > 0;
    }

    /**
     * 慎用！！！！！！！！。
     * 根据构造器物理删除数据
     *
     * @param queryWrapper 构造器
     * @return 删除的数量
     */
    @Override
    public Integer physicalDeleteQWrap(QueryWrapper<T> queryWrapper) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        String tableName = tableInfo.getTableName();

        String customSqlSegment = queryWrapper.getCustomSqlSegment();

        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        ArrayList<Object> list = new ArrayList<>();
        int i = 1;
        for (String key : paramNameValuePairs.keySet()) {
            customSqlSegment = customSqlSegment.replace("#{ew.paramNameValuePairs.MPGENVAL" + i + "}", "?");
            list.add(paramNameValuePairs.get("MPGENVAL" + i));
            i++;
        }

        String sql = String.format("DELETE FROM %s %s", tableName, customSqlSegment);
        Object[] params = list.toArray();
        int update = jdbcTemplate.update(sql, params);
        log.warn("=================> 物理删除 SQL： " + sql + " | 参数：" + Arrays.toString(params) + " | 删除的行数：" + update);
        return update;
    }


    public void setUpdateInfo(UpdateWrapper<T> uw) {
        UserCompany userCompany = userUtils.getUserCompany();
        uw.set("update_id", userCompany.getId());
        uw.set("update_name", userCompany.getAliasUserName());
        uw.set("update_date", new Date());
    }


    /**
     * 新增或者更新,并且记录日志
     *
     * @param entity 实体对象
     * @param name   模块名称
     * @return boolean
     */
    public boolean saveOrUpdate(T entity, String name) {
        if (this.getById(entity.getId()) != null) {
            return this.updateById(entity, name);
        } else {
            return this.save(entity, name);
        }
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean save(T entity, String name) {
        String type = "新增";
        boolean save = this.save(entity);
        if (save) {
            this.saveOperaLog(type, name, entity, null);
        }
        return save;
    }

    /**
     * 批量保存日志
     *
     * @param entity
     * @param name
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<T> entity, String name) {
        String type = "新增";
        boolean save = saveBatch(entity);
        if (save) {
            this.saveOperaLogBatch(type, name, entity, new ArrayList<>());
        }
        return save;
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean updateById(T entity, String name) {
        String type = "修改";
        T oldEntity = this.getById(entity.getId());
        boolean update = this.updateById(entity);
        if (update) {
            this.saveOperaLog(type, name, entity, oldEntity);
        }
        return update;
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(List<T> entity, String name) {
        if (CollUtil.isEmpty(entity)) {
            return true;
        }
        String type = "修改";
        List<String> ids = entity.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<T> oldEntity = this.listByIds(ids);
        boolean update = this.updateBatchById(entity);
        if (update) {
            this.saveOperaLogBatch(type, name, entity, oldEntity);
        }
        return update;
    }


    /**
     * 批量保存操作日志
     *
     * @param type
     * @param name
     * @param newObject
     * @param oldObject
     */
    @Override
    public void saveOperaLogBatch(String type, String name, List<T> newObject, List<T> oldObject) {
        List<OperaLogEntity> operaLogEntityList = new ArrayList<>();

        for (T t : newObject) {
            JSONArray jsonArray = new JSONArray();
            for (T t1 : oldObject) {
                if (t.getId().equals(t1.getId())) {
                    jsonArray = CommonUtils.recordField(t, t1);
                }
            }
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setDocumentId(t.getId());
            operaLogEntity.setName(name);
            operaLogEntity.setType(type);
            operaLogEntity.setJsonContent(jsonArray.toJSONString());
            operaLogEntityList.add(operaLogEntity);
        }
        operaLogService.saveBatch(operaLogEntityList);
    }

    /**
     * 保存操作日志
     *
     * @param type      操作类型  新增 修改 删除
     * @param name      模块名称
     * @param newObject 新对象
     * @param oldObject 旧对象
     */
    @Override
    public void saveOperaLog(String type, String name, T newObject, T oldObject) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        JSONArray jsonArray = CommonUtils.recordField(newObject, oldObject);
        operaLogEntity.setDocumentId(newObject.getId());
        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogEntity.setName(name);
        operaLogEntity.setType(type);
        operaLogService.save(operaLogEntity);
    }


    /**
     * 保存操作日志
     *
     * @param newObject
     * @param oldObject
     */
    @Override
    public void saveOrUpdateOperaLog(Object newObject, Object oldObject, OperaLogEntity operaLogEntity) {
        JSONArray jsonArray = CommonUtils.recordField(newObject, oldObject);
        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogService.save(operaLogEntity);
    }

    /**
     * 批量保存操作日志
     */
    @Override
    public void saveBatchOperaLog(List<T> newObject, OperaLogEntity operaLogEntity) {
        updateBatchOperaLog(newObject, null, operaLogEntity);
    }

    /**
     * 批量修改操作日志
     *
     * @param newObject
     * @param oldObject
     * @param operaLogEntity
     */
    @Override
    public void updateBatchOperaLog(List<T> newObject, List<T> oldObject, OperaLogEntity operaLogEntity) {
        List<OperaLogEntity> operaLogEntityList = new ArrayList<>();
        Map<String, T> newMap = Opt.ofNullable(newObject).map(no -> no.stream().collect(Collectors.toMap(BaseEntity::getId, v -> v, (a, b) -> a))).orElse(MapUtil.empty());
        Map<String, T> oldMap = Opt.ofNullable(oldObject).map(no -> no.stream().collect(Collectors.toMap(BaseEntity::getId, v -> v, (a, b) -> a))).orElse(MapUtil.empty());
        for (Map.Entry<String, T> entry : newMap.entrySet()) {
            OperaLogEntity log = BeanUtil.copyProperties(operaLogEntity, OperaLogEntity.class);
            log.setId(null);
            String id = entry.getKey();
            T ne = entry.getValue();
            T oe = oldMap.get(id);
            JSONArray jsonArray = CommonUtils.recordField(ne, oe);
            log.setJsonContent(jsonArray.toJSONString());
            log.setDocumentId(id);
            operaLogEntityList.add(log);
        }
        operaLogService.saveBatch(operaLogEntityList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(Collection<?> list, OperaLogEntity operaLogEntity) {
        String ids = CollUtil.join(list, StrUtil.COMMA);
        operaLogEntity.setType("删除");
        operaLogEntity.setContent(ids);
        operaLogService.save(operaLogEntity);
        boolean flg = super.removeByIds(list);
        return flg;
    }

    @Override
    public boolean removeById(Serializable id, OperaLogEntity operaLogEntity) {
        boolean b = super.removeById(id);
        operaLogEntity.setType("删除");
        operaLogEntity.setDocumentId(id.toString());
        operaLogEntity.setContent(id.toString());
        operaLogService.save(operaLogEntity);
        return b;
    }

    /**
     * @param ids
     * @param name
     * @return
     */
    @Override
    @Transactional
    public boolean removeByIds(List<String> ids, String name) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName(name);
        operaLogEntity.setType("删除");
        operaLogEntity.setContent(CollUtil.join(ids, StrUtil.COMMA));
        operaLogService.save(operaLogEntity);
        return super.removeByIds(ids);
    }

    /**
     * @param id
     * @param name
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeById(String id, String name) {
        return this.removeByIds(Collections.singletonList(id), name);
    }

    @Override
    public boolean saveLog(OperaLogEntity operaLogEntity) {
        return operaLogService.save(operaLogEntity);
    }


}
