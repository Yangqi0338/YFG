/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.SpElParseUtil;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.PackTechSpecDto;
import com.base.sbc.module.pack.dto.PackTechSpecPageDto;
import com.base.sbc.module.pack.dto.PackTechSpecSavePicDto;
import com.base.sbc.module.pack.dto.PackTechSpecSearchDto;
import com.base.sbc.module.pack.entity.PackTechSpec;
import com.base.sbc.module.pack.mapper.PackTechSpecMapper;
import com.base.sbc.module.pack.service.PackTechSpecService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：资料包-工艺说明 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechSpecService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
@Service
public class PackTechSpecServiceImpl extends BaseServiceImpl<PackTechSpecMapper, PackTechSpec> implements PackTechSpecService {

    @Resource
    private AttachmentService attachmentService;
    @Resource
    private OperaLogService operaLogService;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<PackTechSpecVo> list(PackTechSpecSearchDto dto) {
        QueryWrapper<PackTechSpec> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq("spec_type", dto.getSpecType());
        qw.orderByAsc("sort", "id");

        List<PackTechSpec> list = list(qw);
        return BeanUtil.copyToList(list, PackTechSpecVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackTechSpecVo saveByDto(PackTechSpecDto dto) {
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PackTechSpec pageData = BeanUtil.copyProperties(dto, PackTechSpec.class);
            pageData.setId(null);
            //查询排序
            if (dto.getSort() == null) {
                QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
                PackUtils.commonQw(countQw, dto);
                countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
                long count = count(countQw);
                pageData.setSort(new BigDecimal(String.valueOf(count + 1)));
            }
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackTechSpecVo.class);
        }
        //修改
        else {
            PackTechSpec dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackTechSpecVo.class);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean sort(String id) {
        List<String> ids = StrUtil.split(id, CharUtil.COMMA);
        for (int i = 0; i < ids.size(); i++) {
            UpdateWrapper<PackTechSpec> uw = new UpdateWrapper<>();
            uw.lambda().eq(PackTechSpec::getId, ids.get(i)).set(PackTechSpec::getSort, i + 1);
            update(uw);
        }
        return true;
    }

    @Override
    public List<AttachmentVo> picList(PackTechSpecSearchDto dto) {
        return attachmentService.findByforeignId(dto.getForeignId(), dto.getPackType() + StrUtil.DASHED + dto.getSpecType());
    }

    @Override
    public AttachmentVo savePic(PackTechSpecSavePicDto dto) {
        return attachmentService.savePackTechSpecPic(dto);
    }

    @Override
    public PageInfo<OperaLogEntity> operationLog(PackTechSpecPageDto pageDto) {
        QueryWrapper<OperaLogEntity> qw = new QueryWrapper<>();
        //"'资料包-'+#search.packType+'-'+#search.foreignId+'-工艺说明-'+#search.specType"
        String path = SpElParseUtil.generateKeyBySpEL(PackTechSpecService.pathSqEL, MapUtil.of("p0", pageDto));
        qw.eq("path", path);
        qw.orderByDesc("id");
        Page<OperaLogEntity> objects = PageHelper.startPage(pageDto);
        operaLogService.list(qw);
        return objects.toPageInfo();
    }

// 自定义方法区 不替换的区域【other_end】

}
