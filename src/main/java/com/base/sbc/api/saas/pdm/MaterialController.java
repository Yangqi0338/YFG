package com.base.sbc.api.saas.pdm;

import com.alibaba.fastjson2.JSONObject;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.dto.MaterialDto;
import com.base.sbc.pdm.entity.MaterialDetails;
import com.base.sbc.pdm.entity.MaterialLabel;
import com.base.sbc.pdm.service.MaterialDetailsService;
import com.base.sbc.pdm.service.MaterialLabelService;
import com.base.sbc.pdm.service.MaterialService;
import com.base.sbc.pdm.dao.MaterialAllDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private UserUtils userUtils;

    @Resource
    private MaterialLabelService materialLabelService;

    /**
     * 新增
     */

    @PostMapping("/add")
    @Transactional
    @ApiOperation(value = "新增素材", notes = "新增素材")
    public ApiResult add(@RequestBody MaterialDto materialDto) {

        materialDto.getMaterial().preInsert();
        materialDto.getMaterialDetails().preInsert();
        materialDto.getMaterial().setStatus("1");
        materialService.insert(materialDto.getMaterial());

        List<MaterialLabel> labels = materialDto.getMaterial().getLabels();
        //新增关联标签
        if (labels != null) {
            IdGen idGen = new IdGen();
            for (MaterialLabel label : labels) {
                label.setId(String.valueOf(idGen.nextId()));
                label.setMaterialId(materialDto.getMaterial().getId());
                materialLabelService.add(label);
            }
        }
        materialDto.getMaterialDetails().setMaterialId(materialDto.getMaterial().getId());
        materialDetailsService.insert(materialDto.getMaterialDetails());
        return insertSuccess(materialDto.getMaterial().getId());
    }

    /**
     * 批量新增
     */
    @PostMapping("addList")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "批量新增素材", notes = "批量新增素材")
    public ApiResult addList(@RequestBody List<Material> materialList) {
        if (materialList == null || materialList.size() == 0) {
            throw new OtherException("参数错误");
        }
        IdGen idGen = new IdGen();

        List<MaterialDetails> materialDetailsList = new ArrayList<>();
        for (Material material : materialList) {
            material.preInsert(idGen.nextIdStr());
            material.setStatus("0");
            material.setDelFlag("0");
            material.setCompanyCode(userUtils.getCompanyCode());
            MaterialDetails materialDetails = new MaterialDetails();
            materialDetails.setMaterialId(material.getId());
            materialDetails.preInsert(idGen.nextIdStr());
            materialDetailsList.add(materialDetails);

        }

        int i = materialService.batchInsert(materialList);
        materialDetailsService.batchInsert(materialDetailsList);
        return insertSuccess(i);
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改素材", notes = "修改素材")
    public ApiResult update(@RequestBody MaterialDto materialDto) {
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

        //先去删除关联标签
        materialService.delete(materialDto.getMaterial().getId());

        List<MaterialLabel> labels = materialDto.getMaterial().getLabels();
        //新增关联标签
        if (labels != null) {
            IdGen idGen = new IdGen();
            for (MaterialLabel label : labels) {
                label.setId(String.valueOf(idGen.nextId()));
                label.setMaterialId(materialDto.getMaterial().getId());
                materialLabelService.add(label);
            }
        }
        int i = materialService.updateAll(materialDto.getMaterial());
        materialDetailsService.updateAll(materialDto.getMaterialDetails());
        return updateSuccess(i);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids) {
        Material material = new Material();
        return deleteSuccess(materialService.batchdeleteByIdDelFlag(material, Arrays.asList(ids)));
    }


    /**
     * 查询列表
     */
    @GetMapping("/listQuery")
    public PageInfo<MaterialDto> listQuery(@RequestHeader("Authorization") String token, MaterialAllDto materialAllDto, Page page) {
        if (materialAllDto == null) {
            throw new OtherException("参数不能为空");
        }

        materialAllDto.setCompanyCode(userUtils.getCompanyCode());

        return materialService.listQuery(token, materialAllDto, page);
    }


    /**
     * 根据id单个查询
     */
    @GetMapping("/getById")
    public MaterialDto getById(String id) {
        Material material = materialService.getById(id);
        MaterialDetails materialDetails = materialDetailsService.getById(material.getId());
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterial(material);
        materialDto.setMaterialDetails(materialDetails);
        return materialDto;
    }
}
