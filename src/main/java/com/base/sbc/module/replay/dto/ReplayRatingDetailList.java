package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailDimensionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailType;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ReplayRatingDetailList extends ArrayList<ReplayRatingDetailDTO> {

    public ReplayRatingDetailList(ReplayRatingDetailType type, ReplayRatingType replayRatingType) {
        super();
        for (ReplayRatingDetailDimensionType dimensionType : ReplayRatingDetailDimensionType.values()) {
            ReplayRatingDetailDTO ratingDetailDTO = new ReplayRatingDetailDTO();
            ratingDetailDTO.setType(type);
            ratingDetailDTO.setDimensionType(dimensionType);
            if (replayRatingType == ReplayRatingType.STYLE) {
                if (type == ReplayRatingDetailType.RATING || this.isEmpty()) {
                    this.add(ratingDetailDTO);
                }
            } else if (dimensionType.getCode().equals(replayRatingType.getCode())) {
                this.add(ratingDetailDTO);
            }
        }
    }

    public ReplayRatingDetailList(@NotNull Collection<ReplayRatingDetailDTO> c) {
        super(c);
    }

    public List<ReplayRatingDetailDimensionType> improveDimensionList() {
        if (CollUtil.isEmpty(this)) return new ArrayList<>();
        return this.stream().map(ReplayRatingDetailDTO::getDimensionType).collect(Collectors.toList());
    }
}