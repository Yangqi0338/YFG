package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 卞康
 * @date 2023/6/27 10:44
 * @mail 247967116@qq.com
 */
public interface SpecificationService extends BaseService<Specification> {
    Boolean importExcel(MultipartFile file) throws Exception;

    boolean saveSpecification(Specification specification);
}
