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
            biSample.setC8SamplePaperPatternScore(null);
            biSample.setC8SampleDesignerScore(patternMaking.getPatternMakingScore());
            biSample.setC8ProductSampleCutterStartDate(null);
            biSample.setC8ProductSampleCutter(patternMaking.getCutterName());
            biSample.setC8ProductSampleCutterFinDate(null);
            biSample.setSamplePOProducts(null);
            biSample.setSamplePOColors(patternMaking.getColorName());
            biSample.setC8ProductSampleReferenceCategory(null);
            biSample.setC8SampleChaBanData(null);
            biSample.setSampleSRLineItem(null);
            biSample.setParent(null);
            biSample.setC8ProductSampleActStartData(null);
            biSample.setC8ProductSampleSweiningFinData(null);
            biSample.setProductSize(patternMaking.getSize());
            biSample.setC8ProductSampleProofingDesigner(null);
            biSample.setProofingDesignerID(null);
            biSample.setC8SampleSampleQty(null);
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
            biSample.setC8ProductSampleSamplingDate(null);
            biSample.setC8SampleTechIfQitao(null);


            list.add(biSample);

        }


        this.remove(null);
        this.saveBatch(list, 100);
    }
}
