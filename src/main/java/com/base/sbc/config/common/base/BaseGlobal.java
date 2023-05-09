package com.base.sbc.config.common.base;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public class BaseGlobal {
    /**
     * 当前对象实例
     */
    private static BaseGlobal global = new BaseGlobal();


    /**
     * 默认主键字段为ID
     */
    public static final String ID = "ID";
    /**
     * 非法主键值 -1
     */
    public static final String ID_INVALID = "-1";
    /**
     * 0
     */
    public static final int ZERO = 0;

    /**
     * 1
     */
    public static final Integer ONE = 1;

    /**横杠 */
    public static final String H = "-";
    /**
     * 逗号
     */
    public static final String D = ",";
    /**
     * 点
     */
    public static final String DI = ".";
    /**
     * 斜杠
     */
    public static final String G = "/";
    /**
     * 未验收
     */
    public static final String IS_CHECK_NO = "0";
    /**
     * 单据状态草稿
     */
    public static final String STOCK_STATUS_DRAFT = "0";
    /**
     * 单据状态待审核
     */
    public static final String STOCK_STATUS_WAIT_CHECK = "1";
    /**
     * 单据状态审核通过
     */
    public static final String STOCK_STATUS_CHECKED = "2";
    /**
     * 单据状态驳回
     */
    public static final String STOCK_STATUS_REJECT = "-1";
    /***
     * 状态正常
     */
    public static final String STATUS_NORMAL = "0";
    /***
     * 状态关闭
     */
    public static final String STATUS_CLOSE = "1";

    /**
     * 删除标记0：正常
     */
    public static final String DEL_FLAG_NORMAL = "0";

    /**
     * 删除标记 1：删除；）
     */
    public static final String DEL_FLAG_DELETE = "1";

    /**
     * 删除标记0：正常
     */
    public static final String DEL_FLAG_NORMAL_CN = "启用";

    /**
     * 删除标记 1：删除；）
     */
    public static final String DEL_FLAG_DELETE_CN = "停用";


    /**
     * IE
     */
    public static final String BROWSER_IE = "IE";
    /**
     * 火狐
     */
    public static final String BROWSER_FIREFOX = "Firefox";
    /**
     * 欧派
     */
    public static final String BROWSER_OPERA = "Opera";
    /**
     * 其他浏览器
     */
    public static final String BROWSER_ELSE = "Else";


    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 是否子节点
     */
    public static final String ISLEAF = "0";
    public static final String NOTLEAF = "1";

    /**
     * 产品状态在库
     */
    public static final String IN = "0";
    /**
     * 产品状态不在库
     */
    public static final String OUT = "1";
    /**
     * 产品状态已出库待确认
     */
    public static final String OUT_READY = "2";
    /**
     * 产品状态已入库待确认
     */
    public static final String IN_READY = "3";

    /**批次状态3.待入库确认 2出库完成 1 待出库确认 0入库完成*/
    /**
     * 批次状态3 出库待确认
     */
    public static final String WAIT_OUT = "3";
    /**
     * 批次状态-1待入库
     */
    public static final String WAIT_FOR_IN = "-1";
    /**
     * 批次状态 2出库完成
     */
    public static final String OUT_SUCCESS = "2";
    /**
     * 批次状态 1 入库待确认
     */
    public static final String WAIT_IN = "1";
    /**
     * 批次状态 0入库完成
     */
    public static final String IN_SUCCESS = "0";

    /**
     * 获取当前对象实例
     */
    public static BaseGlobal getInstance() {
        return global;
    }
}
