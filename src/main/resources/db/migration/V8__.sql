ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id' FIRST;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企划名称' AFTER `id`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌' AFTER `name`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌名称' AFTER `brand`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态(0正常,1停用)' AFTER `brand_name`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `year` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年份' AFTER `status`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `season` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '季节' AFTER `year`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `season_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '季节名称' AFTER `season`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `year_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年份名称' AFTER `season_name`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `year_name`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者ID' AFTER `remarks`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者名称' AFTER `create_id`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者ID' AFTER `create_date`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者名称' AFTER `update_id`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记(0正常，1删除)' AFTER `update_date`;

ALTER TABLE `s_pdm_data`.`t_planning_season` MODIFY COLUMN `company_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业编码' AFTER `del_flag`;

ALTER TABLE `s_pdm_data`.`t_planning_season` ADD COLUMN `create_dept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者部门' AFTER `company_code`;

ALTER TABLE `s_pdm_data`.`t_planning_season` COLLATE = utf8mb4_0900_ai_ci;