package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.common.service.IServicePlus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 卞康
 * @date 2023/6/5 9:23:54
 * @mail 247967116@qq.com
 */
public interface ProcessDatabaseService extends IServicePlus<ProcessDatabase> {
    Boolean importExcel(MultipartFile file) throws Exception;
}
