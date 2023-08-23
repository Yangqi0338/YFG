package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizePdfVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-19 13:54
 */
@Data
@ApiModel("资料包-尺寸表PdfVo PackSizePdfVo")
public class PackSizePdfVo extends PackSize {


    private List<PackSizeDetail> details;

}
