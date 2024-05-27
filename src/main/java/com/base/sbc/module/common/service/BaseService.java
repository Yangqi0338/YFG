package com.base.sbc.module.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/4/13 11:49:17
 * 自定义增强
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 跟据字段名称和字段集合查询列表
     * @param fieldName 字段名称
     * @param list     数据集合
     * @return 查询结果
     */
    List<T> listByField(String fieldName, Collection<?> list);

    /**
     * 跟据字段名称和字段集合查询列表
     * @param fieldName 字段名称
     * @param list     数据集合
     * @return 查询结果
     */
    List<T> listByField(String fieldName, String list);



    /**
     * 跟据字段名称和字段查询一条数据
     * @param fieldName
     * @param id
     * @return
     */
    T getByOne(String fieldName, String id);


    /**
     * 跟据字段名称和字段查询多条数据
     * @param fieldName
     * @param id
     * @return
     */
    List<T> getByList(String fieldName, String id);
    /**
     * 根据传入的对象，查询符合条件的数据
     */
    List<T> listByObject(Object object, BaseQueryWrapper<T> baseQueryWrapper, String ...exFields);


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

    void saveOperaLog(OperaLogEntity operaLogEntity);

     void saveOperaLog(String type, String name,String documentName,String documentCode, T newObject, T oldObject);

    public void saveOperaLog(String type, String name, String documentName, String documentCode, Map<String,String> data);
    /**
     * 保存操作日志
     */
    void saveOrUpdateOperaLog(Object newObject, Object oldObject, OperaLogEntity operaLogEntity);


    /**
     * 保存修改操作日志
     * @param type 操作类型 新增/修改
     * @param name 模块名称
     * @param documentId 单据id
     * @param documentName 单据名称
     * @param documentCode 单据编码
     * @param newObject 新对象
     * @param oldObject 原对象
     */
    void saveOperaLog(String type, String name,String documentId,String documentName,String documentCode, T newObject, T oldObject);




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


    /**
     * 启用与停用并且记录日志
     */
    void startStopLog(StartStopDto startStopDto);

    /**
     * 根据queryWrapper检查是否存在
     */
    boolean exists(Wrapper<T> wrapper);

    boolean exists(String id);

    T findOne(QueryWrapper<T> wrapper);
    T findOne(LambdaQueryWrapper<T> wrapper);
    <R> List<R> listOneField(LambdaQueryWrapper<T> wrapper, SFunction<T,R> function);
    <R> List<R> listByIds2OneField(List<String> ids, SFunction<T,R> function);

    /**
     * 用于redis回查
     */
    default Object findByCode(String code) {
        return null;
    }
    <R> R findOneField(LambdaQueryWrapper<T> wrapper, SFunction<T,R> function);
    <R> R findByIds2OneField(String id, SFunction<T,R> function);
    <R> List<R> groupOneField(LambdaQueryWrapper<T> wrapper, SFunction<T,R> function);
}
