package com.base.sbc.module.basicsdatum.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.basicsdatum.mapper.SpecificationGroupMapper;
import com.base.sbc.module.basicsdatum.service.SpecificationGroupService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 卞康
 * @date 2023/6/27 17:24
 * @mail 247967116@qq.com
 */
@Service
public class SpecificationGroupServiceImpl extends BaseServiceImpl<SpecificationGroupMapper,SpecificationGroup> implements SpecificationGroupService {
    @Override
    public boolean saveSpecification(SpecificationGroup specificationGroup) {

        if (StringUtils.isNotBlank(specificationGroup.getId())) {
            //校验名称或者编码是否相同
            BaseQueryWrapper<SpecificationGroup> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", specificationGroup.getCode());
            SpecificationGroup one = this.getOne(queryWrapper);
            if (one != null && !one.getId().equals(specificationGroup.getId())) {
                throw new OtherException("编码存在重复");
            }
            BaseQueryWrapper<SpecificationGroup> queryWrapper1 = new BaseQueryWrapper<>();
            queryWrapper1.eq("name", specificationGroup.getName());
            SpecificationGroup one1 = this.getOne(queryWrapper1);
            if (one1 != null && !one1.getId().equals(specificationGroup.getId())) {
                throw new OtherException("名称存在重复");
            }
            return this.updateById(specificationGroup);
        } else {
            BaseQueryWrapper<SpecificationGroup> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", specificationGroup.getCode());
            SpecificationGroup one = this.getOne(queryWrapper);
            if (one != null) {
                throw new OtherException("编码存在重复");
            }
            BaseQueryWrapper<SpecificationGroup> queryWrapper1 = new BaseQueryWrapper<>();
            queryWrapper1.eq("name", specificationGroup.getName());
            SpecificationGroup one1 = this.getOne(queryWrapper1);
            if (one1 != null) {
                throw new OtherException("名称存在重复");
            }
            return this.save(specificationGroup);
        }
    }
}
