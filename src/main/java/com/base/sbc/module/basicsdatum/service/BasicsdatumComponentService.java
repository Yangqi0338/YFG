/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.AddRevampComponentDto;
import com.base.sbc.module.basicsdatum.dto.QueryComponentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumComponentVo;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumComponent;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 类描述：基础资料-部件 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumComponentService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-10 11:37:55
 */
public interface BasicsdatumComponentService extends IServicePlus<BasicsdatumComponent> {

    /**
     * 方法描述：分页查询部件
     *
     * @param queryComponentDto 查询条件
     * @return boolean
     */
    PageInfo<BasicsdatumComponentVo> getComponentList(QueryComponentDto queryComponentDto);

    /**
     * 方法描述：传入Excel导入
     *
     * @param file 文件
     * @return boolean
     */
    Boolean importExcel(MultipartFile file) throws Exception;

    /**
     * 方法描述：新增修改基础资料-部件
     *
     * @param addRevampComponentDto 部件Dto类
     * @return boolean
     */
    Boolean addRevampComponent(AddRevampComponentDto addRevampComponentDto);

    /**
     * 方法描述：删除部件数据
     *
     * @param id 部件id （多个用，）
     * @return boolean
     */
    Boolean delComponent(String id);

    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    Boolean componentStartStop( StartStopDto startStopDto);
}
