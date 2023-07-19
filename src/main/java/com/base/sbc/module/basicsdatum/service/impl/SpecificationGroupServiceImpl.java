package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumModelTypeExcelDto;
import com.base.sbc.module.basicsdatum.dto.SpecificationGroupDto;
import com.base.sbc.module.basicsdatum.dto.SpecificationGroupExcelDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.basicsdatum.mapper.SpecificationGroupMapper;
import com.base.sbc.module.basicsdatum.mapper.SpecificationMapper;
import com.base.sbc.module.basicsdatum.service.SpecificationGroupService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/27 17:24
 * @mail 247967116@qq.com
 */
@Service
public class SpecificationGroupServiceImpl extends BaseServiceImpl<SpecificationGroupMapper,SpecificationGroup> implements SpecificationGroupService {

    @Autowired
    private SpecificationMapper specificationMapper;

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

    @Override
    public List<Map<String, String>> listIdName() {
        return  baseMapper.listIdName();

    }

    /**
     * 基础资料-门幅组导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean specificationGroupImportExcel(MultipartFile file) throws  Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<SpecificationGroupExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), SpecificationGroupExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());

        for (SpecificationGroupExcelDto specificationGroupExcelDto : list) {
            if (StringUtils.isNotBlank(specificationGroupExcelDto.getSpecificationNames())) {
                specificationGroupExcelDto.setSpecificationNames(specificationGroupExcelDto.getSpecificationNames().replaceAll(" ", ""));
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("name", StringUtils.convertList(specificationGroupExcelDto.getSpecificationNames()));
                List<Specification> specificationList = specificationMapper.selectList(queryWrapper);
                if (!CollectionUtils.isEmpty(specificationList)) {
                    List<String> stringList = specificationList.stream().map(Specification::getId).collect(Collectors.toList());
                    specificationGroupExcelDto.setSpecificationIds(StringUtils.join(stringList, ","));
                }
            }
        }
        List<SpecificationGroup> specificationGroups = BeanUtil.copyToList(list, SpecificationGroup.class);
        for (SpecificationGroup specificationGroup : specificationGroups) {
            specificationGroup.setStatus("1");
            QueryWrapper<SpecificationGroup> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", specificationGroup.getCode());
            this.saveOrUpdate(specificationGroup, queryWrapper);
        }
        return true;
    }

    /**
     * 基础资料-门幅组导出
     *
     * @param response
     * @return
     */
    @Override
    public void specificationGroupDeriveExcel(HttpServletResponse response, SpecificationGroupDto specificationGroupDto) throws Exception {
        BaseQueryWrapper<SpecificationGroup> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("status",specificationGroupDto.getStatus());
        queryWrapper.notEmptyLike("type",specificationGroupDto.getType());
        queryWrapper.notEmptyLike("name",specificationGroupDto.getName());
        queryWrapper.notEmptyLike("code",specificationGroupDto.getCode());
        queryWrapper.notEmptyLike("create_name",specificationGroupDto.getCreateName());
        queryWrapper.notEmptyLike("create_date",specificationGroupDto.getCreateDate());
        List<SpecificationGroupExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), SpecificationGroupExcelDto.class);
        ExcelUtils.exportExcel(list, SpecificationGroupExcelDto.class, "基础资料-门幅组.xlsx", new ExportParams(), response);
    }
}
