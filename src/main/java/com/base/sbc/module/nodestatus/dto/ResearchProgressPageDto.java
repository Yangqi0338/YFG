package com.base.sbc.module.nodestatus.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

import java.util.List;

@Data
public class ResearchProgressPageDto extends Page {
    /**关键字筛选 大货款或设计款*/
    private String search;

    /**生产类型集合 */
    private List<String> devtTypes;

    /**设计师编号集合 */
    private List<String> designerIds;

    /**版师编号集合 */
    private List<String> patternDesignIds;

    /**品牌编号集合 */
    private List<String> brandIds;

    /**产品季集合 */
    private List<String> planningSeasonIds;

    /**当前节点编号 */
    private String nodeCode;

    /**查询开始时间 */
    private String startDate;

    /**查询结束时间 */
    private String endDate;

    /**公司编号*/
    private String companyCode;

    /**模板id集合 */
    private List<String> templateIds;

}
