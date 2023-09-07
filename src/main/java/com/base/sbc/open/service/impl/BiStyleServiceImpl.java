package com.base.sbc.open.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.base.sbc.module.patternmaking.vo.SampleBoardVo;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.style.dto.StylePageDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.entity.BiStyle;
import com.base.sbc.open.mapper.BiStyleMapper;
import com.base.sbc.open.service.BiStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/8/24 16:31:00
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiStyleServiceImpl extends ServiceImpl<BiStyleMapper, BiStyle> implements BiStyleService {
    private final StyleService styleService;
    private final PatternMakingService patternMakingService;
    private final PreProductionSampleTaskService preProductionSampleTaskService;

    /**
     * 样衣看板.产前样
     */
    @Override
    public void style() {
        List<BiStyle> list = new ArrayList<>();



        PatternMakingCommonPageSearchDto pageSearchDto = new PatternMakingCommonPageSearchDto();
        pageSearchDto.setPageNum(1);
        pageSearchDto.setPageSize(9999);
        for (SampleBoardVo sampleBoardVo : patternMakingService.sampleBoardList(pageSearchDto).getList()) {
            BiStyle biStyle = new BiStyle();
            PatternMaking patternMaking = patternMakingService.getById(sampleBoardVo.getId());
            Style style = styleService.getById(sampleBoardVo.getId());

            Map<String, NodeStatusVo> nodeStatus = sampleBoardVo.getNodeStatus();
            NodeStatusVo nodeStatusVo = nodeStatus.get("打版任务-已接收");
            NodeStatusVo nodeStatusVo1 = nodeStatus.get("打版任务-待接收");
            NodeStatusVo nodeStatusVo2 = nodeStatus.get("打版任务-打版中");
            NodeStatusVo nodeStatusVo3 = nodeStatus.get("打版任务-打版完成");
            NodeStatusVo nodeStatusVo4 = nodeStatus.get("技术中心-已接收");
            NodeStatusVo nodeStatusVo5 = nodeStatus.get("技术中心-版房主管下发");
            NodeStatusVo nodeStatusVo6 = nodeStatus.get("样衣任务-待分配");
            NodeStatusVo nodeStatusVo7 = nodeStatus.get("样衣任务-样衣完成");
            NodeStatusVo nodeStatusVo8 = nodeStatus.get("样衣任务-物料齐套");
            NodeStatusVo nodeStatusVo9 = nodeStatus.get("样衣任务-裁剪完成");
            NodeStatusVo nodeStatusVo10 = nodeStatus.get("样衣任务-裁剪开始");
            NodeStatusVo nodeStatusVo11 = nodeStatus.get("样衣任务-车缝完成");
            NodeStatusVo nodeStatusVo12 = nodeStatus.get("样衣任务-车缝未开始");
            NodeStatusVo nodeStatusVo13 = nodeStatus.get("样衣任务-车缝进行中");
            NodeStatusVo nodeStatusVo14 = nodeStatus.get("款式设计-设计下发");


            //产品供应商
            biStyle.setParent(null);
            // 供应商 供应商编码
            biStyle.setSupplierNumber(null);
            biStyle.setSampleName(style.getStyleName());
            // 状态
            biStyle.setSampleStatus(style.getStatus());


            /*
                打板样
             */
            biStyle.setC8ProductSampleSamplingDate(patternMaking.getSglKittingDate());
            biStyle.setC8SampleSampleQty(patternMaking.getRequirementNum());
            biStyle.setC8ProductSampleCutterStartDate(nodeStatusVo9.getStartDate());
            biStyle.setC8ProductSampleCutterFinDate(nodeStatusVo10.getEndDate());
            biStyle.setC8ProductSampleActStartData(nodeStatusVo13.getStartDate());
            biStyle.setC8ProductSampleSweiningFinData(nodeStatusVo11.getEndDate());
            biStyle.setC8SamplePaperPatternScore(patternMaking.getPatternMakingScore());
            biStyle.setSamplePOColors(patternMaking.getColorName());
            biStyle.setProductSize(patternMaking.getSize());
            biStyle.setSampleType(patternMaking.getSampleType());
            biStyle.setC8ProductSampleCutter(patternMaking.getCutterName());
            biStyle.setC8SampleDesignerScore(patternMaking.getPatternMakingScore());
            //打样设计师
            biStyle.setC8ProductSampleProofingDesigner(patternMaking.getPatternDesignerName());
            //打样设计师 用户登录
            biStyle.setProofingDesignerID(patternMaking.getPatternDesignerId());
            //样衣需求完成日期
            biStyle.setSampleNotes(patternMaking.getDemandFinishDate());
            // 样衣工工作量评分
            biStyle.setC8SampleSampleScore(patternMaking.getSampleMakingScore());
            // 样衣工质量评分
            biStyle.setC8SamplePatternScore(patternMaking.getSampleMakingQualityScore());
            //改版原因
            biStyle.setC8SampleCauseOfReversion(patternMaking.getRevisionReason());
            // 改版意见
            biStyle.setC8SampleWhyModify(patternMaking.getRevisionComments());
            // 样衣条码
            biStyle.setSampleDataSheets(patternMaking.getSampleBarCode());
            // 样板号
            biStyle.setC8SampleSampleNumber(patternMaking.getPatternNo());
            // 样品条码
            biStyle.setC8SampleBarcode(patternMaking.getSampleBarCode());
            // 打版难度
            biStyle.setC8SamplePatDiff(patternMaking.getPatDiffName());
            // 打样顺序
            biStyle.setC8SamplePatSeq(patternMaking.getPatSeqName());
            // 面辅料齐套
            biStyle.setC8ProductSampleMatIfQitao(nodeStatusVo8.getEndDate());
            // 纸样需求完成日期
            biStyle.setC8ProductSamplePatternReqDate(patternMaking.getPatternReqDate());


            /*
              产前样
             */
            PreProductionSampleTask preProductionSampleTask = preProductionSampleTaskService.getOne(new QueryWrapper<PreProductionSampleTask>().eq("style_id", style.getId()).orderByDesc("create_date").last("limit 1"));
            //放码日期
            biStyle.setC8SampleFangMaData(preProductionSampleTask.getGradingDate());
            //放码师
            biStyle.setC8ProductSampleFangMaShi(preProductionSampleTask.getGradingName());
            //工艺单完成日期
            biStyle.setC8SampleTechPackData(preProductionSampleTask.getProcessCompletionDate());
            //后技术备注说明
            biStyle.setC8ProductSampleProComment(preProductionSampleTask.getTechRemarks());


            /*
            未知字段
             */

            //采购的产品
            biStyle.setSamplePOProducts(null);
            //参考分类
            biStyle.setC8ProductSampleReferenceCategory(null);
            //查版日期
            biStyle.setC8SampleChaBanData(null);
            //产品
            biStyle.setSampleSRLineItem(null);
            //负责人
            biStyle.setResponsibleUsers(null);
            //工价/件
            biStyle.setC8SamplePriceCost(null);


            //计划开始时间
            biStyle.setC8ProductSamplePlanStartData(null);
            //计提收入
            biStyle.setC8SamplePriceIncomet(null);
            //技术收到日期
            biStyle.setSampleReceivedDate(null);
            //面辅料信息
            biStyle.setC8SampleMaterialInfo(null);
            //面辅料信息(含格式)
            biStyle.setC8SampleMaterialInfo2(null);
            //面料检测单日期
            biStyle.setC8SampleMaterialDetData(null);
            //配色
            biStyle.setProductColor(null);
            //前技术确认是否齐套
            biStyle.setC8SampleTechIfQitao(null);

            // 收到正确样日期
            biStyle.setC8ProductSampleMatLackNote(null);

            // 数据表单
            biStyle.setC8SampleIfQitao(null);

            // 实际收到数量
            biStyle.setC8SampleSampleRecQty(null);

            // 收到正确样日期
            biStyle.setC8SampleRecivedCorrectData(null);


            // 替代产品
            biStyle.setSampleProductAlternative(null);

            // 完成件数
            biStyle.setC8ProductSampleSampleFinQty(null);

            // 下发给版师时间
            biStyle.setC8ProductSampleHO2SDTime(null);

            // 下发给版师状态
            biStyle.setC8ProductSampleHO2SDState(null);

            // 下发给样衣组长时间
            biStyle.setC8ProductSampleHO2STime(null);

            // 下发给样衣组长状态
            biStyle.setC8ProductSampleHO2SState(null);

            // 需求数量
            biStyle.setRequestedQty(null);

            // 样品工厂
            biStyle.setC8SampleRequestDate(null);



            // 样品存储
            biStyle.setSampleStorage(null);

            // 样品存储 Bin Number
            biStyle.setStorageBinNumber(null);

            // 样品存储名称
            biStyle.setStorageName(null);

            // 样品工厂
            biStyle.setSampleFactory(null);

            // 请求编号
            biStyle.setRequestNumber(null);


            // 样衣师
            biStyle.setC8ProductSampleSeiwer(null);

            // 样衣实际完成日期
            biStyle.setC8SampleSampleFinDate(null);

            // 样衣完成
            biStyle.setC8SampleIfFinished(null);

            // 已创建款式
            biStyle.setCreatedStyles(null);

            // 纸样完成件数
            biStyle.setC8ProductSamplePatternFinQty(null);

            // 纸样完成时间
            biStyle.setC8ProductSamplePatternFinData(null);


            // 主搭配
            biStyle.setMainMaterialsList(null);

            // Dimensions
            biStyle.setDimensions(null);


            // Style PLM ID
            biStyle.setC8StylePLMID(null);

            // Colorway PLM ID
            biStyle.setC8ColorwayPLMID(null);


            // 延迟打板原因
            biStyle.setC8ProductSampleDelayedReason(null);



            // 样品 PLM ID
            biStyle.setC8SamplePLMID(null);

            // Style URL
            biStyle.setC8ProductSampleStyleURL(null);

            // 样品 MC Date
            biStyle.setC8SampleMCDate(null);

            // 样品 BExt Auxiliary
            biStyle.setC8SampleBExtAuxiliary(null);

            // 样品 EA Valid From
            biStyle.setC8SampleEAValidFrom(null);

            // 样品 EA Valid To
            biStyle.setC8SampleEAValidTo(null);


            /*
              通用
             */
            // 创建时间
            biStyle.setCreatedAt(patternMaking.getCreateDate());

            // 创建人
            biStyle.setCreatedBy(patternMaking.getCreateName());

            // 修改时间
            biStyle.setModifiedAt(patternMaking.getUpdateDate());

            // 修改者
            biStyle.setModifiedBy(patternMaking.getUpdateName());

            list.add(biStyle);

        }



        this.remove(null);
        this.saveBatch(list, 100);
    }
}
