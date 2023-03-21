package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.entity.Tag;
import com.base.sbc.pdm.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 卞康
 * @date 2023/3/18 10:19:29
 */
@RestController
@Api(tags = "1.2 SAAS接口[标签]")
@RequestMapping(value = BaseController.SAAS_URL + "/tag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TagController extends BaseController {
    @Resource
    private TagService tagService;


    /**
     * 新增标签
     */
    @PostMapping("/add")
    public ApiResult add(@RequestBody Tag tag) {
        Integer i = tagService.add(tag);
        if (i == 0) {
            return insertDataRepeat("标签名称重复");
        }
        return insertSuccess("新增成功");
    }

    /**
     * 根据条件查询标签列表
     *
     * @return 标签列表
     */
    @GetMapping("/listQuery")
    public ApiResult listQuery(Tag tag, Page page) {
        tag.setCompanyCode(getUserCompany());
        PageHelper.startPage(page);
        PageInfo<Tag> pageInfo=new PageInfo<>( tagService.listQuery(tag));
        return selectSuccess(pageInfo);
    }

    /**
     * 根据主键id查询
     */
    @GetMapping("/getById")
    public ApiResult getById(String id) {
        return selectSuccess(tagService.getById(id));
    }

    /**
     * 批量删除标签
     */
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(Tag tag) {
        return deleteSuccess(tagService.delByIds(tag));
    }

    /**
     * 修改标签
     */
    @PutMapping("/update")
    public ApiResult update(@RequestBody Tag tag) {
        Integer i = tagService.update(tag);
        if (i==0){
            return  updateNotFound("名称重复");
        }
        return updateSuccess(1);
    }

}
