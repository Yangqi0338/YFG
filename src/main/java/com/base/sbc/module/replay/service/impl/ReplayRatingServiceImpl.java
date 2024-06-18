/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import com.base.sbc.module.replay.mapper.ReplayRatingMapper;
import com.base.sbc.module.replay.service.ReplayRatingDetailService;
import com.base.sbc.module.replay.service.ReplayRatingService;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingStyleVO;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.REPLAY_CV;

/**
 * 类描述：基础资料-复盘评分 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.service.ReplayRatingService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Service
public class ReplayRatingServiceImpl extends BaseServiceImpl<ReplayRatingMapper, ReplayRating> implements ReplayRatingService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private ReplayRatingDetailService replayRatingDetailService;

    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Autowired
    private StylePicUtils stylePicUtils;

    private static @NotNull BaseQueryWrapper<ReplayRating> buildQueryWrapper(ReplayRatingQO dto) {
        BaseQueryWrapper<ReplayRating> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("tsc.plan_season_id", dto.getPlanningSeasonId());
        qw.notEmptyEq("tsc.band_code", dto.getBandCode());
        qw.notEmptyEq("ts.prod_category1st", dto.getProdCategory1st());
        qw.notEmptyEq("ts.prod_category", dto.getProdCategory());
        qw.notEmptyEq("ts.prod_category2nd", dto.getProdCategory2nd());
        qw.notEmptyEq("ts.prod_category3rd", dto.getProdCategory3rd());
        qw.notEmptyEq("tsc.plan_season_id", dto.getPlanningLevel());
        qw.notEmptyEq("tsc.style_no", dto.getBulkStyleNo());
        qw.notEmptyEq("trr.rating_status", dto.getRatingFlag());

        qw.notEmptyEq("tsc.plan_season_id", dto.getRegisteringNo());
        qw.notEmptyEq("tsc.plan_season_id", dto.getSilhouette());
        qw.notEmptyEq("tsc.plan_season_id", dto.getTransferPatternFlag());
        qw.notEmptyEq("tsc.plan_season_id", dto.getMaterialCode());
        qw.notEmptyEq("tsc.plan_season_id", dto.getSupplierId());
        qw.notEmptyEq("tsc.plan_season_id", dto.getColorCode());
        qw.notEmptyEq("tsc.plan_season_id", dto.getMaterialOwnResearchFlag());

        qw.notEmptyEq("tsc.plan_season_id", dto.getSaleLevel());
        qw.notEmptyEq("tsc.plan_season_id", dto.getDesignNo());
        return qw;
    }

    @Override
    public PageInfo<? extends ReplayRatingVO> queryPageInfo(ReplayRatingQO dto) {
        Page<? extends ReplayRatingVO> page = PageHelper.startPage(dto);
        BaseQueryWrapper<ReplayRating> qw = buildQueryWrapper(dto);
        switch (dto.getType()) {
            case STYLE:
                List<ReplayRatingStyleVO> styleDTOList = baseMapper.queryStyleList(qw);
                styleDTOList.forEach(styleDTO -> {
                    // 为空获取 想要实时就去掉该空判断
                    if (styleDTO.getPlanningLevel() == null) {
//                        styleDTO.setPlanningLevel();
                    }
                    if (styleDTO.getPlanningLevel() == null) {
//                        styleDTO.setSaleLevel();
                    }
                });
                break;
            case PATTERN:
                baseMapper.queryPatternList(qw);
                break;
            case FABRIC:
                baseMapper.queryFabricList(qw);
                break;
            default:
                throw new UnsupportedOperationException("不受支持的复盘类型");
        }
        List<? extends ReplayRatingVO> list = page.getResult();
        Map<String, String> planningSeasonNameMap = planningSeasonService.mapOneField(
                new LambdaQueryWrapper<PlanningSeason>().in(PlanningSeason::getId, list.stream().map(ReplayRatingVO::getPlanningSeasonId).distinct().collect(Collectors.toList())), PlanningSeason::getId, PlanningSeason::getName);
        stylePicUtils.setStyleColorPic2(list);
        list.forEach(it -> it.setPlanningSeasonName(planningSeasonNameMap.getOrDefault(it.getPlanningSeasonId(), "")));
        return page.toPageInfo();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doSave(ReplayRatingSaveDTO replayRatingSaveDTO) {
        String id = replayRatingSaveDTO.getId();
        ReplayRating replayRating;
        if (StrUtil.isBlank(id)) {
            replayRating = REPLAY_CV.copy2Entity(replayRatingSaveDTO);
        } else {
            replayRating = Opt.ofNullable(this.getById(id)).orElseThrow(() -> new OtherException("无效的主键id"));
        }

        this.saveOrUpdate(replayRating);

        String replayRatingId = replayRating.getId();

        List<ReplayRatingDetail> replayRatingDetailList = replayRatingDetailService.list(new LambdaQueryWrapper<ReplayRatingDetail>()
                .eq(ReplayRatingDetail::getReplayRatingId, replayRatingId));

        replayRatingSaveDTO.getDetailListMap().forEach((replayRatingDetailType, replayRatingDetailListDTO) -> {
            // 获取跟进人
            List<ReplayRatingDetail> newReplayRatingDetailList = replayRatingDetailListDTO.stream().map(replayRatingDetailDTO -> {
                ReplayRatingDetail newReplayRatingDetail = replayRatingDetailList.stream()
                        .filter(it -> it.getId().equals(replayRatingDetailDTO.getId())).findFirst().orElse(new ReplayRatingDetail());
                replayRatingDetailList.remove(newReplayRatingDetail);

                replayRatingDetailDTO.setType(replayRatingDetailType);
                replayRatingDetailDTO.setReplayRatingId(replayRatingId);
                REPLAY_CV.copy(newReplayRatingDetail, replayRatingDetailDTO);
                newReplayRatingDetail.updateInit();
                return newReplayRatingDetail;
            }).collect(Collectors.toList());
            replayRatingDetailService.saveOrUpdateBatch(newReplayRatingDetailList);
        });

        if (CollUtil.isNotEmpty(replayRatingDetailList)) {
            replayRatingDetailService.removeByIds(replayRatingDetailList);
        }

        return replayRatingId;
    }

    @Override
    public ReplayRatingStyleDTO getStyleById(String id) {
        ReplayRating replayRating = getById(id);
        return null;
    }

    @Override
    public ReplayRatingPatternDTO getPatternById(String id) {
        return null;
    }

    @Override
    public ReplayRatingFabricDTO getFabricById(String id) {
        return null;
    }

// 自定义方法区 不替换的区域【other_end】

}
