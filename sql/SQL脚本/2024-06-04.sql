ALTER TABLE `s_pdm_data`.`t_pattern_making`
ADD COLUMN `kitting_reason_name` varchar(64) NULL COMMENT '是否齐套原因名称' AFTER `kitting_reason`;

UPDATE `s_pdm_data`.`t_column_define` SET `table_code` = 'sampleBoard', `column_code` = 'kittingReasonName', `column_name` = '未齐套原因', `column_name_i18n_key` = '', `hidden` = '1', `required_return` = '', `align_type` = '', `fix_type` = '', `link` = '', `is_sort` = '', `is_query` = '', `is_edit` = '', `is_required` = '', `column_type` = '', `data_format` = '', `dict_type` = '', `column_width` = 150, `sort_order` = 34, `custom_render` = '', `column_color` = '', `create_id` = '1', `create_name` = '1', `create_date` = '2023-12-07 11:10:06', `update_id` = '774671561280978944', `update_name` = '罗旭峰', `update_date` = '2024-06-04 10:45:28', `company_code` = '1', `del_flag` = '0', `export_alias` = '', `excel_replace` = NULL, `group_name` = '', `export_function` = NULL, `sql_code` = 'p.kitting_reason_name', `property` = '', `column_filter` = '', `column_filter_extent` = '', `column_ellipsis` = '', `column_merge` = NULL WHERE `id` = '156220e8a47e13da7975ee6134736c03';

-- mango吊牌增加品名翻译和颜色编码翻译字段
ALTER TABLE `s_pdm_data`.`t_hang_tag`
ADD COLUMN `product_name_translate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'mango品牌品名翻译' AFTER `lavation_reminder_code`,
ADD COLUMN `color_code_translate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'mango品牌颜色编码翻译' AFTER `product_name_translate`;