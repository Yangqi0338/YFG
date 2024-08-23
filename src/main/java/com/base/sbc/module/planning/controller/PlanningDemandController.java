/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.controller;

import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.vo.PlanningDemandVo;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：企划-需求管理相关接口
 * @address com.base.sbc.module.planning.web.PlanningDemandDimensionalityController
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18
 * @version 1.0
 */
@RestController
@Api(tags = "企划-需求管理相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningDemand", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningDemandController {

	@Resource
	private PlanningDemandService planningDemandService;

	@Resource
	private PlanningDemandProportionDataService planningDemandProportionDataService;

	@Resource
	private PlanningDimensionalityService planningDimensionalityService;

	@Autowired
	private CcmFeignService ccmFeignService;
	@Autowired
	private PlanningChannelService planningChannelService;


	@Resource
	private PlanningSeasonService planningSeasonService;

	/*品类id获取需求及维度*/
	@ApiOperation(value = "品类id获取需求列表")
	@GetMapping("/getDemandListById")
	public List<PlanningDemandVo> getDemandListById(Principal user, QueryDemandDto queryDemandDto) {
		List<PlanningDemandVo> demandListById = planningDemandService.getDemandListById(user, queryDemandDto);
		return demandListById;
	}

	/*获取穿梭框表添加需求占比数据*/
	@ApiOperation(value = "获取穿梭框表添加需求占比数据")
	@GetMapping("/getFormDemand")
	public ApiResult getFormDemand(QueryDemandDto queryDemandDto) {
		return planningDemandService.getFormDemand(queryDemandDto);
	}

	@ApiOperation(value = "删除需求占比")
	@DeleteMapping("/delDemand")
	public Boolean delDemand(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return planningDemandService.delDemand(id);
	}

	/*穿梭框新增删除需求占比数据*/
	@ApiOperation(value = "穿梭框新增删除需求占比数据")
	@PostMapping("saveDel")
	public ApiResult saveDel(@RequestBody List<SaveDelDemandDto> saveDelDemandDto) {
		return planningDemandService.saveDel(saveDelDemandDto);
	}

	/*新增修改需求占比数据表*/
	@ApiOperation(value = "批量新增修改需求占比数据表")
	@PostMapping("batchSaveUpdate")
	public List<PlanningDemandProportionData> batchSaveUpdate(@Valid @RequestBody List<SaveUpdateDemandProportionDataDto> list) {
		return planningDemandProportionDataService.batchSaveUpdate(list);
	}

	/*新增修改需求占比数据表*/
	@ApiOperation(value = "新增修改需求占比数据表")
	@PostMapping("saveUpdate")
	public ApiResult saveUpdate(@Valid @RequestBody SaveUpdateDemandProportionDataDto saveUpdateDemandDimensionalityDto) {
		return planningDemandProportionDataService.saveUpdate(saveUpdateDemandDimensionalityDto);
	}

	/*删除需求维度数据*/
	@ApiOperation(value = "删除需求维度数据")
	@DeleteMapping("/del")
	public ApiResult delPlanningBand(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return planningDemandProportionDataService.del(id);
	}


	/*维度相关接口*/


	/*获取维度列表*/
	@ApiOperation(value = "获取维度列表")
	@GetMapping("/getDimensionalityList")
	public List<PlanningDimensionality> getDimensionalityList(DimensionLabelsSearchDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getDimensionalityList(queryDemandDimensionalityDto).getPlanningDimensionalities();
	}

	@ApiOperation(value = "同步需求占比到坑位")
	@PostMapping("/syncToSeat")
	public boolean syncToSeat(@RequestBody SyncToSeatDto dto) {
		return planningDemandService.syncToSeat(dto);
	}

	/*获取穿梭框表添加维度标签*/
	@ApiOperation(value = "获取穿梭框表维度标签")
	@GetMapping("/getFormDimensionality")
	public ApiResult getFormDimensionality(DimensionLabelsSearchDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getFormDimensionality(queryDemandDimensionalityDto);
	}

	/*新增删除维度标签*/
	@ApiOperation(value = "新增删除维度标签")
	@PostMapping("/saveBatchDimensionality")
	public Boolean saveBatchDimensionality(@RequestBody List<UpdateDimensionalityDto> list) {
		return planningDimensionalityService.saveBatchDimensionality(list);
	}

	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/saveDimensionality")
	public ApiResult saveDimensionality(@Valid @RequestBody UpdateDimensionalityDto updateDimensionalityDto) {
		CheckMutexDto checkMutexDto = new CheckMutexDto();
		checkMutexDto.setChannel(updateDimensionalityDto.getChannel());
		checkMutexDto.setPlanningSeasonId(updateDimensionalityDto.getPlanningSeasonId());
		checkMutexDto.setPlanningChannelId(updateDimensionalityDto.getPlanningChannelId());
		checkMutexDto.setProdCategory(updateDimensionalityDto.getProdCategory());
		checkMutexDto.setProdCategory2nd(updateDimensionalityDto.getProdCategory2nd());
		planningDemandService.checkMutex(checkMutexDto);
		return planningDimensionalityService.saveDimensionality(updateDimensionalityDto);
	}


	/**
	 *  复制,引用维度标签
	 */
	@ApiOperation(value = "复制,引用维度标签")
	@PostMapping("/copyDimensionality")
	public List<PlanningDimensionality> copyDimensionality(@Valid @RequestBody DimensionLabelsSearchDto dto) {
		return  planningDimensionalityService.copyDimensionality(dto);
	}


	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/batchSaveDimensionality")
	public List<PlanningDimensionality> batchSaveDimensionality(@Valid @RequestBody List<UpdateDimensionalityDto> dimensionalityDtoList) {
		return planningDimensionalityService.batchSaveDimensionality(dimensionalityDtoList);
	}

	/*删除维度标签*/
	@ApiOperation(value = "删除维度标签")
	@DeleteMapping("/delDimensionality")
	public ApiResult delDimensionality(@Valid @NotBlank(message = "编号id不能为空") String id, String sortId) {
		return planningDimensionalityService.delDimensionality(id, sortId);
	}

	@ApiOperation(value = "调整顺序")
	@PostMapping("/regulateSort")
	public Boolean regulateSort(@Valid @RequestBody QueryFieldManagementDto queryFieldManagementDto) {
		return planningDimensionalityService.regulateSort(queryFieldManagementDto);
	}

	/**
	 * 设置重点维度
	 *
	 * @param planningDemand
	 * @return
	 */
	@PostMapping("/setImportantFlag")
	public boolean setImportantFlag(@RequestBody PlanningDemand planningDemand) {
		return planningDemandService.setImportantFlag(planningDemand);
	}


	@ApiOperation(value = "获取围度系数数据")
	@GetMapping("/getCoefficient")
	public List<PlanningDimensionalityVo>  getCoefficient(DimensionLabelsSearchDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getCoefficient(queryDemandDimensionalityDto);
	}


	@ApiOperation(value = "系数模板引用")
	@PostMapping("/templateReference")
	public boolean templateReference(@RequestBody DimensionLabelsSearchDto dto) {
		return planningDimensionalityService.templateReference(dto);
	}

	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/batchSaveDimensionalityNoCheck")
	public ApiResult batchSaveDimensionalityNoCheck(@RequestBody BatchSaveDimensionalityDto batchSaveDimensionalityDto) {
		//前端选择产品季
		List<UpdateDimensionalityDto> tree = batchSaveDimensionalityDto.getTree();
		//选择的字段
		List<UpdateDimensionalityDto> field = batchSaveDimensionalityDto.getField();
		//根据前端选择查询品类 或 中类
		String type = batchSaveDimensionalityDto.getType();
		/*查品类*/
		List<BasicStructureTreeVo> basicStructureTreeVoList = ccmFeignService.basicStructureTreeByCode("品类",null,"0,1,2");
		/*获取产品季渠道*/
		List<PlanningChannel> channelList = planningChannelService.list();
		Map<String, List<PlanningChannel>> channelMap = channelList.stream().collect(Collectors.groupingBy(PlanningChannel::getPlanningSeasonId));

		//取笛卡尔积
		List<UpdateDimensionalityDto> dimensionalityDtoList = new ArrayList<>();
		//遍历产品季
		for (UpdateDimensionalityDto treeDto : tree) {
			//这里调整了前端策略，需要后端查询渠道 和 品类 中类填充数据
			if(!channelMap.containsKey(treeDto.getPlanningSeasonId())){
				continue;
			}
			List<PlanningChannel> planningChannels = channelMap.get(treeDto.getPlanningSeasonId());
			//遍历渠道
			for (PlanningChannel planningChannel : planningChannels) {
				//遍历大类
				for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVoList) {
					//遍历品类
					List<BasicStructureTreeVo> children = basicStructureTreeVo.getChildren();
					for (BasicStructureTreeVo child : children) {
						if("1".equals(type)){
							for (UpdateDimensionalityDto fieldDto : field) {
								UpdateDimensionalityDto dto = getUpdateDimensionalityDto(treeDto, planningChannel, basicStructureTreeVo, child, null, fieldDto);
								dimensionalityDtoList.add(dto);
							}
						}else{
							//遍历中类
							List<BasicStructureTreeVo> children1 = child.getChildren();
							for (BasicStructureTreeVo child1 : children1) {
								for (UpdateDimensionalityDto fieldDto : field) {
									UpdateDimensionalityDto dto = getUpdateDimensionalityDto(treeDto, planningChannel, basicStructureTreeVo, child, child1, fieldDto);
									dimensionalityDtoList.add(dto);
								}
							}
						}

					}
				}
			}
		}
		return planningDimensionalityService.batchSaveDimensionalityNoCheck(dimensionalityDtoList);
	}

	private static UpdateDimensionalityDto getUpdateDimensionalityDto(UpdateDimensionalityDto treeDto, PlanningChannel planningChannel, BasicStructureTreeVo basicStructureTreeVo, BasicStructureTreeVo child, BasicStructureTreeVo child1, UpdateDimensionalityDto fieldDto) {
		UpdateDimensionalityDto dto = new UpdateDimensionalityDto();
		//产品季
		dto.setPlanningSeasonId(treeDto.getPlanningSeasonId());
		dto.setPlanningSeasonName(treeDto.getPlanningSeasonName());
		//渠道
		dto.setChannel(planningChannel.getChannel());
		dto.setChannelName(planningChannel.getChannelName());
		//大类
		dto.setProdCategory1st(basicStructureTreeVo.getValue());
		dto.setProdCategory1stName(basicStructureTreeVo.getName());
		//品类
		dto.setProdCategory(child.getValue());
		dto.setProdCategoryName(child.getName());
		//中类
		if(child1 != null){
			dto.setProdCategory2nd(child1.getValue());
			dto.setProdCategory2ndName(child1.getName());
		}

		//字段
		dto.setFieldId(fieldDto.getFieldId());
		dto.setDimensionalityName(fieldDto.getDimensionalityName());
		dto.setDesignShowFlag(fieldDto.getDesignShowFlag());
		dto.setDesignExamineFlag(fieldDto.getDesignExamineFlag());
		dto.setResearchShowFlag(fieldDto.getResearchShowFlag());
		dto.setResearchExamineFlag(fieldDto.getResearchExamineFlag());
		dto.setReplayShowFlag(fieldDto.getReplayShowFlag());
		dto.setReplayExamineFlag(fieldDto.getReplayExamineFlag());
		dto.setCoefficientFlag(fieldDto.getCoefficientFlag());
		dto.setSort(fieldDto.getSort());
		dto.setGroupSort(fieldDto.getGroupSort());
		dto.setStatus(fieldDto.getStatus());
		return dto;
	}

	@ApiOperation(value = "获取物料库字段数据")
	@GetMapping("/getMaterialCoefficient")
	public List<PlanningDimensionalityVo> getMaterialCoefficient(DimensionLabelsSearchDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getMaterialCoefficient(queryDemandDimensionalityDto);
	}

	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/batchSaveMaterial")
	public List<PlanningDimensionality> batchSaveMaterial(@Valid @RequestBody List<UpdateDimensionalityDto> dimensionalityDtoList) {
		return planningDimensionalityService.batchSaveMaterial(dimensionalityDtoList);
	}

}
