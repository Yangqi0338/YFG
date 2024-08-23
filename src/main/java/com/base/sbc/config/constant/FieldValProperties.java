package com.base.sbc.config.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(FieldValProperties.PREFIX)
public class FieldValProperties {
    public static final String PREFIX = "field-val";

    public static String silhouette = "廓形及代码";
    public static String colorSystem = "色系";
    public static String fabricComposition = "廓形及代码";
    public static String positioning = "廓形及代码";

    public void setSilhouette(String silhouette) {
        FieldValProperties.silhouette = silhouette;
    }

    public void setColorSystem(String colorSystem) {
        FieldValProperties.colorSystem = colorSystem;
    }

    public void setFabricComposition(String fabricComposition) {
        FieldValProperties.fabricComposition = fabricComposition;
    }

    public void setPositioning(String positioning) {
        FieldValProperties.positioning = positioning;
    }


}
