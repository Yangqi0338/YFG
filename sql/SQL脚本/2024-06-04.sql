-- mango吊牌增加品名翻译和颜色编码翻译字段
ALTER TABLE `s_pdm_data`.`t_hang_tag`
ADD COLUMN `product_name_translate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'mango品牌品名翻译' AFTER `lavation_reminder_code`,
ADD COLUMN `color_code_translate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'mango品牌颜色编码翻译' AFTER `product_name_translate`;