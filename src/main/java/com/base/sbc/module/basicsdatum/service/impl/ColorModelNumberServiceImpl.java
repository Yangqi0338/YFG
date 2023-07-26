package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.mapper.ColorModelNumberMapper;
import com.base.sbc.module.basicsdatum.service.ColorModelNumberService;
import com.base.sbc.module.basicsdatum.vo.ColorModelNumberBaseSelectVO;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/26 10:04
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class ColorModelNumberServiceImpl extends BaseServiceImpl<ColorModelNumberMapper, ColorModelNumber> implements ColorModelNumberService {

    private final CcmFeignService ccmFeignService;

    @Override
    public Boolean saveColorModelNumber(ColorModelNumber colorModelNumber) {
        if (StringUtils.isNotBlank(colorModelNumber.getId())) {
            //校验名称或者编码是否相同
            BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            ColorModelNumber one = this.getOne(queryWrapper);
            if (one != null && !one.getId().equals(colorModelNumber.getId())) {
                throw new OtherException("编码存在重复");
            }
            return this.updateById(colorModelNumber);
        } else {
            BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            ColorModelNumber one = this.getOne(queryWrapper);
            if (one != null) {
                throw new OtherException("编码存在重复");
            }
            return this.save(colorModelNumber);
        }
    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<ColorModelNumberExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ColorModelNumberExcelDto.class, params);
        list = list.stream().filter(c -> StringUtils.isNotBlank(c.getCode())).collect(Collectors.toList());
        List<ColorModelNumber> colorModelNumbers = BeanUtil.copyToList(list, ColorModelNumber.class);
        /*打版难易度数据*/
        List<ColorModelNumber> colorModelNumbers1=new ArrayList<>();
        /*字典*/
        Map<String, String> map = new HashMap<>();
        /*品类*/
        List<BasicCategoryDot> basicCategoryDotList = new ArrayList<>();
        String name = split[0];
        String type ="";
        String dict = "";
        String level ="";
        if(name.indexOf("包装袋标准")!= -1){
            type="1";
            dict = "C8_PackageType";
        }else if(name.indexOf("廓形")!= -1){
            type="2";
            level="1";
        }else if(name.indexOf("形状功能描述")!= -1){
            type="3";
            dict = "C8_Mat2ndCategory";
        }else if(name.indexOf("性别")!= -1){
            type="4";
            dict = "C8_Brand";
        }else if(name.indexOf("打样顺序")!= -1){
            type="5";
        }else if(name.indexOf("打版难易度")!= -1){
            type="6";
            dict="DevtType";
        }else if(name.indexOf("款式来源")!= -1){
            type="7";
            dict="DevtType";
        }else if(name.indexOf("淘宝分类")!= -1){
            type="8";
            level="2";
        }else if(name.indexOf("色号和型号")!= -1){
            type="9";
            dict="C8_Mat2ndCategory";
        }
        if (StringUtils.isNotBlank(dict)) {
            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap(dict);
            map = dictInfoToMap.get(dict);
        } else if(type.equals("5")){
            QueryWrapper queryWrapper =  new QueryWrapper<>();
            queryWrapper.eq("file_name","6");
            colorModelNumbers1 = baseMapper.selectList(queryWrapper);
        }else {
            basicCategoryDotList = ccmFeignService.getTreeByNamelList("品类", level);
        }

        /*拼接数据*/
        for (ColorModelNumber colorModelNumber : colorModelNumbers) {
            colorModelNumber.setFileName(type);
            if(StringUtils.isNotBlank(colorModelNumber.getMat2ndCategoryName())){
                String[] strings = colorModelNumber.getMat2ndCategoryName().replaceAll(" ","").split(",");
                List<String> stringList =new ArrayList<>();
                if(StringUtils.isNotBlank(dict)){
                    /*依赖字典*/
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if( Arrays.asList(strings).contains(value)){
                            stringList.add(key);
                        }
                    }
                }else if(type.equals("5")){
                    /*依赖打版难易度*/
                    stringList =   colorModelNumbers1.stream().filter(c -> c.getName().equals(colorModelNumber.getMat2ndCategoryName())).map(ColorModelNumber::getCode).collect(Collectors.toList());
                }else {
                    /*依赖品类*/
                   List<BasicCategoryDot> list1 = basicCategoryDotList.stream().filter(b ->  Arrays.asList(strings).contains(b.getName())).collect(Collectors.toList());
                   if (!CollectionUtils.isEmpty(list1)){
                       stringList =  list1.stream().map(BasicCategoryDot::getValue).collect(Collectors.toList());
                   }
                }
                colorModelNumber.setMat2ndCategoryId(StringUtils.join(stringList,","));
            }
            if(StringUtils.isNotBlank(colorModelNumber.getFileName())){
                QueryWrapper<ColorModelNumber> queryWrapper =new BaseQueryWrapper<>();
                queryWrapper.eq("code",colorModelNumber.getCode());
                queryWrapper.like("file_name",colorModelNumber.getFileName());
                this.saveOrUpdate(colorModelNumber,queryWrapper);
            }
        }
        return true;
    }

    @Override
    public List<ColorModelNumberBaseSelectVO> getByDistCode(String distCode, String fileName, String userCompany) {
        LambdaQueryWrapper<ColorModelNumber> queryWrapper = new LambdaQueryWrapper<ColorModelNumber>()
                .like(ColorModelNumber::getMat2ndCategoryId, distCode)
                .eq(ColorModelNumber::getFileName, fileName)
                .eq(ColorModelNumber::getCompanyCode, userCompany)
                .select(ColorModelNumber::getCode, ColorModelNumber::getName);
        List<ColorModelNumber> list = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return CopyUtil.copy(list, ColorModelNumberBaseSelectVO.class);
    }
}
