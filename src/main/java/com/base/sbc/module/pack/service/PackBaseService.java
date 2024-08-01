package com.base.sbc.module.pack.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;

import java.util.List;

public interface PackBaseService<T> extends BaseService<T> {


    int MOVE_UP = -1;
    int MOVE_DOWN = 1;

    /**
     * 新增 修改 删除
     *
     * @param entityList
     * @param queryWrapper
     * @param delFlg       是否删除
     * @return
     */
    Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper, boolean delFlg);

    /**
     * 子表的新增 修改 删除
     * 物料清单的尺码用量规格，颜色 配色
     *
     * @param entityList
     * @param queryWrapper
     * @param pid
     * @return
     */
    Integer addAndUpdateAndDelListSub(List<T> entityList, QueryWrapper<T> queryWrapper, String pid);

    OperaLogEntity genOperaLogEntity(Object bean, String type);


    boolean delByIds(String id);

    /**
     * 删除
     *
     * @param foreignId
     * @param packType
     * @return
     */
    boolean del(String foreignId, String packType);

    /**
     * 物理删除
     *
     * @param foreignId
     * @param packType
     * @return
     */
    boolean physicsDel(String foreignId, String packType);

    /**
     * 获取1个
     *
     * @param foreignId
     * @param packType
     * @return
     */
    T get(String foreignId, String packType);

    /**
     * 获取列表
     *
     * @param foreignId
     * @param packType
     * @return
     */
    List<T> list(String foreignId, String packType);

    /**
     * 复制
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param overlayFlag     覆盖标识1 为覆盖
     * @return
     */
    boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag);


    /**
     * 移动
     *
     * @param id
     * @param column
     * @param moveType
     * @return
     */
    boolean move(String id, String column, int moveType);

    /**
     * 排序
     *
     * @param ids
     * @return
     */
    boolean sort(String ids, String column);

    boolean log(String id, String type);

    long count(String foreignId, String packType);
}
