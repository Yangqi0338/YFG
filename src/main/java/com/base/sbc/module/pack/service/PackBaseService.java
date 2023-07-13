package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;

import java.util.List;

public interface PackBaseService<T> extends BaseService<T> {


    /**
     * 记入操作日志
     * @param id
     * @param type
     * @param content
     */
    void log(String id, String type, String content);

    /**
     * 记入操作日志
     *
     * @param id
     * @param type
     */
    void log(String id, String type);

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
     * @return
     */
    boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType);
}
