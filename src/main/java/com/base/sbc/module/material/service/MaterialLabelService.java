package com.base.sbc.module.material.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.vo.MaterialChildren;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/29 16:19:50
 */
public interface MaterialLabelService extends BaseService<MaterialLabel> {
    /**
     * 获取素材列表相关联的列表
     */
    List<MaterialLabel> getByMaterialIds(List<String> materialIds);

    /**
     * 根据label_id集合查询列表
     */
    List<MaterialLabel> getByLabelIds(List<String> labelId);

    List<MaterialChildren> linkageQuery(String search,  List<String> materialCategoryIds);
}
