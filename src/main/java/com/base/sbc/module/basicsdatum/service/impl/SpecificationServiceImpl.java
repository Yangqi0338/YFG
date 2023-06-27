package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.mapper.SpecificationMapper;
import com.base.sbc.module.basicsdatum.service.SpecificationService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/27 10:44
 * @mail 247967116@qq.com
 */
@Service
public class SpecificationServiceImpl extends BaseServiceImpl<SpecificationMapper,Specification> implements SpecificationService {
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<SpecificationExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), SpecificationExcelDto.class, params);
        List<Specification> Specifications = BeanUtil.copyToList(list, Specification.class);
        for (Specification specification : Specifications) {
            specification.setStatus("1");
            QueryWrapper<Specification> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("code",specification.getCode());
            this.saveOrUpdate(specification,queryWrapper);
        }
        return true;
    }
}
