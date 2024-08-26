//package com.base.sbc.open.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.base.sbc.module.pack.entity.PackInfo;
//import com.base.sbc.module.pack.service.PackInfoService;
//import com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto;
//import com.base.sbc.module.patternmaking.entity.PatternMaking;
//import com.base.sbc.module.patternmaking.service.PatternMakingService;
//import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
//import com.base.sbc.module.patternmaking.vo.SampleBoardVo;
//import com.base.sbc.module.sample.entity.PreProductionSampleTask;
//import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
//import com.base.sbc.module.style.entity.Style;
//import com.base.sbc.module.style.entity.StyleColor;
//import com.base.sbc.module.style.service.StyleColorService;
//import com.base.sbc.module.style.service.StyleService;
//import com.base.sbc.open.entity.BiStyle;
//import com.base.sbc.open.mapper.BiStyleMapper;
//import com.base.sbc.open.service.BiStyleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author 卞康
// * @date 2023/8/24 16:31:00
// * @mail 247967116@qq.com
// */
//@Service
//@RequiredArgsConstructor
//public class BiStyleServiceImpl extends ServiceImpl<BiStyleMapper, BiStyle> implements BiStyleService {
//    private final StyleService styleService;
//    private final StyleColorService styleColorService;
//    private final PackInfoService packInfoService;
//    private final PatternMakingService patternMakingService;
//    private final PreProductionSampleTaskService preProductionSampleTaskService;
//
//    /**
//     * 样衣看板.产前样
//     */
//    @Override
//    public void style() {
//        List<BiStyle> list = new ArrayList<>();
//
//
//        PatternMakingCommonPageSearchDto pageSearchDto = new PatternMakingCommonPageSearchDto();
//        pageSearchDto.setPageNum(1);
//        pageSearchDto.setPageSize(9999);
//        List<SampleBoardVo> list1 =patternMakingService.sampleBoardList(pageSearchDto).getList();
//        for (SampleBoardVo sampleBoardVo :list1 ) {
//            BiStyle biStyle = new BiStyle();
//            PatternMaking patternMaking = patternMakingService.getById(sampleBoardVo.getId());
//            Style style = styleService.getById(sampleBoardVo.getStyleId());
//            if (style != null) {
//                PreProductionSampleTask preProductionSampleTask = preProductionSampleTaskService.getOne(
//                        new QueryWrapper<PreProductionSampleTask>().eq("style_id", style.getId()).
//                                orderByDesc("create_date").last("limit 1"));
//
//                if (preProductionSampleTask != null) {
//                    PackInfo packInfo = packInfoService.getById(preProductionSampleTask.getPackInfoId());
//                    if (packInfo!=null){
//                        StyleColor styleColor = styleColorService.getById(packInfo.getStyleColorId());
//                        if (styleColor != null) {
//                            //配色
//                            biStyle.setProductColor(styleColor.getColorName());
//                        }
//                    }
//                }
//
//
//                Map<String, NodeStatusVo> nodeStatus = sampleBoardVo.getNodeStatus();
//                if (nodeStatus != null) {
//                    NodeStatusVo nodeStatusVo = nodeStatus.get("打版任务-已接收");
//                    NodeStatusVo nodeStatusVo1 = nodeStatus.get("打版任务-待接收");
//                    NodeStatusVo nodeStatusVo2 = nodeStatus.get("打版任务-打版中");
//                    NodeStatusVo nodeStatusVo3 = nodeStatus.get("打版任务-打版完成");
//                    NodeStatusVo nodeStatusVo4 = nodeStatus.get("技术中心-已接收");
//                    NodeStatusVo nodeStatusVo5 = nodeStatus.get("技术中心-版房主管下发");
//                    NodeStatusVo nodeStatusVo6 = nodeStatus.get("样衣任务-待分配");
//                    NodeStatusVo nodeStatusVo7 = nodeStatus.get("样衣任务-样衣完成");
//                    NodeStatusVo nodeStatusVo8 = nodeStatus.get("样衣任务-物料齐套");
//                    NodeStatusVo nodeStatusVo9 = nodeStatus.get("样衣任务-裁剪完成");
//                    NodeStatusVo nodeStatusVo10 = nodeStatus.get("样衣任务-裁剪开始");
//                    NodeStatusVo nodeStatusVo11 = nodeStatus.get("样衣任务-车缝完成");
//                    NodeStatusVo nodeStatusVo12 = nodeStatus.get("样衣任务-车缝未开始");
//                    NodeStatusVo nodeStatusVo13 = nodeStatus.get("样衣任务-车缝进行中");
//                    NodeStatusVo nodeStatusVo14 = nodeStatus.get("款式设计-设计下发");
//                    // 纸样完成时间   打板纸样完成时间
//                    biStyle.setC8ProductSamplePatternFinData(nodeStatusVo3==null?null:nodeStatusVo3.getEndDate());
//                    biStyle.setC8ProductSampleCutterStartDate(nodeStatusVo10 == null ? null : nodeStatusVo10.getStartDate());
//                    biStyle.setC8ProductSampleCutterFinDate(nodeStatusVo9 == null ? null : nodeStatusVo9.getEndDate());
//                    // 样衣需求完成日期*
//                    biStyle.setC8SampleRequestDate(nodeStatusVo11 == null ? null : nodeStatusVo11.getEndDate());
//                    // 样衣实际完成日期   样衣完成日期
//                    biStyle.setC8SampleSampleFinDate(nodeStatusVo11 == null ? null : nodeStatusVo11.getEndDate());
//                    biStyle.setC8ProductSampleActStartData(nodeStatusVo13 == null ? null : nodeStatusVo13.getStartDate());
//                    biStyle.setC8ProductSampleSweiningFinData(nodeStatusVo11 == null ? null : nodeStatusVo11.getEndDate());
//                    // 面辅料齐套
//                    biStyle.setC8ProductSampleMatIfQitao(nodeStatusVo8 == null ? null : nodeStatusVo8.getEndDate());
//                }
//
//
//                biStyle.setSampleName(style.getStyleName());
//                // 状态
//                biStyle.setSampleStatus(style.getStatus());
//                //产品
//                biStyle.setSampleSrLineItem(style.getDesignNo());
//                // Style URL
//                biStyle.setC8ProductSampleStyleUrl(style.getId());
//
//            /*
//                打板样
//             */
//                biStyle.setC8ProductSampleSamplingDate(patternMaking.getSglKittingDate());
//                biStyle.setC8SampleSampleQty(patternMaking.getRequirementNum());
//
//                biStyle.setC8SamplePaperPatternScore(patternMaking.getPatternMakingScore());
//                biStyle.setSamplePOColors(patternMaking.getColorName());
//                biStyle.setProductSize(patternMaking.getSize());
//                biStyle.setSampleType(patternMaking.getSampleType());
//                biStyle.setC8ProductSampleCutter(patternMaking.getCutterName());
//                biStyle.setC8SampleDesignerScore(patternMaking.getPatternMakingScore());
//                //打样设计师
//                biStyle.setC8ProductSampleProofingDesigner(patternMaking.getPatternDesignerName());
//                //打样设计师 用户登录
//                biStyle.setProofingDesignerId(patternMaking.getPatternDesignerId());
//                //样衣需求完成日期
//                biStyle.setSampleNotes(patternMaking.getDemandFinishDate());
//                // 样衣工工作量评分
//                biStyle.setC8SampleSampleScore(patternMaking.getSampleMakingScore());
//                // 样衣工质量评分
//                biStyle.setC8SamplePatternScore(patternMaking.getSampleMakingQualityScore());
//                //改版原因
//                biStyle.setC8SampleCauseOfReversion(patternMaking.getRevisionReason());
//                // 改版意见
//                biStyle.setC8SampleWhyModify(patternMaking.getRevisionComments());
//                // 样衣条码
//                biStyle.setSampleDataSheets(patternMaking.getSampleBarCode());
//                // 样板号
//                biStyle.setC8SampleSampleNumber(patternMaking.getPatternNo());
//                // 样品条码
//                biStyle.setC8SampleBarcode(patternMaking.getSampleBarCode());
//                // 打版难度
//                biStyle.setC8SamplePatDiff(patternMaking.getPatDiffName());
//                // 打样顺序
//                biStyle.setC8SamplePatSeq(patternMaking.getPatSeqName());
//
//                // 纸样需求完成日期
//                biStyle.setC8ProductSamplePatternReqDate(patternMaking.getPatternReqDate());
//                //产品供应商
//                biStyle.setParent(patternMaking.getPatternRoom());
//                // 供应商 供应商编码
//                biStyle.setSupplierNumber(patternMaking.getPatternRoomId());
//                 /*
//                  通用
//                  */
//                // 创建时间
//                biStyle.setCreatedAt(patternMaking.getCreateDate());
//
//                // 创建人
//                biStyle.setCreatedBy(patternMaking.getCreateName());
//
//                // 修改时间
//                biStyle.setModifiedAt(patternMaking.getUpdateDate());
//
//                // 修改者
//                biStyle.setModifiedBy(patternMaking.getUpdateName());
//
//
//
//
//                 /*
//                   产前样
//                  */
//                if (preProductionSampleTask != null) {
//                    //放码日期
//                    biStyle.setC8SampleFangMaData(preProductionSampleTask.getGradingDate());
//                    //放码师
//                    biStyle.setC8ProductSampleFangMaShi(preProductionSampleTask.getGradingName());
//                    //工艺单完成日期
//                    biStyle.setC8SampleTechPackData(preProductionSampleTask.getProcessCompletionDate());
//                    //后技术备注说明
//                    biStyle.setC8ProductSampleProComment(preProductionSampleTask.getTechRemarks());
//                    //面辅料信息
//                    biStyle.setC8SampleMaterialInfo(preProductionSampleTask.getMaterialInfo());
//                    //前技术确认是否齐套
//                    biStyle.setC8SampleTechIfQitao("1".equals(preProductionSampleTask.getKitting()));
//                    // 样衣完成
//                    biStyle.setC8SampleIfFinished("1".equals(preProductionSampleTask.getSampleCompleteFlag()));
//                    //技术收到日期  (缺少字段) 工艺部接收正确样时间
//                    biStyle.setSampleReceivedDate(preProductionSampleTask.getTechReceiveDate());
//                    // 收到正确样日期  产前样看板
//                    biStyle.setC8SampleRecivedCorrectData(preProductionSampleTask.getTechReceiveDate());
//                    //查版日期 (工艺师查版,缺少字段)
//                    biStyle.setC8SampleChaBanData(preProductionSampleTask.getSampleChaBanData());
//                    //面料检测单日期   无字段
//                    biStyle.setC8SampleMaterialDetData(preProductionSampleTask.getMaterialInfo());
//                }
//                 /*
//                 未知字段
//                  */
//                // 完成件数 样衣完成数量
//                biStyle.setC8ProductSampleSampleFinQty(patternMaking.getRequirementNum().add(new BigDecimal(1)));
//                // 下发给版师时间 下发版师时间（指令）
//                String sampleTypeName = patternMaking.getSampleTypeName();
//                biStyle.setC8ProductSampleHo2sdTime("初版样".equals(sampleTypeName) ? patternMaking.getPrmSendDate() : patternMaking.getDesignSendDate());
//                // 下发给版师状态 已发,等待下发，空白
//                biStyle.setC8ProductSampleHo2sdState("0".equals(patternMaking.getSampleCompleteFlag()) ? "等待下发" : "已发");
//                // 下发给样衣组长时间 下发样衣组长时间（指令）
//                biStyle.setC8ProductSampleHo2sTime(patternMaking.getDemandFinishDate());
//                // 下发给样衣组长状态  状态:已发,等待下发，空白
//                biStyle.setC8ProductSampleHo2sState("0".equals(patternMaking.getSampleBarCode()) ? "等待下发" : "已发");
//                // 需求数量 样衣指令
//                biStyle.setRequestedQty(patternMaking.getRequirementNum().add(new BigDecimal(1)));
//
//
//                // 样衣师   产前样看板   车缝工
//                biStyle.setC8ProductSampleSeiwer(patternMaking.getStitcher());
//
//                // 纸样完成件数   打板纸样完成件数
//                biStyle.setC8ProductSamplePatternFinQty(patternMaking.getPatternFinishNum());
//
//                // 延迟打板原因   打板的时候
//                biStyle.setC8ProductSampleDelayedReason("1".equals(patternMaking.getBreakOffPattern()) ? "中断"+ patternMaking.getRevisionReason(): "正常");
//                //参考分类 (待定)
//                biStyle.setC8ProductSampleReferenceCategory(null);
//
//                //计提收入
//                biStyle.setC8SamplePriceIncomet(null);
//
//                //面辅料信息(含格式)
//                biStyle.setC8SampleMaterialInfo2(null);
//
//                // 缺料备注
//                biStyle.setC8ProductSampleMatLackNote(null);
//
//
//
//
//
//
//
//                /*
//                废弃字段
//                 */
//                // 请求编号
//                biStyle.setRequestNumber(null);
//                //负责人 (无)
//                biStyle.setResponsibleUsers(null);
//                //工价/件 (无)
//                biStyle.setC8SamplePriceCost(null);
//                //计划开始时间 (无)
//                biStyle.setC8ProductSamplePlanStartData(null);
//                //计提收入 (无)
//                //采购的产品  (无)
//                biStyle.setSamplePOProducts(null);
//                // 数据表单   无
//                biStyle.setC8SampleIfQitao(null);
//                // 实际收到数量  无
//                biStyle.setC8SampleSampleRecQty(null);
//                // 替代产品 无
//                biStyle.setSampleProductAlternative(null);
//                // 样品存储 无
//                biStyle.setSampleStorage(null);
//                // 样品存储 Bin Number 无
//                biStyle.setStorageBinNumber(null);
//                // 样品存储名称   无
//                biStyle.setStorageName(null);
//                // 样品工厂 无
//                biStyle.setSampleFactory(null);
//                // 已创建款式   无
//                biStyle.setCreatedStyles(null);
//                // 主搭配 无
//                biStyle.setMainMaterialsList(null);
//                // Dimensions 无
//                biStyle.setDimensions(null);
//                // 样品 PLM ID
//                biStyle.setC8SamplePLMId(null);
//                // 样品 MC Date
//                biStyle.setC8SampleMCDate(null);
//                // 样品 BExt Auxiliary
//                biStyle.setC8SampleBExtAuxiliary(null);
//                // 样品 EA Valid From
//                biStyle.setC8SampleEAValidFrom(null);
//                // 样品 EA Valid To
//                biStyle.setC8SampleEAValidTo(null);
//                // Style PLM ID
//                biStyle.setC8StylePLMId(null);
//                // Colorway PLM ID
//                biStyle.setC8ColorwayPLMId(null);
//
//
//                list.add(biStyle);
//            }
//        }
//
//
//        this.remove(null);
//        this.saveBatch(list, 100);
//    }
//}
