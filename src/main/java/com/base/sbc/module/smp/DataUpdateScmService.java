package com.base.sbc.module.smp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.mapper.StyleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述 - 用于修改下发数据后重新下发
 * ******修改成功后调用
 * @author 孟繁江
 * @date 2023/8/12 15:27:43
 * @mail XX@qq.com
 */

@Service
@RequiredArgsConstructor
public class DataUpdateScmService {


    private final StyleColorMapper styleColorMapper;

    private final StyleMapper styleMapper;

    private final BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;

    @Autowired
    @Lazy
    private SmpService smpService;

    /**
     * 方法描述 更新配色重新下发商品主数据
     * 每次修改配色信息调用该方法
     * 只会发送以下发或可编辑状态的数据
     *
     * @param styleColorId 配色id
     * @return
     */
    public void updateStyleColorSendById(String styleColorId) {
        StyleColor styleColor = styleColorMapper.selectById(styleColorId);
        if (!ObjectUtils.isEmpty(styleColor)) {
            if (styleColor.getScmSendFlag().equals(BaseGlobal.STATUS_CLOSE) || styleColor.getScmSendFlag().equals("3")) {
                smpService.goods(styleColorId.split(","));
            }
        }
    }

    /**
     * 方法描述 更新配色重新下发商品主数据
     * 每次修改配色信息调用该方法
     * 只会发送以下发或可编辑状态的数据
     *
     * @param styleNo 大货款号
     * @return
     */
    public void updateStyleColorSend(String styleNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_no", styleNo);
        List<StyleColor> styleColorList = styleColorMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(styleColorList)) {
            updateStyleColorSendById(styleColorList.get(0).getId());
        }
    }

    /**
     * 方法描述 更新款式重新下发商品主数据
     * 每次修改配色信息调用该方法
     * 发送款式下以下发或可编辑状态的数据
     *
     * @param styleId 款式id
     * @return
     */
    public void updateStyleSendById(String styleId) {
        Style style = styleMapper.selectById(styleId);
        if (!ObjectUtils.isEmpty(style)) {
            /*查询款式下的已下发及可编辑配色*/
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("style_id", style.getId());
            queryWrapper.in("scm_send_flag", StringUtils.convertList("1,3"));
            List<StyleColor> styleColorList = styleColorMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(styleColorList)) {
                List<String> stringList = styleColorList.stream().map(StyleColor::getId).collect(Collectors.toList());
                smpService.goods(stringList.toArray(new String[0]));
            }
        }
    }

    /**
     * 方法描述 更新款式重新下发商品主数据
     * 每次修改配色信息调用该方法
     * 发送款式下以下发或可编辑状态的数据
     *
     * @param designNo 款式id
     * @return
     */
    public void updateStyleSend(String designNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("design_no", designNo);
        List<Style> styleList = styleMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(styleList)) {
            updateColorSendById(styleList.get(0).getId());
        }
    }

    /**
     * 方法描述 更新颜色库重新下发商品主数据
     * 每次修改颜色库信息调用该方法
     * 只会发送以下发或可编辑状态的数据
     *
     * @param colorId 款式id
     * @return
     */
    public void updateColorSendById(String colorId) {
      BasicsdatumColourLibrary basicsdatumColourLibrary =   basicsdatumColourLibraryMapper.selectById(colorId);
      if(!ObjectUtils.isEmpty(basicsdatumColourLibrary)){
          if (basicsdatumColourLibrary.getScmSendFlag().equals(BaseGlobal.STATUS_CLOSE) || basicsdatumColourLibrary.getScmSendFlag().equals("3")) {
              smpService.color(basicsdatumColourLibrary.getId().split(","));
          }
      }

    }





}
