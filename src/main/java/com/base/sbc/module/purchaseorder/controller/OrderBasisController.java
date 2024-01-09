package com.base.sbc.module.purchaseorder.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchaseorder.dto.OrderBasisDto;
import com.base.sbc.module.purchaseorder.service.OrderBasisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "订货本")
@RequestMapping(value = BaseController.SAAS_URL + "/orderBasis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class OrderBasisController extends BaseController{

    @Autowired
    private OrderBasisService orderBasisService;

    @ApiOperation(value = "订货本-大货款列表查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage(@Valid OrderBasisDto dto) {
        System.out.println(this.getUser().getUsername());
        return selectSuccess(orderBasisService.queryPage(dto));
    }

    @ApiOperation(value = "订货本产品季-新增")
    @PostMapping("/orderBasisAdd")
    public ApiResult orderBasisAdd(@Valid @RequestBody OrderBasisDto dto) {
        return insertSuccess(orderBasisService.orderBasisAdd(dto));
    }
    @ApiOperation(value = "订货本-分配人员")
    @PostMapping("/orderBasisUpdate")
    public ApiResult orderBasisUpdate(@Valid @RequestBody OrderBasisDto dto) {

        return updateSuccess(orderBasisService.orderBasisUpdate(dto));
    }

    @ApiOperation(value = "订货本-添加大货款的保存")
    @PostMapping("/orderBasisSave")
    public ApiResult orderBasisSave(@Valid @RequestBody OrderBasisDto dto) {

        return updateSuccess(orderBasisService.orderBasisSave(dto));
    }

    @ApiOperation(value = "订货本-导出")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@Valid @RequestBody OrderBasisDto dto) {

        return null;
    }

    @ApiOperation(value = "订货本-编辑")
    @PostMapping("/getCompile")
    public ApiResult getCompile(@Valid @RequestBody OrderBasisDto dto) {

        return updateSuccess(orderBasisService.getCompile(dto));
    }

    @ApiOperation(value = "订货本-详情")
    @PostMapping("/getOrderBasisById")
    public ApiResult getOrderBasisById(@Valid @RequestBody OrderBasisDto dto) {

        return selectSuccess(orderBasisService.getCompile(dto));
    }

    @ApiOperation(value = "订货本-提交审批")
    @PostMapping("/getStartApproval")
    public ApiResult getStartApproval(@Valid @NotBlank(message = "款式配色id不可为空") String id ) {
        orderBasisService.getStartApproval(id,super.getUserCompany()) ;
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "订货本-删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return deleteSuccess(false);
        }
        List<String> idList = Arrays.asList(ids.split(","));
        boolean b = orderBasisService.removeByIds(idList);
        return deleteSuccess(b);
    }
}
