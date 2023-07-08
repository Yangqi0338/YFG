package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;

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
     * @param id
     * @param type
     */
    void log(String id, String type);
}
