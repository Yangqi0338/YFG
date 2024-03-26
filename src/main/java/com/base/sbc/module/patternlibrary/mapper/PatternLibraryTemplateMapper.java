package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplateItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版型库-模板表  Mapper 接口
 *
 * @author xhte
 * @create 2024-03-22
 */
@Mapper
public interface PatternLibraryTemplateMapper extends BaseMapper<PatternLibraryTemplate> {

}
