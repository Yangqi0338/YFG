package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.operalog.dto.OperaLogDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.time.OffsetDateTime;
import java.util.StringJoiner;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_OPERA_LOG_NAME;

/**
 * 类描述：资料包-公共筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackCommonSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("公共分页筛选条件 PackCommonSearchDto")
public class MoreLanguageOperaLogDTO extends OperaLogDto {

    @ApiModelProperty(value = "主数据id")
    private String code;

    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private CountryLanguageType countryLanguageType;

    @ApiModelProperty(value = "资料包类型:packDesign:颜色编码")
    private String standardColumnCode;

    @Override
    public void init() {
        if (StrUtil.isNotBlank(standardColumnCode)) {
            this.setDocumentCode(standardColumnCode);
        }
        StringJoiner joiner = new StringJoiner("-");
        if (StrUtil.isNotBlank(code)) {
            joiner.add(code);
        }
        if (countryLanguageType != null) {
            joiner.add(countryLanguageType.getText());
        }
        this.setContent(joiner.toString());
        if (StrUtil.isBlank(this.getName())) {
            throw new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_OPERA_LOG_NAME));
        }
        super.init();
    }
}
