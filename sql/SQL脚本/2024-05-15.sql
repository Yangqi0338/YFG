ALTER TABLE `s_pdm_data`.`t_basicsdatum_material_color`
MODIFY COLUMN `picture` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '颜色图片' AFTER `scm_status`;