package com.base.sbc.module.goodscolor.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.goodscolor.dto.GoodsColorExcelDto;
import com.base.sbc.module.goodscolor.entity.GoodsColor;
import com.base.sbc.module.goodscolor.mapper.GoodsColorMapper;
import com.base.sbc.module.goodscolor.service.GoodsColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @data 2023/4/7 10:06
 */
@Service
public class GoodsColorServiceImpl extends BaseServiceImpl<GoodsColorMapper, GoodsColor> implements GoodsColorService {

    @Autowired
    private CcmFeignService ccmFeignService;

    /**
     * 基础资料-物料颜色导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<GoodsColorExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), GoodsColorExcelDto.class, params);
        list = list.stream().filter(g -> StringUtils.isNotBlank(g.getColorCode())).collect(Collectors.toList());
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorType");
        /*色系*/
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");
        for (GoodsColorExcelDto goodsColorExcelDto : list) {
            /*色系*/
            if (StringUtils.isNotBlank(goodsColorExcelDto.getColorTypeName())) {
                for (Map.Entry<String, String> entry : mapColorType.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(goodsColorExcelDto.getColorTypeName())) {
                        goodsColorExcelDto.setColorType(key);
                        goodsColorExcelDto.setColorTypeName(value);
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(goodsColorExcelDto.getColorRgb())) {
                goodsColorExcelDto.setColorHex(StringUtils.rgbToHex(goodsColorExcelDto.getColorRgb()));
            }
        }
        List<GoodsColor> goodsColorList = BeanUtil.copyToList(list, GoodsColor.class);
        /*按颜色编码修改*/
        for (GoodsColor goodsColor : goodsColorList) {
            QueryWrapper<GoodsColor> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("color_code",goodsColor.getColorCode());
            this.saveOrUpdate(goodsColor,queryWrapper);
        }
        return true;
    }

    /**
     * 基础资料-物料颜色导出
     *
     * @param response
     * @return
     */
    @Override
    public void deriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<GoodsColor> queryWrapper=new QueryWrapper<>();
        List<GoodsColorExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), GoodsColorExcelDto.class);
        ExcelUtils.exportExcel(list,  GoodsColorExcelDto.class, "基础资料-物料颜色.xlsx",new ExportParams() ,response);
    }
}
