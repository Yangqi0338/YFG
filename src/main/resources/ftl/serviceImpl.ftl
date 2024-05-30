/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import ${javapackage}.${project}.mapper.${className}Mapper;
import ${javapackage}.${project}.entity.${className};
import ${javapackage}.${project}.dto.${className}QueryDto;
import ${javapackage}.${project}.vo.${className}Vo;
import ${javapackage}.${project}.service.${className}Service;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
 * 类描述：${title} service类
 * @address ${javapackage}.${project}.service.${className}Service
 * @author ${author}
 * @email ${email}
 * @date 创建时间：${.now}
 * @version 1.0
 */
@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<${className}Vo> findPage(${className}QueryDto dto) {
       Page<Object> objects = PageHelper.startPage(dto);
       BaseQueryWrapper<${className}> qw = new BaseQueryWrapper<>();
       List<${className}> list = list(qw);
       return new PageInfo<>(BeanUtil.copyToList(list, ${className}Vo.class));
    }

// 自定义方法区 不替换的区域【other_end】

}
