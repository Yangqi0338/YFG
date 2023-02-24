DROP TABLE IF EXISTS t_design;/*SkipError*/
CREATE TABLE t_design(
    id VARCHAR(64) NOT NULL   COMMENT '主键' ,
    company_code VARCHAR(64) NOT NULL   COMMENT '企业编码' ,
    status VARCHAR(1) NOT NULL  DEFAULT 0 COMMENT '状态(0正常,1停用)' ,
    create_id VARCHAR(64) NOT NULL   COMMENT '创建人ID' ,
    create_name VARCHAR(64) NOT NULL   COMMENT '创建人' ,
    create_date DATETIME NOT NULL   COMMENT '创建时间' ,
    update_id VARCHAR(64) NOT NULL   COMMENT '更新人ID' ,
    update_name VARCHAR(64) NOT NULL   COMMENT '更新人' ,
    update_date DATETIME NOT NULL   COMMENT '更新时间' ,
    remarks VARCHAR(512)    COMMENT '备注' ,
    del_flag VARCHAR(1) NOT NULL  DEFAULT 0 COMMENT '删除标记(0正常1删除)' ,
    image VARCHAR(64)    COMMENT '图片' ,
    code VARCHAR(64) NOT NULL   COMMENT '设计编号' ,
    product_code VARCHAR(64)    COMMENT '款号' ,
    product_name VARCHAR(64) NOT NULL   COMMENT '款式名称' ,
    designer_id VARCHAR(64)    COMMENT '设计师ID' ,
    designer VARCHAR(64)    COMMENT '设计师' ,
    brand VARCHAR(64)    COMMENT '品牌' ,
    year VARCHAR(64) NOT NULL   COMMENT '年度' ,
    season VARCHAR(64) NOT NULL   COMMENT '季节' ,
    waviness VARCHAR(64) NOT NULL   COMMENT '波段' ,
    subject VARCHAR(64)    COMMENT '主题' ,
    series VARCHAR(64)    COMMENT '系列' ,
    model VARCHAR(64)    COMMENT '版型' ,
    category_code VARCHAR(64) NOT NULL   COMMENT '品类(产品类别)' ,
    sex VARCHAR(64)    COMMENT '性别' ,
    colors VARCHAR(64)    COMMENT '颜色' ,
    basic_yard VARCHAR(64)    COMMENT '基本码 基本码存尺码组code' ,
    size_type_code VARCHAR(64)    COMMENT '尺码组' ,
    size_codes VARCHAR(64)    COMMENT '尺码区间' ,
    unit VARCHAR(64)    COMMENT '单位' ,
    planning_type VARCHAR(64)    COMMENT '企划类型' ,
    source VARCHAR(64)    COMMENT '单据来源' ,
    source_code VARCHAR(64)    COMMENT '来源单号' ,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '设计档案 ';

ALTER TABLE t_design COMMENT '设计档案';
DROP TABLE IF EXISTS t_design_file;/*SkipError*/
CREATE TABLE t_design_file(
    id VARCHAR(64) NOT NULL   COMMENT '主键' ,
    company_code VARCHAR(64) NOT NULL   COMMENT '企业编码' ,
    create_id VARCHAR(64) NOT NULL   COMMENT '创建人ID' ,
    create_name VARCHAR(64) NOT NULL   COMMENT '创建人' ,
    create_date DATETIME NOT NULL   COMMENT '创建时间' ,
    update_id VARCHAR(64) NOT NULL   COMMENT '更新人ID' ,
    update_name VARCHAR(64) NOT NULL   COMMENT '更新人' ,
    update_date DATETIME NOT NULL   COMMENT '更新时间' ,
    remarks VARCHAR(512)    COMMENT '备注' ,
    file_name VARCHAR(64) NOT NULL   COMMENT '文件名称' ,
    url VARCHAR(1024) NOT NULL   COMMENT '地址' ,
    file_size VARCHAR(64) NOT NULL   COMMENT '大小' ,
    suffix VARCHAR(64) NOT NULL   COMMENT '后缀' ,
    page_id VARCHAR(64) NOT NULL   COMMENT '页面主表id' ,
    module_type VARCHAR(64) NOT NULL   COMMENT '模块分类' ,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '设计档案_图稿 ';

ALTER TABLE t_design_file COMMENT '设计档案_图稿';
DROP TABLE IF EXISTS t_design_color;/*SkipError*/
CREATE TABLE t_design_color(
    id VARCHAR(64) NOT NULL   COMMENT '主键' ,
    company_code VARCHAR(64) NOT NULL   COMMENT '企业编码' ,
    sort INT    COMMENT '排序' ,
    remarks VARCHAR(512)    COMMENT '备注' ,
    design_id VARCHAR(64) NOT NULL   COMMENT '设计档案ID' ,
    color VARCHAR(64) NOT NULL   COMMENT '颜色名称' ,
    color_hex VARCHAR(64)    COMMENT '颜色16进制' ,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '设计档案_颜色 ';

ALTER TABLE t_design_color COMMENT '设计档案_颜色';
DROP TABLE IF EXISTS t_design_size;/*SkipError*/
CREATE TABLE t_design_size(
    id VARCHAR(64) NOT NULL   COMMENT '主键' ,
    company_code VARCHAR(64) NOT NULL   COMMENT '企业编码' ,
    sort INT    COMMENT '排序' ,
    remarks VARCHAR(512)    COMMENT '备注' ,
    design_id VARCHAR(64) NOT NULL   COMMENT '设计档案ID' ,
    size_name VARCHAR(64) NOT NULL   COMMENT '尺码' ,
    size_code VARCHAR(64)    COMMENT '尺码编号' ,
    size_type_name VARCHAR(64)    COMMENT '尺码组' ,
    size_type_code VARCHAR(64)    COMMENT '尺码组编号' ,
    basic_yard VARCHAR(1)    COMMENT '基本码' ,
    contrast_yard VARCHAR(64)    COMMENT '对照码' ,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '设计档案_尺码 ';

ALTER TABLE t_design_size COMMENT '设计档案_尺码';
DROP TABLE IF EXISTS t_design_process;/*SkipError*/
CREATE TABLE t_design_process(
    id VARCHAR(64) NOT NULL   COMMENT '主键' ,
    company_code VARCHAR(64) NOT NULL   COMMENT '企业编码' ,
    sort INT    COMMENT '排序' ,
    remarks VARCHAR(512)    COMMENT '备注' ,
    design_id VARCHAR(64) NOT NULL   COMMENT '设计档案ID' ,
    process_code VARCHAR(64)    COMMENT '工段编码' ,
    process_name VARCHAR(64)    COMMENT '工段名称' ,
    outsource VARCHAR(1)    COMMENT '是否委外' ,
    processor VARCHAR(64)    COMMENT '加工商' ,
    unit_price DECIMAL(32,8)    COMMENT '单价' ,
    currency_code VARCHAR(64)    COMMENT '币种code' ,
    process_description VARCHAR(128)    COMMENT '工艺描述' ,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '设计档案_工段制程 ';

ALTER TABLE t_design_process COMMENT '设计档案_工段制程';
