package com.base.sbc.module.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/13 11:49:17
 * 自定义增强
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 批量提交修改，逻辑删除新增修改
     *
     * @param entityList   实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper);


    /**
     * 慎用！！！！！！！！。
     * 根据id物理删除数据
     *
     * @param id 主键id
     * @return 操作结果
     */
    Boolean physicalDeleteById(String id);

    /**
     * 慎用！！！！！！！！。
     * 根据构造器物理删除数据
     *
     * @param queryWrapper 构造器
     * @return 删除的数量
     */
    Integer physicalDeleteQWrap(QueryWrapper<T> queryWrapper);


    boolean saveOrUpdate(T entity, String name);
    boolean saveOrUpdate(T entity, String name,String documentName,String documentCode);

    boolean save(T entity, String name);

    boolean save(T entity, String name,String documentName,String documentCode);

    boolean saveBatch(List<T> entity, String name);

    boolean updateById(T entity, String name);
    boolean updateById(T entity, String name,String documentName,String documentCode);

     boolean updateBatchById(List<T> entity, String name);
    /**
     * 保存操作日志
     */
    void saveOperaLogBatch( String type, String name, List<T> newObject, List<T> oldObject);


    /**
     * 保存操作日志
     */
    void saveOperaLog( String type, String name, T newObject, T oldObject);

     void saveOperaLog(String type, String name,String documentName,String documentCode, T newObject, T oldObject);
    /**
     * 保存操作日志
     */
    void saveOrUpdateOperaLog(Object newObject, Object oldObject, OperaLogEntity operaLogEntity);

    /**
     * 批量保存操作日志
     */
    void saveBatchOperaLog(List<T> newObject, OperaLogEntity operaLogEntity);

    /**
     * 批量修改操作日志
     */
    void updateBatchOperaLog(List<T> newObject, List<T> oldObject, OperaLogEntity operaLogEntity);

    boolean removeByIds(Collection<?> list, OperaLogEntity operaLogEntity);

    boolean removeById(Serializable id, OperaLogEntity operaLogEntity);


    /**
     * 删除并且记录日志
     * @param removeDto 删除参数
     * @return 操作结果
     */
    boolean removeByIds(RemoveDto removeDto);


    boolean saveLog(OperaLogEntity operaLogEntity);
}
