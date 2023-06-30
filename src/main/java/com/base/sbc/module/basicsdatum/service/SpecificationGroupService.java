package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/27 17:24
 * @mail 247967116@qq.com
 */
public interface SpecificationGroupService extends BaseService<SpecificationGroup> {
    boolean saveSpecification(SpecificationGroup specificationGroup);

    List<Map<String, String>> listIdName();

}
