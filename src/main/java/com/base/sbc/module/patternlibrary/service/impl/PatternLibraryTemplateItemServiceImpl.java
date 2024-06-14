package com.base.sbc.module.patternlibrary.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplateItem;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateItemMapper;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import org.springframework.stereotype.Service;

/**
 * 版型库-模板表-子表 服务实现类
 *
 * @author xhte
 * @create 2024-03-25
 */
@Service
public class PatternLibraryTemplateItemServiceImpl extends ServiceImpl<PatternLibraryTemplateItemMapper, PatternLibraryTemplateItem> implements PatternLibraryTemplateItemService {

}
