package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.base.sbc.module.patternmaking.vo.SampleBoardVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.entity.BiColorway;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.mapper.BiSampleMapper;
import com.base.sbc.open.service.BiSampleService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/8/24 16:26:28
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiSampleServiceImpl extends ServiceImpl<BiSampleMapper, BiSample> implements BiSampleService {
    private final PatternMakingService patternMakingService;
    private final StyleService styleService;

    /**
     *
     */
    @Override
    public void sample() {
        List<BiSample> list = new ArrayList<>();
        PatternMakingCommonPageSearchDto pageSearchDto = new PatternMakingCommonPageSearchDto();
        pageSearchDto.setPageNum(1);
        pageSearchDto.setPageSize(9999);
        for (SampleBoardVo sampleBoardVo : patternMakingService.sampleBoardList(pageSearchDto).getList()) {
            PatternMaking patternMaking = patternMakingService.getById(sampleBoardVo.getId());
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

            Style style = styleService.getById(patternMaking.getStyleId());
            BiSample biSample = new BiSample();
            biSample.setSampleName((StringUtils.isNotEmpty(patternMaking.getPatternNo()) ? patternMaking.getPatternNo() : style.getStyleNo()) + "/" + patternMaking.getSampleType());
            biSample.setC8SamplePaperPatternScore(sampleBoardVo.getPatternMakingScore());
            biSample.setC8SampleDesignerScore(patternMaking.getPatternMakingScore());
            biSample.setC8ProductSampleCutterStartDate(nodeStatusVo9.getStartDate());
            biSample.setC8ProductSampleCutter(patternMaking.getCutterName());
            biSample.setC8ProductSampleCutterFinDate(nodeStatusVo10.getEndDate());
            biSample.setSamplePOProducts(null);
            biSample.setSamplePOColors(patternMaking.getColorName());
            biSample.setC8ProductSampleReferenceCategory(null);
            biSample.setC8SampleChaBanData(null);
            biSample.setSampleSRLineItem(null);
            biSample.setParent(null);
            biSample.setC8ProductSampleActStartData(nodeStatusVo13.getStartDate());
            biSample.setC8ProductSampleSweiningFinData(nodeStatusVo11.getEndDate());
            biSample.setProductSize(patternMaking.getSize());
            biSample.setC8ProductSampleProofingDesigner(null);
            biSample.setProofingDesignerID(null);
            biSample.setC8SampleSampleQty(sampleBoardVo.getRequirementNum());
            biSample.setC8SampleFangMaData(null);
            biSample.setC8ProductSampleFangMaShi(null);
            biSample.setResponsibleUsers(null);
            biSample.setC8SampleCauseOfReversion(null);
            biSample.setC8SamplePriceCost(null);
            biSample.setC8SampleTechPackData(null);
            biSample.setC8ProductSampleProComment(null);
            biSample.setC8ProductSamplePlanStartData(null);
            biSample.setC8SamplePriceIncomet(null);
            biSample.setSampleReceivedDate(null);
            biSample.setSampleType(patternMaking.getSampleType());
            biSample.setC8SampleMaterialInfo(null);
            biSample.setC8SampleMaterialInfo2(null);
            biSample.setC8SampleMaterialDetData(null);
            biSample.setProductColor(null);
            biSample.setC8ProductSampleSamplingDate(nodeStatusVo8.getEndDate());
            biSample.setC8SampleTechIfQitao(null);
            biSample.setSampleNotes(null);
            // 收到正确样日期
            biSample.setC8ProductSampleMatLackNote(null);

            // 数据表单
            biSample.setC8SampleIfQitao(null);

            // 实际收到数量
            biSample.setC8SampleSampleRecQty(null);

            // 收到正确样日期
            biSample.setC8SampleRecivedCorrectData(null);

            // 样衣条码
            biSample.setSampleDataSheets(null);

            // 替代产品
            biSample.setSampleProductAlternative(null);

            // 完成件数
            biSample.setC8ProductSampleSampleFinQty(null);

            // 下发给版师时间
            biSample.setC8ProductSampleHO2SDTime(null);

            // 下发给版师状态
            biSample.setC8ProductSampleHO2SDState(null);

            // 下发给样衣组长时间
            biSample.setC8ProductSampleHO2STime(null);

            // 下发给样衣组长状态
            biSample.setC8ProductSampleHO2SState(null);

            // 需求数量
            biSample.setRequestedQty(null);

            // 样品工厂
            biSample.setC8SampleRequestDate(null);

            // 样板号
            biSample.setC8SampleSampleNumber(null);

            // 样品存储
            biSample.setSampleStorage(null);

            // 样品存储 Bin Number
            biSample.setStorageBinNumber(null);

            // 样品存储名称
            biSample.setStorageName(null);

            // 样品工厂
            biSample.setSampleFactory(null);

            // 请求编号
            biSample.setRequestNumber(null);

            // 样衣工工作量评分
            biSample.setC8SampleSampleScore(null);

            // 样衣工质量评分
            biSample.setC8SamplePatternScore(null);

            // 样衣师
            biSample.setC8ProductSampleSeiwer(null);

            // 样衣实际完成日期
            biSample.setC8SampleSampleFinDate(null);

            // 样衣完成
            biSample.setC8SampleIfFinished(null);

            // 改版意见
            biSample.setC8SampleWhyModify(null);

            // 已创建款式
            biSample.setCreatedStyles(null);

            // 纸样完成件数
            biSample.setC8ProductSamplePatternFinQty(null);

            // 纸样完成时间
            biSample.setC8ProductSamplePatternFinData(null);

            // 状态
            biSample.setSampleStatus(null);

            // 主搭配
            biSample.setMainMaterialsList(null);

            // Dimensions
            biSample.setDimensions(null);

            // 面辅料齐套
            biSample.setC8ProductSampleMatIfQitao(null);

            // 纸样需求完成日期
            biSample.setC8ProductSamplePatternReqDate(null);

            // Style PLM ID
            biSample.setC8StylePLMID(null);

            // Colorway PLM ID
            biSample.setC8ColorwayPLMID(null);

            // 供应商 供应商编码
            biSample.setSupplierNumber(null);

            // 延迟打板原因
            biSample.setC8ProductSampleDelayedReason(null);

            // 打版难度
            biSample.setC8SamplePatDiff(null);

            // 打样顺序
            biSample.setC8SamplePatSeq(null);

            // 样品 PLM ID
            biSample.setC8SamplePLMID(null);

            // Style URL
            biSample.setC8ProductSampleStyleURL(null);

            // 样品 MC Date
            biSample.setC8SampleMCDate(null);

            // 样品 BExt Auxiliary
            biSample.setC8SampleBExtAuxiliary(null);

            // 样品 EA Valid From
            biSample.setC8SampleEAValidFrom(null);

            // 样品 EA Valid To
            biSample.setC8SampleEAValidTo(null);

            // 样品条码
            biSample.setC8SampleBarcode(null);

            // 创建时间
            biSample.setCreatedAt(null);

            // 创建人
            biSample.setCreatedBy(null);

            // 修改时间
            biSample.setModifiedAt(null);

            // 修改者
            biSample.setModifiedBy(null);

            list.add(biSample);

        }


        this.remove(null);
        this.saveBatch(list, 100);
    }
}
