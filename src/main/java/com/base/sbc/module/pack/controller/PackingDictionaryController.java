package com.base.sbc.module.pack.controller;


import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pack.dto.PackBusinessOpinionDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.dto.PackingDictionaryDto;
import com.base.sbc.module.pack.entity.PackingDictionary;
import com.base.sbc.module.pack.service.PackingDictionaryService;
import com.base.sbc.module.pack.vo.PackBusinessOpinionVo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.StylePackInfoListVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = "资料包字典")
@RequestMapping(value = BaseController.SAAS_URL + "/packTechSpec", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackingDictionaryController extends BaseController{


    @Autowired
    private PackingDictionaryService packingDictionaryService;

    @ApiOperation(value = "包装字典查询")
    @GetMapping("/packingList")
    public PageInfo<PackingDictionary> pagePacingdiction(@Valid PackingDictionaryDto packingDictionaryDto) {
        PageInfo<PackingDictionary> packingDictionaryPageInfo = packingDictionaryService.pageInfo(packingDictionaryDto);
        return packingDictionaryPageInfo;
    }

    @ApiOperation(value = "新增")
    @PostMapping("/savePacking")
    public boolean save(@RequestBody PackingDictionary dto) {
        boolean save = packingDictionaryService.save(dto);
        return save;
    }

    @ApiOperation(value = "修改")
    @PostMapping("/updatePacking")
    public ApiResult update(@RequestBody @Valid PackingDictionary dto){
        boolean update = packingDictionaryService.update(dto);
        return update ? this.updateSuccess(update): this.updateNotFound();
    }


    @ApiOperation(value = "删除")
    @PostMapping("/delPacking")
    public boolean delPacking(@RequestBody PackingDictionary dto){
        return packingDictionaryService.dele(dto);
    }

}
