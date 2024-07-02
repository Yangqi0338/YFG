/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampSizeDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumSizeExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDasicsdatumSizeDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-尺码表 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 14:01:34
 */
@Service
public class BasicsdatumSizeServiceImpl extends BaseServiceImpl<BasicsdatumSizeMapper, BasicsdatumSize> implements BasicsdatumSizeService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumModelTypeMapper basicsdatumModelTypeMapper;


    /**
     * 查询尺码列表
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumSizeVo> getSizeList(QueryDasicsdatumSizeDto dto) {
        /*分页*/
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }
        BaseQueryWrapper<BasicsdatumSize> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.notEmptyLike("hangtags", dto.getHangtags());
        queryWrapper.notEmptyLike("model", dto.getModel());
        queryWrapper.notEmptyLike("internal_size", dto.getInternalSize());
        queryWrapper.notEmptyLike("create_name", dto.getCreateName());
        queryWrapper.notEmptyEq("status", dto.getStatus());
        queryWrapper.between("create_date",dto.getCreateDate());
        if (StringUtils.isNotEmpty(dto.getAll())){
            if("0".equals(dto.getAll())){
                queryWrapper.eq("model_type_code","").or().isNull("model_type_code");
            }else {
                if (StrUtil.isNotEmpty(dto.getModelTypeExt())) {
                    queryWrapper.notEmptyLike("model_type_code", dto.getModelType()).like("model_type", dto.getModelTypeExt());
                }else{
                    queryWrapper.like("model_type_code", dto.getModelType()).or().eq("model_type_code","").or().isNull("model_type_code");
                }
            }
        }
        queryWrapper.likeList(StrUtil.isNotBlank(dto.getModelTypeCode()),"model_type_code",StringUtils.convertList(dto.getModelTypeCode()));
        queryWrapper.orderByAsc("code");
        /*查询尺码数据*/
        List<BasicsdatumSize> basicsdatumSizeList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumSize> pageInfo = new PageInfo<>(basicsdatumSizeList);
        /*转换vo*/
        List<BasicsdatumSizeVo> list = BeanUtil.copyToList(basicsdatumSizeList, BasicsdatumSizeVo.class);
        PageInfo<BasicsdatumSizeVo> vo = new PageInfo<>();
        vo.setList(list);
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(pageInfo.getPageNum());
        vo.setPageSize(pageInfo.getPageSize());
        return vo;
    }

    /**
     * 尺码导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumSizeExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumSizeExcelDto.class, params);
        list = list.stream().filter(s -> s.getSort()!=null).collect(Collectors.toList());
        for (BasicsdatumSizeExcelDto basicsdatumSizeExcelDto : list) {
            if (!StringUtils.isEmpty(basicsdatumSizeExcelDto.getModelType())) {
                /*查询号型类型*/
                QueryWrapper<BasicsdatumModelType> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("model_type", StringUtils.convertList(basicsdatumSizeExcelDto.getModelType()));
                /*关联号型类型*/
                List<BasicsdatumModelType> modelTypeList = basicsdatumModelTypeMapper.selectList(queryWrapper);
                if(!CollectionUtils.isEmpty(modelTypeList)){
                    List<String> stringList =     modelTypeList.stream().map(BasicsdatumModelType::getCode).collect(Collectors.toList());
                    basicsdatumSizeExcelDto.setModelTypeCode(StringUtils.convertListToString(stringList));
                }
            }

        }
        List<BasicsdatumSize> basicsdatumSizeList = BeanUtil.copyToList(list, BasicsdatumSize.class);
        /*每次添加500条数据*/
        int batchSize = 500;
        for (int i = 0; i < basicsdatumSizeList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, basicsdatumSizeList.size());
            List<BasicsdatumSize> list1 = basicsdatumSizeList.subList(i, endIndex);
            for (BasicsdatumSize basicsdatumSize : list1) {
                QueryWrapper<BasicsdatumSize> queryWrapper =new BaseQueryWrapper<>();
                queryWrapper.eq("code",basicsdatumSize.getCode());
                this.saveOrUpdate(basicsdatumSize,queryWrapper);
            }



            //saveOrUpdateBatch(list1);
        }
        return true;
    }

    /**
     * 导出
     *
     * @param dto isDerive 1导出，0模板导出,sizeLabelId 为空导出所有数据
     * @param response
     */
    @Override
    public void deriveExcel(QueryDasicsdatumSizeDto dto, HttpServletResponse response) throws IOException {
        BaseQueryWrapper<BasicsdatumSize> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.notEmptyLike("hangtags", dto.getHangtags());
        queryWrapper.notEmptyLike("model", dto.getModel());
        queryWrapper.notEmptyLike("internal_size", dto.getInternalSize());
        queryWrapper.notEmptyLike("create_name", dto.getCreateName());
        queryWrapper.notEmptyEq("status", dto.getStatus());
        queryWrapper.between("create_date",dto.getCreateDate());
        List<BasicsdatumSize> list = baseMapper.selectList(queryWrapper);

      List<BasicsdatumSizeExcelDto> basicsdatumSizeExcelDtoList =  BeanUtil.copyToList(list, BasicsdatumSizeExcelDto.class);
        ExcelUtils.exportExcel(basicsdatumSizeExcelDtoList, BasicsdatumSizeExcelDto.class, "尺码.xlsx", new ExportParams(), response);
    }

    /**
     * 新增修改尺码表
     *
     * @param addRevampSizeDto
     * @return
     */
    @Override
    public Boolean addRevampSize(AddRevampSizeDto addRevampSizeDto) {

        BasicsdatumSize basicsdatumSize = new BasicsdatumSize();
        if (StringUtils.isEmpty(addRevampSizeDto.getId())) {
            QueryWrapper<BasicsdatumSize> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("code",addRevampSizeDto.getCode());
            BasicsdatumSize one = this.getOne(queryWrapper);
            if (one!=null){
                throw new OtherException("排序重复");
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampSizeDto, basicsdatumSize);
            basicsdatumSize.setCompanyCode(baseController.getUserCompany());
            basicsdatumSize.setSendStatus(BaseGlobal.STATUS_NORMAL);
            basicsdatumSize.insertInit();
            baseMapper.insert(basicsdatumSize);
        } else {
            /*修改*/
            basicsdatumSize = baseMapper.selectById(addRevampSizeDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumSize)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            if (basicsdatumSize.getSort().equals(addRevampSizeDto.getSort())){
                QueryWrapper<BasicsdatumSize> queryWrapper =new QueryWrapper<>();
                queryWrapper.eq("code",basicsdatumSize.getCode());
                queryWrapper.eq("del_flag",0);
                BasicsdatumSize one = this.getOne(queryWrapper);
                if (one!=null && !addRevampSizeDto.getId().equals(one.getId())){
                    throw new OtherException("排序重复");
                }
            }
            BeanUtils.copyProperties(addRevampSizeDto, basicsdatumSize);
            basicsdatumSize.setSendStatus(BaseGlobal.STATUS_CLOSE);
            basicsdatumSize.updateInit();
            baseMapper.updateById(basicsdatumSize);
        }
        return true;
    }

    /**
     * 启动停止尺码
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean sizeStartStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumSize> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delSize(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    /**
     * 获取尺码名
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String,String> getSizeName(String ids) {
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.in("id",StringUtils.convertList(ids));
        queryWrapper.orderByAsc("code");
        List<BasicsdatumSize> basicsdatumSizeList =baseMapper.selectList(queryWrapper);
        Map<String,String> map =new HashMap<>();
       if(!CollectionUtils.isEmpty(basicsdatumSizeList)){
           basicsdatumSizeList = basicsdatumSizeList.stream().sorted(Comparator.comparing(BasicsdatumSize::getCode)).collect(Collectors.toList());

//           List<String> id =  basicsdatumSizeList.stream().map(BasicsdatumSize::getId).collect(Collectors.toList());
//           List<String> name =  basicsdatumSizeList.stream().map(BasicsdatumSize::getHangtags).collect(Collectors.toList());
//           List<String> sort =  basicsdatumSizeList.stream().map(BasicsdatumSize::getSort).collect(Collectors.toList());
           List<String> id =  new ArrayList<>();
           List<String> name =  new ArrayList<>();
           List<String> sort =  new ArrayList<>();
           List<String> code =  new ArrayList<>();
           for(BasicsdatumSize size : basicsdatumSizeList){
               id.add(size.getId());
               name.add(size.getHangtags());
               sort.add(size.getSort());
               code.add(size.getCode());
           }
           map.put("ids",StringUtils.join(id,","));
           map.put("name",StringUtils.join(name,","));
           map.put("sort",StringUtils.join(sort,","));
           map.put("code", StringUtils.join(code,","));
       }
        return map;
    }


}
