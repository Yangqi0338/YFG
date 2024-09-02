/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.annotation.MessageTrigger;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.vo.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：资料包 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackInfoController
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@RestController
@Api(tags = "资料包-基础信息")
@RequestMapping(value = BaseController.SAAS_URL + "/packInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackInfoController {

    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private UserUtils userUtils;

	@Autowired
	private StyleColorService styleColorService;

	@Autowired
	private StyleService styleService;

	@Autowired
	private UploadFileService uploadFileService;

    @ApiOperation(value = "设计BOM管理列表-分页查询")
    @GetMapping
    public PageInfo<StylePackInfoListVo> pageBySampleDesign(@Valid PackInfoSearchPageDto pageDto) {
        return packInfoService.pageBySampleDesign(pageDto);
    }

    @ApiOperation(value = "设计BOM资料包-分页查询")
    @GetMapping("/packList")
    public PageInfo<PackInfoListVo> pageInfo(@Valid PackInfoSearchPageDto pageDto) {
		return packInfoService.pageInfo(pageDto);
	}

	@ApiOperation(value = "资料包明细")
	@GetMapping("/getDetail")
	public PackInfoListVo getDetail(@Valid PackInfoDetailSearchDto dto) {
		return packInfoService.getDetail(dto.getId(), dto.getPackType());
	}

	@ApiOperation(value = "资料包启用停用")
	@PostMapping("/enableFlagSetting")
	public boolean enableFlagSetting(@Valid @RequestBody EnableFlagSettingDto dto) {
		return packInfoStatusService.enableFlagSetting(dto.getForeignId(), dto.getPackType(), dto.getEnableFlag());
	}

	@ApiOperation(value = "标准资料包-分页查询")
	@GetMapping("/pageByBigGoods")
	public PageInfo<BigGoodsPackInfoListVo> pageByBigGoods(@Valid PackInfoSearchPageDto pageDto) {
		return packInfoService.pageByBigGoods(pageDto);
	}

	@ApiOperation(value = "标准资料包导出")
	@GetMapping("/pageByBigGoodsDerive")
	public void pageByBigGoodsDerive(HttpServletResponse response, @Valid PackInfoSearchPageDto pageDto) throws IOException {
		 packInfoService.pageByBigGoodsDerive(response,pageDto);
	}

	@ApiOperation(value = "新建BOM(通过款式设计)")
	@PostMapping("/createByStyle")
	public PackInfoListVo createByStyle(@Valid @RequestBody CreatePackInfoByStyleDto dto) {

		OperaLogEntity operaLogEntity =new OperaLogEntity();
		operaLogEntity.setDocumentId(dto.getId());
		operaLogEntity.setDocumentName(dto.getName());

		operaLogEntity.setParentId(dto.getPatternMakingId());
		operaLogEntity.setType("新建bom");
		operaLogEntity.setName(dto.getModelName());

		packInfoService.saveOrUpdateOperaLog(dto, new CreatePackInfoByStyleDto(), operaLogEntity);
		return packInfoService.createByStyle(dto);
	}

	@ApiOperation(value = "复制BOM")
	@PostMapping("/copyBom")
	public PackInfoListVo copyBom(@Valid @RequestBody CopyBomDto dto) {

		OperaLogEntity operaLogEntity = new OperaLogEntity();
		operaLogEntity.setDocumentId(dto.getId());
		operaLogEntity.setDocumentName(dto.getName());

		operaLogEntity.setParentId(dto.getPatternMakingId());
		operaLogEntity.setType("复制bom");
		operaLogEntity.setName(dto.getModelName());

		packInfoService.saveOrUpdateOperaLog(dto, new CreatePackInfoByStyleDto(), operaLogEntity);
		return packInfoService.copyBom(dto);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping()
	public Boolean removeById(RemoveDto removeDto) {
		return packInfoService.removeByIds(removeDto);
	}

	@ApiOperation(value = "变更日志")
	@GetMapping("/operationLog")
	public PageInfo<OperaLogEntity> operationLog(@Valid PackCommonPageSearchDto pageDto) {
		return packInfoService.operationLog(pageDto);
	}

	@ApiOperation(value = "转大货")
	@GetMapping("/toBigGoods")
	@MessageTrigger(value = "xxzx-stylepricing",code = "toBigGoods")
	public ApiResult toBigGoods(@Valid PackCommonSearchDto dto) {
		return packInfoService.toBigGoods(dto);
	}

	@ApiOperation(value = "反审")
	@GetMapping("/startReverseApproval")
	public boolean startReverseApproval(@Valid IdDto idDto) {
		return packInfoService.startReverseApproval(idDto.getId());
	}
	/**
	 * 处理反审批
	 *
	 * @param dto
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/reverseApproval")
	public boolean reverseApproval(@RequestBody AnswerDto dto) {
		return packInfoService.reverseApproval(dto);
	}


	@ApiOperation(value = "提交审核")
	@GetMapping("/startApproval")
	public boolean startApproval(@Valid IdDto idDto) {
		return packInfoService.startApproval(idDto.getId());
	}

	@ApiIgnore
	@PostMapping("/approval")
	public boolean approval(@RequestBody AnswerDto dto) {
		return packInfoService.approval(dto);
	}

	/**
	 * 核价管理选择制版单列表
	 *
	 * @param pricingSelectSearchDTO
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/pricingSelectList")
	public PageInfo<PricingSelectListVO> pricingSelectList(@RequestBody PricingSelectSearchDTO pricingSelectSearchDTO) {
		return packInfoService.pricingSelectList(pricingSelectSearchDTO);
	}


	/**
	 * 样衣id查询bom
	 *

	 * @return
	 */
	@ApiOperation(value = "查询设计款号下的bom")
	@GetMapping("/getInfoListByDesignNo")
	public PageInfo<PackInfoListVo> pageBySampleDesign(@Valid PricingSelectSearchDTO pricingSelectSearchDTO) {
		return packInfoService.getInfoListByDesignNo(pricingSelectSearchDTO.getStyleId());
	}

	@ApiOperation(value = "关联配色")
	@PostMapping("/association")
	public boolean association(@RequestBody PackInfoAssociationDto dto) {
		return packInfoService.association(dto);
	}

	@ApiOperation(value = "取消关联配色")
	@PostMapping("/cancelAssociation")
	public boolean cancelAssociation(@RequestBody PackInfoAssociationDto dto) {
		return packInfoService.cancelAssociation(dto);
	}

	@ApiOperation(value = "关联样板号")
    @PostMapping("/setPatternNo")
	@Transactional
    public boolean setPatternNo(@Validated @RequestBody PackInfoSetPatternNoDto dto) {
        return packInfoService.setPatternNo(dto);
    }

	@ApiOperation(value = "资料包数据复制")
	@GetMapping("/copyItems")
	@DuplicationCheck
	public CopyItemsVo copyItems(Principal principal, @Valid PackCopyDto dto) {
		GroupUser user = userUtils.getUserBy(principal);
		return packInfoService.copyItems(user, dto);
	}


	@GetMapping("/getBomPrint")
	@ApiOperation(value = "获取打印信息")
	public BomPrintVo getBomPrint(Principal principal, PackCommonSearchDto dto) {
		GroupUser user = userUtils.getUserBy(principal);
		return packInfoService.getBomPrint(user, dto);
	}

	@PostMapping("/updatePackInfoStatusField")
	@ApiOperation(value = "修改资料包状态表信息")
	public boolean updatePackInfoStatusField(@RequestBody PackInfoStatusVo dto) {
		return packInfoService.updatePackInfoStatusField(dto);
	}

	@PostMapping("/updatePackInfo")
	@ApiOperation(value = "修改资料包信息")
	public boolean updatePackInfo(@RequestBody PackInfoDto packInfo) {
		return packInfoService.updatePackInfo(packInfo);
	}

/*	@PostMapping("/copyBom")
	@ApiOperation(value = "复制BOM")
	public boolean copyBom(@RequestBody PackInfoStatusVo dto) {
		return packInfoService.updatePackInfoStatusField(dto);
	}*/

	/**
	 * 工艺单数据写入
	 */
	@PostMapping("/saveTechSpec")
	@ApiOperation(value = "工艺单数据写入")
	public boolean saveTechSpec() {

		//获取所有没有图片的工艺单
		QueryWrapper<PackInfoStatus> queryWrapper = new QueryWrapper<>();
		queryWrapper.isNull("tech_spec_file_id");
		queryWrapper.eq("historical_data","1");
		List<PackInfoStatus> list = packInfoStatusService.list(queryWrapper);
		for (PackInfoStatus packInfoStatus : list) {


			PackInfo packInfo = packInfoService.getById(packInfoStatus.getForeignId());
			if (packInfo == null) {
				continue;
			}
			StyleColor styleColor = styleColorService.getById(packInfo.getStyleColorId());
			if (styleColor == null) {
				continue;
			}
			Style style = styleService.getById(styleColor.getStyleId());

			if (style == null) {
				continue;
			}

			String brandName = style.getBrandName();
			String styleNo = styleColor.getStyleNo();
			UploadFile uploadFile = new UploadFile();


			String url ="http://test-mall.eifini.com:9000/pdm/DataPackage/"+brandName+"/"+style.getYearName()+"/"+styleNo+".pdf";

			uploadFile.setUrl(url);
			uploadFile.setName(styleNo+".pdf");
			uploadFileService.save(uploadFile);
			packInfoStatus.setTechSpecFileId(uploadFile.getId());
			packInfoStatusService.updateById(packInfoStatus);
		}

		return true;
	}

}
