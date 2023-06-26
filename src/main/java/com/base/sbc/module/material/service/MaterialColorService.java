package com.base.sbc.module.material.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.material.entity.MaterialColor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/31 16:08:52
 */
@Service
public interface MaterialColorService extends BaseService<MaterialColor> {

    /**
     * 获取素材列表相关联的列表
     */
    List<MaterialColor> getByMaterialIds(List<String> materialIds);

    /**
     * 根据id集合查询列表
     */
    List<MaterialColor> getColorId(String colorId);

}
