package com.base.sbc.config.enums.business.workload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.common.vo.SelectOptionsChildrenVo;
import com.base.sbc.module.workload.dto.WorkloadRatingStructureDTO;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code 描述：订货本渠道类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/3/7
 */
@Getter
@AllArgsConstructor
public enum WorkloadRatingItemType {
    /**/
    ENUM("枚举") {
        @Override
        public List<SelectOptionsChildrenVo> findSelectOptionsByKey(String key) {
            List<SelectOptionsChildrenVo> result = new ArrayList<>();
            try {
                Class<?> clazz = Class.forName(key);
                if (clazz.isEnum()) {
                    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
                    List<String> fieldNames = EnumUtil.getFieldNames(enumClass);
                    if (CollUtil.containsAll(fieldNames, Arrays.asList("code", "text"))) {
                        Map<String, Object> codeMap = EnumUtil.getNameFieldMap(enumClass, "code");
                        Map<String, Object> textMap = EnumUtil.getNameFieldMap(enumClass, "text");

                        codeMap.forEach((enumName, code) -> {
                            Object text = textMap.get(enumName);
                            result.add(new SelectOptionsChildrenVo(null, text.toString(), code.toString()));
                        });
                    }
                }
            } catch (ClassNotFoundException ignored) {
            }
            return result;
        }
    },
    ARRAY("数组"),
    DICT("字典") {
        @Override
        public List<SelectOptionsChildrenVo> findSelectOptionsByKey(String key) {
            CcmFeignService ccmFeignService = SpringContextHolder.getBean("ccmFeignService");
            return ccmFeignService.getDictInfoToList(key).stream()
                    .map(it -> new SelectOptionsChildrenVo(it.getId(), it.getName(), it.getValue()))
                    .collect(Collectors.toList());
        }
    },
    STRUCTURE("结构", WorkloadRatingStructureDTO.class) {
        @Override
        public List<SelectOptionsChildrenVo> findSelectOptionsByKey(String key) {
            CcmFeignService ccmFeignService = SpringContextHolder.getBean("ccmFeignService");
            BasicCategoryDot categoryDot = new BasicCategoryDot();
            categoryDot.setId(key);
            BasicCategoryDot tree = ccmFeignService.getTreeByName(categoryDot, null);
            if (tree == null) return new ArrayList<>();
            return tree.getChildren().stream().filter(Objects::nonNull)
                    .map(it -> new SelectOptionsChildrenVo(it.getId(), it.getName(), it.getValue()).setChildrenByStructure(it.getChildren(), it.getValue()))
                    .collect(Collectors.toList());
        }
    },
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final Class<?> titleClass;

    WorkloadRatingItemType(String text) {
        this(text, null);
    }

    WorkloadRatingItemType(String text, Class<?> titleClass) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
//        this.code = this.ordinal()+"";
        this.text = text;
        this.titleClass = titleClass;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public List<SelectOptionsChildrenVo> findSelectOptionsByKey(String key) {
        return JSONUtil.toList(key, SelectOptionsChildrenVo.class);
    }
}
