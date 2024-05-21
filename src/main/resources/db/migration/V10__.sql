ALTER TABLE s_pdm_data.t_order_book_detail ADD online_commissioning_size varchar(1000) NULL COMMENT '线上投产尺寸';
ALTER TABLE s_pdm_data.t_order_book_detail ADD offline_commissioning_size varchar(1000) NULL COMMENT '线下投产尺寸';
ALTER TABLE s_pdm_data.t_order_book_detail ADD department varchar(64) NULL COMMENT '部门：design 设计部，offline 线下商企，online 线上商企';
ALTER TABLE s_pdm_data.t_order_book_detail ADD similar_bulk_style_no varchar(64) NULL COMMENT '参考款号';
ALTER TABLE s_pdm_data.t_order_book_detail ADD version int(5) DEFAULT 0 NULL COMMENT '版本号';
ALTER TABLE s_pdm_data.t_order_book_detail ADD online_version int(5) DEFAULT 0 NULL COMMENT '线上版本号';
ALTER TABLE s_pdm_data.t_order_book_detail ADD similar_bulk_remark varchar(255) NULL COMMENT '参考款备注';
INSERT INTO c_amc_data.t_data_permissions_field
(id, company_code, create_id, create_name, create_date, update_id, update_name, update_date, remarks, del_flag, business_type, field_remarks, field_name, field_value_source, field_source_default_value, sort)
VALUES('831113189222514688', '677447590605750272', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'style_order_book', '部门', 'department', 'dict', 'ORDER_BOOK_DEPARTMENT', 0);