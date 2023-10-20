package com.base.sbc.config;


import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：自定义大货款图和设计款图上传配置
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.config.StylePicConfig
 * @email li_xianglin@126.com
 * @date 创建时间：2023-10-20 09:29
 */
@Data
@Configuration
public class CustomStylePicUpload {
    /**
     * 开启标志
     */
    @Value("${CustomStylePicUpload.open:0}")
    private String open;
    @Value("${CustomStylePicUpload.viewUrl:http://img.eifini.com/image/index?goodsno=}")
    private String viewUrl;
    @Value("${CustomStylePicUpload.uploadPhotoUrl:http://img.eifini.com/image/UploadPhoto}")
    private String uploadPhotoUrl;
    @Value("${CustomStylePicUpload.deletePhotoUrl:http://img.eifini.com/image/DeletePhoto}")
    private String deletePhotoUrl;
    @Value("${CustomStylePicUpload.key:PDMImage}")
    private String key;
    @Value("${CustomStylePicUpload.appSecret:925091ef18f40b662a55c058cb475137}")
    private String appSecret;
    @Value("${CustomStylePicUpload.salt:eifiniEMS202307}")
    private String salt;
    /**
     * 大货款图目录
     */
    @Value("${CustomStylePicUpload.bigGoodsFolder:PDM}")
    private String bigGoodsFolder;

    /**
     * 设计款图目录
     */
    @Value("${CustomStylePicUpload.designFolder:PDM_Style}")
    private String designFolder;
    /**
     * 是否调试模式，1测试，非必填
     */
    @Value("${CustomStylePicUpload.debug:1}")
    private String debug;


    public boolean isOpen() {
        return StrUtil.equals(BaseGlobal.YES, open);
    }

}
