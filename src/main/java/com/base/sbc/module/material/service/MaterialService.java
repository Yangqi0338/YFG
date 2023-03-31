package com.base.sbc.module.material.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.material.dto.MaterialDto;
import com.base.sbc.module.material.dao.MaterialAllDto;
import com.base.sbc.module.material.entity.Material;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：素材库 service类
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-3-24 16:26:15
 */
public interface MaterialService extends IService<Material> {

    /**
     * 条件查询
     * @param token          用户的登录凭证
     * @param materialAllDto 请求封装对象
     * @param page           分页参数
     * @return 返回的封装对象
     */
     PageInfo<MaterialDto> listQuery(String token, MaterialAllDto materialAllDto, Page page);
}
