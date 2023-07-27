package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.mapper.SpecificationMapper;
import com.base.sbc.module.basicsdatum.service.SpecificationService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/27 10:44
 * @mail 247967116@qq.com
 */
@Service
public class SpecificationServiceImpl extends BaseServiceImpl<SpecificationMapper,Specification> implements SpecificationService {

    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<SpecificationExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), SpecificationExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("specificationType");
        Map<String, String> map = dictInfoToMap.get("specificationType");
        List<Specification> Specifications = BeanUtil.copyToList(list, Specification.class);
        for (Specification specification : Specifications) {
            specification.setStatus("1");
            if (!StringUtils.isEmpty(specification.getTypeName())) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(specification.getTypeName())) {
                        specification.setType(key);
                        break;
                    }
                }
            }
            QueryWrapper<Specification> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("code",specification.getCode());
            this.saveOrUpdate(specification,queryWrapper);
        }
        return true;
    }

    @Override
    public boolean saveSpecification(Specification specification) {
        if (StringUtils.isNotBlank(specification.getId())) {
            //校验名称或者编码是否相同
            BaseQueryWrapper<Specification> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", specification.getCode());
            Specification one = this.getOne(queryWrapper);
            if (one != null && !one.getId().equals(specification.getId())) {
                throw new OtherException("编码存在重复");
            }
            BaseQueryWrapper<Specification> queryWrapper1 = new BaseQueryWrapper<>();
            queryWrapper1.eq("name", specification.getName());
            Specification one1 = this.getOne(queryWrapper1);
            if (one1 != null && !one1.getId().equals(specification.getId())) {
                throw new OtherException("名称存在重复");
            }
            return this.updateById(specification);
        } else {
            BaseQueryWrapper<Specification> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", specification.getCode());
            Specification one = this.getOne(queryWrapper);
            if (one != null) {
                throw new OtherException("编码存在重复");
            }
            BaseQueryWrapper<Specification> queryWrapper1 = new BaseQueryWrapper<>();
            queryWrapper1.eq("name", specification.getName());
            Specification one1 = this.getOne(queryWrapper1);
            if (one1 != null) {
                throw new OtherException("名称存在重复");
            }
            return this.save(specification);
        }
    }
}
