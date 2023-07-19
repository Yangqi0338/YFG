package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.SpecificationGroupDto;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/27 17:24
 * @mail 247967116@qq.com
 */
public interface SpecificationGroupService extends BaseService<SpecificationGroup> {
    boolean saveSpecification(SpecificationGroup specificationGroup);

    List<Map<String, String>> listIdName();




    /**
     * 基础资料-门幅组导入
     *
     * @param file
     * @return
     */
    Boolean specificationGroupImportExcel(MultipartFile file) throws  Exception;

    /**
     * 基础资料-门幅组导出
     *
     * @param response
     * @return
     */
    void specificationGroupDeriveExcel(HttpServletResponse response, SpecificationGroupDto specificationGroupDto) throws Exception;

}
