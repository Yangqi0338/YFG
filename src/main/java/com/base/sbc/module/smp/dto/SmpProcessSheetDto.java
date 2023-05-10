package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 13:16:10
 * @mail 247967116@qq.com
 * 工艺单
 */
@Data
public class SmpProcessSheetDto extends SmpBaseDto {
    /**大货款号*/
    private String bulkNumber;
    /**工艺制单PDF路径*/
    private String pdfUrl;
}
