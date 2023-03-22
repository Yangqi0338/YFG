package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.service.MaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author 卞康
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(tags = "1.2 SAAS接口[标签]")
@RequestMapping(value = BaseController.SAAS_URL + "/material", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController extends BaseController {
    @Resource
    private MaterialService materialService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ApiResult add(@RequestBody Material material) {
        return insertSuccess(materialService.insert(material));
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/delByIds")
    public ApiResult add(String[] ids) {
        Material material=new Material();
        return deleteSuccess(materialService.batchdeleteByIdDelFlag(material, Arrays.asList(ids)));
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ApiResult update(@RequestBody Material material) {
        return deleteSuccess(materialService.updateAll(material));
    }


    /**
     * 查询列表
     */
    @GetMapping("/list")
    public ApiResult list(Material material, Page page) {
        QueryCondition qc =new QueryCondition();
        if (material!=null &&  material.getFileName()!=null && "".equals(material.getFileName())){
            qc.andEqualTo("file_name",material.getFileName());
        }
        PageHelper.startPage(page);
        PageInfo<Material> pageInfo=new PageInfo<>(materialService.findByCondition(qc));
        return selectSuccess(pageInfo);
    }



    /**
     * 根据id单个查询
     */
    @GetMapping("/getById")
    public ApiResult getById(String id) {
      return selectSuccess(materialService.getById(id));
    }
}
