package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplateItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 版型库-模板表-子表  Mapper 接口
 *
 * @author xhte
 * @create 2024-03-25
 */
@Mapper
public interface PatternLibraryTemplateItemMapper extends BaseMapper<PatternLibraryTemplateItem> {

}
