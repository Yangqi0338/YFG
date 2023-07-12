package com.base.sbc.module.band.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.dto.BandExcelDto;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.mapper.BandMapper;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCategoryMeasure;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @data 2023/4/7 10:35
 */
@Service
public class BandServiceImpl extends BaseServiceImpl<BandMapper, Band> implements BandService {
    @Autowired
    private CcmFeignService ccmFeignService;
    /**
     * 导入
     *
     * @param file
     * @return
     */
    @Override
    public ApiResult bandImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BandExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BandExcelDto.class, params);
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Quarter");
        Map<String, String> map = dictInfoToMap.get("C8_Quarter");
        list =list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());
        for (BandExcelDto bandExcelDto : list) {
//            月份
            if(StringUtils.isNotBlank(bandExcelDto.getMonth())){
                int month = Integer.parseInt(bandExcelDto.getMonth());
                bandExcelDto.setMonth(month >= 10 ? String.valueOf(month)  : "0"+String.valueOf( month) );
            }
            /*季节*/
            if (StringUtils.isNotEmpty(bandExcelDto.getSeason())) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(bandExcelDto.getSeason())) {
                        bandExcelDto.setSeason(key);
                        break;
                    }
                }
            }
        }
        List<Band> bandList1 = BeanUtil.copyToList(list, Band.class);
        for (Band band : bandList1) {
            QueryWrapper<Band> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("code", band.getCode());
            this.saveOrUpdate(band, queryWrapper1);
        }
        return ApiResult.success();

    }

    /**
     * 导出
     *
     * @param response
     */
    @Override
    public void bandDeriveExcel(HttpServletResponse response) throws IOException {
        QueryWrapper<Band> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("del_flag","0");
        queryWrapper.orderByDesc("sort");
        List<BandExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BandExcelDto.class);
        ExcelUtils.exportExcel(list,  BandExcelDto.class, "基础资料-波段.xlsx",new ExportParams() ,response);
    }

    @Override
    public String getNameByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return "";
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq("code", code);
        qw.last("limit 1");
        return getBaseMapper().getNameByCode(qw);
    }

    @Override
    public Map<String, String> getNamesByCodes(String bandCodes) {
        Map<String, String> result = new HashMap<>(12);
        QueryWrapper<Band> qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in("code", StrUtil.split(bandCodes, CharUtil.COMMA));
        List<Band> bandList = list(qw);
        for (Band band : bandList) {
            result.put(band.getCode(), band.getBandName());
        }
        return result;
    }
}
