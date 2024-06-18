package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailDimensionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailType;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ReplayRatingDetailList extends ArrayList<ReplayRatingDetailDTO> {

    public ReplayRatingDetailList(ReplayRatingDetailType replayRatingDetailType) {
        super();
    }

    public ReplayRatingDetailList(@NotNull Collection<ReplayRatingDetailDTO> c) {
        super(c);
    }

    public List<ReplayRatingDetailDimensionType> improveDimensionList() {
        if (CollUtil.isEmpty(this)) return new ArrayList<>();
        return this.stream().map(ReplayRatingDetailDTO::getDimensionType).collect(Collectors.toList());
    }
}