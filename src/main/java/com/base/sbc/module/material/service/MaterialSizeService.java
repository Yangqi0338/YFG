package com.base.sbc.module.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.material.entity.MaterialSize;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/31 9:36:01
 */
public interface MaterialSizeService extends IService<MaterialSize> {

    /**
     * 获取素材列表相关联的列表
     */
    List<MaterialSize> getByMaterialIds(List<String> materialIds);

    /**
     * 根据id集合查询列表
     */
     List<MaterialSize> getBySizeId(String sizeId);

}
