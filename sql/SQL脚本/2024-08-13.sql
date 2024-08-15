ALTER TABLE `s_pdm_data`.`t_basicsdatum_material_price`
MODIFY COLUMN `order_day` varchar(19) NULL DEFAULT NULL COMMENT '订货周期' AFTER `currency_name`,
MODIFY COLUMN `production_day` varchar(19) NULL DEFAULT NULL COMMENT '生产周期' AFTER `order_day`;
ALTER TABLE `s_pdm_data`.`t_basicsdatum_supplier`
ADD COLUMN `first_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '一级分类' AFTER `company_code`,
ADD COLUMN `first_class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '一级分类名称' AFTER `first_class`,
ADD COLUMN `second_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '二级分类' AFTER `first_class_name`,
ADD COLUMN `second_class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '二级分类名称' AFTER `second_class`;