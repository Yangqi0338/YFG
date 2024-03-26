-- 用于打版任务 待接收提示是否参考样衣字段
ALTER TABLE `s_pdm_data`.`t_pattern_making`
ADD COLUMN `refer_sample` int(1) NULL COMMENT '是否参考样衣(0：否，1：是)' AFTER `receive_reason`;

-- 列配置
INSERT INTO `s_pdm_data`.`t_column_define` (`id`, `table_code`, `column_code`, `column_name`, `column_name_i18n_key`, `hidden`, `required_return`, `align_type`, `fix_type`, `link`, `is_sort`, `is_query`, `is_edit`, `is_required`, `column_type`, `data_format`, `dict_type`, `column_width`, `sort_order`, `custom_render`, `column_color`, `create_id`, `create_name`, `create_date`, `update_id`, `update_name`, `update_date`, `company_code`, `del_flag`, `export_alias`, `excel_replace`, `group_name`, `export_function`, `sql_code`, `property`, `column_filter`, `column_filter_extent`, `column_ellipsis`) VALUES ('54ab2da8b6440908f438d1039d325586', 'sampleBoard', 'referSample', '是否参考样衣', '', '1', '', '', '', '', '', '', '', '', '', '', '[{\"label\":\"否\",\"value\":\"0\"},{\"label\":\"是\",\"value\":\"1\"}]', 80, 24, '', '', '1', '1', '2023-12-07 11:10:06', '1102841', '黄强', '2024-03-20 15:53:37', '1', '0', '', NULL, '', NULL, 'p.refer_sample', '', '', '', '');
