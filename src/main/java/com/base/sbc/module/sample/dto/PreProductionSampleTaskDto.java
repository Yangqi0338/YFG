package com.base.sbc.module.sample.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：产前样-任务
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样-任务 PreProductionSampleTaskDto")
public class PreProductionSampleTaskDto extends PreProductionSampleTask {
    /** 更新日期 */
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

}
