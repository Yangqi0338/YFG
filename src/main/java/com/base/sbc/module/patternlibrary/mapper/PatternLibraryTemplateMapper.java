package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
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

    /**
     * 根据模板编码批量查询数据
     * @param templateCodeList 模板编码集合
     * @return 模板信息
     */
    List<PatternLibraryTemplate> listByCodes(
            @Param("templateCodeList") List<String> templateCodeList
    );

}
