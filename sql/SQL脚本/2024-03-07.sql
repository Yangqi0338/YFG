-- 用于mango品牌  状态：未上生产
ALTER TABLE `s_pdm_data`.`t_style`
ADD COLUMN `gender_type` varchar(64) NULL COMMENT '产品季性别类型' AFTER `design_marking_status`,
ADD COLUMN `gender_name` varchar(64) NULL COMMENT '产品季性别名称' AFTER `gender_type`;


--  以及字典表数据维护

INSERT INTO `c_amc_data`.`t_data_permissions_field` (`id`, `company_code`, `create_id`, `create_name`, `create_date`, `update_id`, `update_name`, `update_date`, `remarks`, `del_flag`, `business_type`, `field_remarks`, `field_name`, `field_value_source`, `field_source_default_value`) VALUES ('adcb2f9fece511eea71c005056916a64', '677447590605750272', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'technologyCenter', '版房', 'pattern_room', 'dict', 'PATTERN_ROOM');

-- 版房-PATTERN_ROOM

-- D技术部
--
-- M技术部
--
-- M工艺部
--
-- E技术部
--
-- S工艺部
--
-- AM技术部
--
-- S技术部