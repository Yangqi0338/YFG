package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.config.enums.business.ProcessDatabaseType;
import com.base.sbc.module.basicsdatum.dto.AddRevampProcessDatabaseDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/5 9:23:54
 * @mail 247967116@qq.com
 */
public interface ProcessDatabaseService extends BaseService<ProcessDatabase> {
    /**
     * 导入
     * @param file 文件
     * @return 成功或者失败
     */
    Boolean importExcel(MultipartFile file) throws Exception;


    /**
     * 导出
     * @param response
     * @param type
     */
    void deriveExcel(HttpServletResponse response, ProcessDatabaseType type) throws IOException;


    /**
     *
     * @param addRevampProcessDatabaseDto
     * @return
     */
    Boolean save(AddRevampProcessDatabaseDto addRevampProcessDatabaseDto);


    /**
     * 分页查询
     *
     * @param pageDto 查询条件对象
     * @return 分页对象
     */
    PageInfo<ProcessDatabase> listPage(ProcessDatabasePageDto pageDto);

    List<ProcessDatabase> list(ProcessDatabasePageDto pageDto);

    /**
     * 获取到部件中部件类别可查询的数据
     * @param type
     * @param companyCode
     * @return
     */
    List<ProcessDatabase> getQueryList(ProcessDatabasePageDto pageDto, String field);

    Map<String, String> listAllDistinct(ProcessDatabasePageDto pageDto);
}
