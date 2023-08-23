package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.style.dto.QueryStyleColorDto;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.open.entity.BiColorway;
import com.base.sbc.open.mapper.BiColorwayMapper;
import com.base.sbc.open.service.BiColorwayService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/23 9:38:50
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiColorwayServiceImpl extends ServiceImpl<BiColorwayMapper, BiColorway> implements BiColorwayService {
    private final StyleColorService styleColorService;

    /**
     *
     */
    @Override
    public void colorway() {
        List<BiColorway> list=new ArrayList<>();
        QueryStyleColorDto queryStyleColorDto=new QueryStyleColorDto();
        queryStyleColorDto.setPageNum(1);
        queryStyleColorDto.setPageSize(99999);
        PageInfo<StyleColorVo> sampleStyleColorList = styleColorService.getSampleStyleColorList(null, queryStyleColorDto);
        for (StyleColorVo styleColorVo : sampleStyleColorList.getList()) {
            BiColorway biColorway =new BiColorway();



            list.add(biColorway);
        }

        this.remove(null);
        this.saveBatch(list);
    }
}
