package com.base.sbc.api.saas.pdm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.dto.MaterialAddDto;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.service.MaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author 卞康
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与素材库相关的所有接口信息",tags={"素材库接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/material", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController extends BaseController {
    @Resource
    private MaterialService materialService;

    /**
     * 新增
     */

    @PostMapping("/add")
    @ApiOperation(value = "新增素材", notes = "新增素材")
    public String add(@RequestBody MaterialAddDto materialAddDto) {
        Material material =new Material();

        BeanUtil.copyProperties(materialAddDto,material);
        material.insertInit();
        materialService.insert(material);
        return material.getId();
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")
    public Integer update(@RequestBody Material material) {
        return materialService.updateAll(material);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids) {
        Material material=new Material();
        return deleteSuccess(materialService.batchdeleteByIdDelFlag(material, Arrays.asList(ids)));
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
