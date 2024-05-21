SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `s_pdm_data`.`planning_project_plank_import`  (
                                                               `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                                               `week` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '月-周别',
                                                               `wave_band` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上市波段',
                                                               `market_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上市时间',
                                                               `order_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '下单时间',
                                                               `fabric_order_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '面料下单时间',
                                                               `skc_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SKC数',
                                                               `season_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品季id',
                                                               `planning_channel_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '渠道编码',
                                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

CREATE TABLE `s_pdm_data`.`t_category_planning`  (
                                                     `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                                     `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
                                                     `channel_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码',
                                                     `channel_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道名称',
                                                     `season_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品季id',
                                                     `season_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品季名称',
                                                     `seasonal_planning_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联季节企划id',
                                                     `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0：启用，1：未启用）',
                                                     `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者名称',
                                                     `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者id',
                                                     `update_date` datetime NOT NULL COMMENT '更新日期',
                                                     `create_date` datetime NOT NULL COMMENT '创建日期',
                                                     `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称',
                                                     `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者id',
                                                     `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除；）',
                                                     `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
                                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品类企划' ROW_FORMAT = Dynamic;

CREATE TABLE `s_pdm_data`.`t_category_planning_details`  (
                                                             `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                                             `seasonal_planning_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '季节企划Id',
                                                             `seasonal_planning_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '季节企划名称',
                                                             `category_planning_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '品类企划名称',
                                                             `category_planning_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类企划Id',
                                                             `prod_category1st_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '大类名称',
                                                             `prod_category1st_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '大类编码',
                                                             `prod_category_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '品类编码',
                                                             `prod_category_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '品类名称',
                                                             `prod_category2nd_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '中类名称',
                                                             `prod_category2nd_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '中类编码',
                                                             `dimension_type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度类型的 code',
                                                             `dimension_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度系数 id',
                                                             `dimension_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度系数名称',
                                                             `dimension_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度 code',
                                                             `dimension_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度 value',
                                                             `dimensionality_grade` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级',
                                                             `dimensionality_grade_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级名称',
                                                             `dimensionality_type` tinyint(1) NULL DEFAULT 1 COMMENT '维度类型（1-品类 2-中类）',
                                                             `band_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '波段名称',
                                                             `band_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '波段编码',
                                                             `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                                                             `style_category` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式类别',
                                                             `order_time` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '下单时间',
                                                             `launch_time` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上市时间',
                                                             `skc_count` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'SKC数量',
                                                             `total` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '合计数量 合并季节企划详情到品类-中类的数据',
                                                             `number` int UNSIGNED NULL DEFAULT 0 COMMENT '具体品类企划的需求数',
                                                             `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者名称',
                                                             `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者id',
                                                             `update_date` datetime NOT NULL COMMENT '更新日期',
                                                             `create_date` datetime NOT NULL COMMENT '创建日期',
                                                             `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称',
                                                             `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者id',
                                                             `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除；）',
                                                             `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
                                                             `is_generate` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '是否已生成数据(0-否 1-是 2-已撤回)',
                                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '品类企划明细' ROW_FORMAT = Dynamic;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主键' FIRST;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `field_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字段管理id' AFTER `id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `planning_channel_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道企划id' AFTER `field_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '渠道' AFTER `planning_channel_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `channel_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '渠道名称' AFTER `channel`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `category_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类id' AFTER `channel_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `planning_season_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品季id' AFTER `category_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category1st` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '大类' AFTER `planning_season_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category1st_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '大类名称' AFTER `prod_category1st`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '品类' AFTER `prod_category1st_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '品类名称' AFTER `prod_category`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category2nd` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类code' AFTER `prod_category_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `prod_category2nd_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类名称' AFTER `prod_category2nd`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `dimensionality_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '维度名称' AFTER `prod_category2nd_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `design_show_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '设计显示标记' AFTER `dimensionality_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `design_examine_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '设计检查标记' AFTER `design_show_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `research_show_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '研发显示标记' AFTER `design_examine_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `research_examine_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '研发检查标记' AFTER `research_show_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `replay_show_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '复盘显示标记' AFTER `research_examine_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `replay_examine_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '复盘检查标记' AFTER `replay_show_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `coefficient_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '围度系数标识(0微度数据1围度系数)' AFTER `replay_examine_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `dimensionality_grade` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级' AFTER `coefficient_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `dimensionality_grade_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级名称' AFTER `dimensionality_grade`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `is_examine` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否检查' AFTER `dimensionality_grade_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `sort` int NULL DEFAULT NULL COMMENT '排序' AFTER `is_examine`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `group_sort` int NULL DEFAULT NULL COMMENT '分组排序' AFTER `sort`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态(0正常,1停用)' AFTER `group_sort`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `show_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示配置，为空是全部场景显示，不为空时根据传入条件取交集' AFTER `status`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者ID' AFTER `create_date`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称' AFTER `create_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者ID' AFTER `update_date`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者名称' AFTER `update_id`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记(0正常，1删除)' AFTER `update_name`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` MODIFY COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业编码' AFTER `del_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_dimensionality` DROP COLUMN `historical_data`;

ALTER TABLE `s_pdm_data`.`t_planning_project` COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `s_pdm_data`.`t_planning_project` ADD COLUMN `category_planning_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类企划id' AFTER `id`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id' FIRST;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `season_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品季节id' AFTER `category_planning_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `season_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品季节名称' AFTER `season_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `planning_project_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企划规划名称' AFTER `season_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `planning_channel_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道名称' AFTER `planning_project_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `planning_channel_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码' AFTER `planning_channel_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `is_match` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否已匹配:0否,1是' AFTER `planning_channel_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '1停用、0启用' AFTER `is_match`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者ID' AFTER `status`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称' AFTER `create_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者ID' AFTER `create_date`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者名称' AFTER `update_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记(0正常，1删除)' AFTER `update_date`;

ALTER TABLE `s_pdm_data`.`t_planning_project` MODIFY COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司编码' AFTER `del_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `category_planning_details_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类维度明细id' AFTER `prod_category2nd_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `dimensionality_grade` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级' AFTER `dimension_value`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `dimensionality_grade_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度等级名称' AFTER `dimensionality_grade`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `dimensionality_type` tinyint(1) NULL DEFAULT 1 COMMENT '维度类型（1-品类 2-中类）' AFTER `dimensionality_grade_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `dimension_type_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典编码' AFTER `number`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `virtual_number` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '虚拟坑位数量' AFTER `is_prod_category2nd`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` ADD COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业编号' AFTER `virtual_number`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL FIRST;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `planning_project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企划看板规划id' AFTER `id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category1st_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大类编码' AFTER `planning_project_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category1st_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大类名称' AFTER `prod_category1st_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类' AFTER `prod_category1st_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类名称' AFTER `prod_category_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category2nd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类code' AFTER `prod_category_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `prod_category2nd_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类名称' AFTER `prod_category2nd_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `dimension_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第一维度名称' AFTER `dimension_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_dimension` MODIFY COLUMN `dimension_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度值' AFTER `dimension_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project_max_category` ADD COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业编码' AFTER `number`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD COLUMN `planning_project_dimension_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对应企划维度id' AFTER `planning_project_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD COLUMN `his_design_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '旧大货款号' AFTER `matching_style_status`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL FIRST;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `planning_project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企划看板规划ID' AFTER `id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `bulk_style_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大货款号' AFTER `planning_project_dimension_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `style_color_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样衣配色id' AFTER `bulk_style_no`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片' AFTER `style_color_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `band_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '波段名称' AFTER `pic`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `band_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '波段编码' AFTER `band_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `color_system` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '色系' AFTER `band_code`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `fixed_sales` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '定销' AFTER `color_system`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `matching_style_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '匹配款式状态:(0:未匹配,1:手动匹配,2:自动匹配,3:关联历史款)' AFTER `fixed_sales`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者名称' AFTER `his_design_no`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者id' AFTER `update_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者名称' AFTER `create_date`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者id' AFTER `create_name`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除；）' AFTER `create_id`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` MODIFY COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id' AFTER `del_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD INDEX `planning_project_id`(`planning_project_id` ASC) USING BTREE;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD INDEX `planning_project_dimension_id`(`planning_project_dimension_id` ASC) USING BTREE;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD INDEX `bulk_style_no`(`bulk_style_no` ASC) USING BTREE;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD INDEX `style_color_id`(`style_color_id` ASC) USING BTREE;

ALTER TABLE `s_pdm_data`.`t_planning_project_plank` ADD INDEX `del_flag`(`del_flag` ASC) USING BTREE;

CREATE TABLE `s_pdm_data`.`t_seasonal_planning`  (
                                                     `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                                     `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
                                                     `channel_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码',
                                                     `channel_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道名称',
                                                     `season_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品季id',
                                                     `season_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品季名称',
                                                     `is_generate` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否已生产品类企划:0:否,1:是',
                                                     `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0：启用，1：未启用）',
                                                     `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '数据json',
                                                     `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者名称',
                                                     `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者id',
                                                     `update_date` datetime NOT NULL COMMENT '更新日期',
                                                     `create_date` datetime NOT NULL COMMENT '创建日期',
                                                     `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称',
                                                     `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者id',
                                                     `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除；）',
                                                     `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
                                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '季节企划' ROW_FORMAT = Dynamic;

CREATE TABLE `s_pdm_data`.`t_seasonal_planning_details`  (
                                                             `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                                             `seasonal_planning_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '季节企划Id',
                                                             `seasonal_planning_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '季节企划名称',
                                                             `prod_category1st_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大类名称',
                                                             `prod_category1st_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大类编码',
                                                             `prod_category_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类编码',
                                                             `prod_category_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类名称',
                                                             `prod_category2nd_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类名称',
                                                             `prod_category2nd_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中类编码',
                                                             `band_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '波段名称',
                                                             `band_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '波段编码',
                                                             `style_category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式类别code',
                                                             `style_category` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式类别',
                                                             `order_time` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下单时间',
                                                             `launch_time` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上市时间',
                                                             `skc_count` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'SKC数量',
                                                             `column_index` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列记录位置，用于还原排序',
                                                             `row_index` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行记录位置，用于还原排序',
                                                             `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'json数据',
                                                             `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者名称',
                                                             `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者id',
                                                             `update_date` datetime NOT NULL COMMENT '更新日期',
                                                             `create_date` datetime NOT NULL COMMENT '创建日期',
                                                             `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称',
                                                             `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者id',
                                                             `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除；）',
                                                             `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
                                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '季节企划明细' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS=1;