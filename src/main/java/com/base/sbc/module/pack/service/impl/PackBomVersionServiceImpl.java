/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.mapper.PackBomVersionMapper;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：资料包-物料清单-物料版本 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomVersionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:20
 */
@Service
public class PackBomVersionServiceImpl extends BaseServiceImpl<PackBomVersionMapper, PackBomVersion> implements PackBomVersionService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackBomVersionVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        Page<PackBomVersionVo> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackBomVersionVo> pageInfo = page.toPageInfo();
        PageInfo<PackBomVersionVo> voPageInfo = CopyUtil.copy(pageInfo, PackBomVersionVo.class);
        return voPageInfo;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackBomVersionVo saveVersion(PackBomVersionDto dto) {
        //新增
        if (StrUtil.isBlank(dto.getId()) || StrUtil.contains(dto.getId(), StrUtil.DASHED)) {
            PackBomVersionVo pageData = BeanUtil.copyProperties(dto, PackBomVersionVo.class);
            pageData.setId(null);
            QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
            PackUtils.commonQw(qw, dto);
            long count = count(qw);
            pageData.setVersion("BOM " + (count + 1));
            pageData.setStatus(BaseGlobal.YES);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackBomVersionVo.class);
        }
        //修改
        else {
            PackBomVersion dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            boolean b = updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackBomVersionVo.class);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean changeVersionStatus(String id) {
        PackBomVersion version = getById(id);
        if (version == null) {
            throw new OtherException("未找到版本信息");
        }
        String status = version.getStatus();

        //停用操作
        if (StrUtil.equals(status, BaseGlobal.NO)) {
            QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
            PackUtils.commonQw(qw, version);
            qw.ne("id", id);
            qw.eq("status", BaseGlobal.NO);
            long count = count(qw);
            if (count == 0) {
                throw new OtherException("必须有一个是启用的");
            }
            version.setStatus(BaseGlobal.YES);
            updateById(version);
        }
        //启用操作
        else {
            // 1.将当前启用的停用 2.启用当前的
            enable(version);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean enable(PackBomVersion version) {
        UpdateWrapper<PackBomVersion> qw = new UpdateWrapper<>();
        PackUtils.commonQw(qw, version);
        qw.eq("status", BaseGlobal.NO);
        qw.set("status", BaseGlobal.YES);
        setUpdateInfo(qw);
        update(qw);
        version.setStatus(BaseGlobal.NO);
        return updateById(version);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean lockChange(String id, String lockFlag) {
        UpdateWrapper<PackBomVersion> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("lock_flag", lockFlag);
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean toBigGoods(String id) {


        return false;
    }

// 自定义方法区 不替换的区域【other_end】

}
