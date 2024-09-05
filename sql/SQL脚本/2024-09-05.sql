--档差字段管理字段改名
ALTER TABLE `s_pdm_data`.`t_basicsdatum_difference`
CHANGE COLUMN `part_coed` `part_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部位名称' AFTER `range_difference_id`;

