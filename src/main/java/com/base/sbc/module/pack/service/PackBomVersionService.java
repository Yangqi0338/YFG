/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：资料包-物料清单-物料版本 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomVersionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:20
 */
public interface PackBomVersionService extends PackBaseService<PackBomVersion> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PackBomVersionVo> pageInfo(PackCommonPageSearchDto dto);


    /**
     * 新建版本
     *
     * @param dto
     * @return
     */
    PackBomVersionVo saveVersion(PackBomVersionDto dto);

    /**
     * 启用停用
     *
     * @param id
     * @return
     */
    boolean changeVersionStatus(String id);

    /**
     * 启用
     *
     * @param version
     * @return
     */
    boolean enable(PackBomVersion version);

    /**
     * 锁定解锁
     *
     * @param id
     * @param lockFlag 0正常 1锁定
     * @return
     */
    boolean lockChange(String userCompany, String id, String lockFlag);


    /**
     * 获取启用的版本
     *
     * @param dto
     * @return
     */
    PackBomVersion getEnableVersion(PackCommonSearchDto dto);

    /**
     * 获取启动版本的物料清单
     *
     * @param foreignId
     * @param packType
     * @return
     */
    List<PackBomVo> getEnableVersionBomList(String foreignId, String packType);

    /**
     * 获取启用的版本
     *
     * @param foreignId
     * @param packType
     * @return
     */
    PackBomVersion getEnableVersion(String foreignId, String packType);

    /**
     * 开始审批
     *
     * @param id
     * @return
     */
    boolean startApproval(String id);

    /**
     * 审批处理
     *
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);

    /**
     * 检查版本是否锁定
     *
     * @param id
     * @return
     */
    PackBomVersion checkVersion(String id);

    /**
     * 检查版本是否锁定
     *
     * @param id
     * @param type 0-不校验锁定 1或者空-校验锁定
     * @return
     */
    PackBomVersion checkVersion(String id, Integer type);


    /**
     * 复制
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param flg             0 正常拷贝,  1 转大货 ,2 反审
     * @param flag
     * @return
     */
    boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag, String flg, String flag);

    void checkBomDataEmptyThrowException(Collection<? extends PackBom> bomList, Collection<? extends PackBomSize> bomSizeList);

    void checkBomDataEmptyThrowException(PackBom bom);

    void checkBomSizeDataEmptyThrowException(PackBomSize bomSize);

    boolean reverseApproval(String id);

// 自定义方法区 不替换的区域【other_end】


}
