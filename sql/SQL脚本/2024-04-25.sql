ALTER TABLE `s_pdm_data`.`t_seasonal_planning_details`
DROP COLUMN `row_index`,
DROP COLUMN `column_index`,
ADD COLUMN `style_category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式类别code' AFTER `band_code`,
ADD COLUMN `column_index` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列记录位置，用于还原排序' AFTER `company_code`,
ADD COLUMN `row_index` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行号' AFTER `column_index`;