/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.Technology;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：工艺信息 dto
 * @address com.base.sbc.module.sample.dto.TechnologyDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:43
 * @version 1.0
 */
@Data
@ApiModel("工艺信息 保存修改 TechnologyDto")
public class TechnologySaveDto extends Technology {


}

