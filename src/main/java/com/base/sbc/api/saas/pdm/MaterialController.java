package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.dto.MaterialDto;
import com.base.sbc.pdm.entity.MaterialDetails;
import com.base.sbc.pdm.service.MaterialDetailsService;
import com.base.sbc.pdm.service.MaterialService;
import com.base.sbc.pdm.vo.MaterialAllVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author 卞康
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与素材库相关的所有接口信息", tags = {"素材库接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/material", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController extends BaseController {
    @Resource
    private MaterialService materialService;

    @Resource
    private MaterialDetailsService materialDetailsService;

    /**
     * 新增
     */

    @PostMapping("/add")
    @Transactional
    @ApiOperation(value = "新增素材", notes = "新增素材")
    public String add(@RequestBody MaterialDto materialDto) {
        QueryCondition qc = new QueryCondition();

        qc.andEqualTo("material_name", materialDto.getMaterial().getMaterialName());
        qc.andEqualTo("material_library", materialDto.getMaterial().getMaterialLibrary());
        Material material1 = materialService.getByCondition(qc);

        if (material1 != null) {
            throw new OtherException("已存在相同名称素材");
        }
        materialDto.getMaterial().preInsert();
        materialDto.getMaterialDetails().preInsert();
        materialService.insert(materialDto.getMaterial());
        materialDto.getMaterialDetails().setMaterialId(materialDto.getMaterial().getId());
        materialDetailsService.insert(materialDto.getMaterialDetails());
        return materialDto.getMaterial().getId();
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改素材", notes = "修改素材")
    public Integer update(@RequestBody MaterialDto materialDto) {
        Material material1 = materialService.getById(materialDto.getMaterial().getId());
        //是否修改了文件名或者所属素材库
        if (!material1.getMaterialLibrary().equals(materialDto.getMaterial().getMaterialLibrary()) || !material1.getMaterialName().equals(materialDto.getMaterial().getMaterialName())) {
            QueryCondition qc = new QueryCondition();
            qc.andEqualTo("material_name", materialDto.getMaterial().getMaterialName());
            qc.andEqualTo("material_library", materialDto.getMaterial().getMaterialLibrary());
            Material material = materialService.getByCondition(qc);

            if (material != null && !Objects.equals(material.getId(), materialDto.getMaterial().getId())) {
                throw new OtherException("已存在相同名称素材");
            }
        }
        int i = materialService.updateAll(materialDto.getMaterial());
        materialDetailsService.updateAll(materialDto.getMaterialDetails());
        return i;
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/delByIds")
    public Integer delByIds(String[] ids) {
        Material material = new Material();
        return materialService.batchdeleteByIdDelFlag(material, Arrays.asList(ids));
    }


    /**
     * 查询列表
     */
    @GetMapping("/listQuery")
    public PageInfo<MaterialDto> listQuery(MaterialAllVo materialAllVo, Page page) {
        if (materialAllVo == null) {
            throw new OtherException("参数不能为空");
        }
        QueryCondition qc = new QueryCondition();
        if (materialAllVo.getMaterialName() != null && "".equals(materialAllVo.getMaterialName())) {
            qc.andEqualTo("materialName", materialAllVo.getMaterialName());
        }
        qc.andEqualTo("material_type", materialAllVo.getMaterialType());
        qc.andEqualTo("material_library",materialAllVo.getMaterialLibrary());
        PageHelper.startPage(page);
        List<Material> materialList = materialService.findByCondition(qc);
        List<String> ids = new ArrayList<>();
        for (Material material : materialList) {
            ids.add(material.getId());
        }

        QueryCondition qc2 = new QueryCondition();
        qc2.andIn("material_id", ids);
        List<MaterialDetails> materialDetailsList = materialDetailsService.findByCondition(qc2);

        List<MaterialDto> list=new ArrayList<>();
        for (Material material : materialList) {
            MaterialDto materialDto =new MaterialDto();
            materialDto.setMaterial(material);
            for (MaterialDetails materialDetails : materialDetailsList) {
                if (material.getId().equals(materialDetails.getMaterialId())){
                    materialDto.setMaterialDetails(materialDetails);
                }
            }
            list.add(materialDto);
        }
        return new PageInfo<>(list);
    }


    /**
     * 根据id单个查询
     */
    @GetMapping("/getById")
    public MaterialDto getById(String id) {
        Material material = materialService.getById(id);
        MaterialDetails materialDetails = materialDetailsService.getById(material.getId());
        MaterialDto materialDto=new MaterialDto();
        materialDto.setMaterial(material);
        materialDto.setMaterialDetails(materialDetails);
        return materialDto;
    }
}
